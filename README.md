# SlateWindow

A **graphics-API-agnostic** Java library for multi-window management using LWJGL3/GLFW. Supports keyboard, mouse, gamepad, joystick, and touch input handling with a flexible listener-based event system.

## Table of Contents

- [Key Features](#key-features)
- [Architecture & Design Philosophy](#architecture--design-philosophy)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [API Documentation](#api-documentation)
  - [Window Creation & Management](#window-creation--management)
  - [Event Listeners (Input Handling)](#event-listeners-input-handling)
  - [Gamepad & Joystick Support](#gamepad--joystick-support)
- [Examples](#examples)
- [Threading Model](#threading-model)
- [Building & Running](#building--running)

---

## Key Features

- **Graphics-API Agnostic**: Uses `GLFW_NO_API` mode—no OpenGL/Vulkan context binding. Full control over rendering pipeline.
- **Multi-Window Support**: Create and manage multiple windows simultaneously with independent input handling.
- **Comprehensive Input System**: Keyboard, mouse (movement, buttons, scroll), gamepad, joystick, SpaceMouse, and touch input.
- **Event-Driven Architecture**: Register listeners for window and peripheral events; listeners are thread-safe via `CopyOnWriteArrayList`.
- **Cross-Platform**: Runs on Windows, Linux, and macOS (x86_64 and ARM64) via LWJGL's native bindings.
- **Type-Safe Builder Pattern**: Fluent API for window configuration with integrated listener registration.

---

## Architecture & Design Philosophy

### Why `GLFW_NO_API`?

SlateWindow deliberately avoids binding a graphics context (OpenGL, Vulkan, etc.). This design choice offers:

1. **Rendering Freedom**: Use any graphics library (OpenGL, Vulkan, Direct3D, WebGPU, etc.) or none at all.
2. **Multi-Threaded Rendering**: Run rendering on dedicated threads independent of the main GLFW event thread.
3. **Engine Independence**: Integrate with existing game engines or custom rendering pipelines.

### Event Processing Model

- **Main Thread**: `manager.update()` polls GLFW events on the main thread and dispatches to registered listeners.
- **Listener Threads**: Listeners can run on the calling thread (typically the main thread) but should not block.
- **Rendering Threads**: You manage rendering on separate threads—SlateWindow provides window handles and state queries.

### Thread Safety

- `SlateWindow` stores window state in **volatile** fields and **atomic** flags for safe multi-threaded access.
- Listener lists use `CopyOnWriteArrayList` to allow safe iteration during concurrent modifications.
- GLFW operations (except `glfwPollEvents()`) are thread-safe if called outside the event processing loop.

---

## Requirements

- **Java**: 21+
- **LWJGL**: 3.3.2 (included via Gradle)
- **GLFW**: 3.x (provided by LWJGL)
- **OS**: Windows, Linux, or macOS with native bindings

### Gradle Dependency

```gradle
dependencies {
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    runtimeOnly("org.lwjgl:lwjgl::natives-windows")      // or your platform
    runtimeOnly("org.lwjgl:lwjgl-glfw::natives-windows")
}
```

---

## Quick Start

### 1. Initialize the Manager

```java
SlateWindowManager manager = new SlateWindowManager();
manager.init();  // Initializes GLFW and sets up joystick callbacks
```

### 2. Create a Window

```java
SlateWindow window = manager.builder()
    .title("My Application")
    .size(1280, 720)
    .build();
```

The builder automatically registers the window with the manager.

### 3. Main Loop with Event Processing

```java
while (!window.isClosed()) {
    manager.update();  // Poll GLFW events and update input state
    
    // Your rendering logic here (or on separate thread)
    // window.getHandle() provides the GLFW window handle
}
```

### 4. Cleanup

```java
manager.terminate();  // Closes all windows and terminates GLFW
```

---

## API Documentation

### Window Creation & Management

#### SlateWindowManager

The central hub for window and peripheral management.

**Methods:**

| Method | Description |
|--------|-------------|
| `void init()` | Initializes GLFW. Must be called before creating windows. |
| `<W extends SlateWindow> SlateWindowBuilder<W, ?> builder()` | Returns a builder for creating windows. |
| `<W extends SlateWindow> SlateWindowBuilder<W, ?> builder(SlateWindowFactory<W> factory)` | Returns a builder with a custom window factory. |
| `void registerWindow(SlateWindow window)` | Registers a window (auto-called by builder). |
| `List<SlateMonitorInfo> getAvailableMonitors()` | Lists all connected monitors with their properties. |
| `void update()` | Polls GLFW events and updates gamepad/joystick state. **Call once per frame.** |
| `void terminate()` | Closes all windows and terminates GLFW. |

#### SlateWindowBuilder

A fluent builder for configuring windows before creation.

**Configuration Methods:**

```java
builder
    .size(int width, int height)           // Window dimensions (default: 800x600)
    .title(String title)                   // Window title (default: "SlateWindow")
    .decorated(boolean decorated)          // Enable window decoration (default: true)
    .maximized(boolean maximized)          // Start maximized (default: false)
    .build()                               // Create and return the window
```

#### SlateWindow

Represents a single GLFW window.

**Query Methods:**

```java
long   getHandle()         // GLFW window handle for low-level operations
String getTitle()          // Current window title
int    getWidth()          // Framebuffer width
int    getHeight()         // Framebuffer height
boolean isClosed()         // Whether window is closed or closing
boolean isVisible()        // Window visibility state
boolean isVSyncEnabled()   // Check VSync interval
```

**Control Methods:**

```java
void setPosition(int x, int y)     // Move window to screen position
void setVisible(boolean visible)   // Show or hide window
void setVSync(int interval)        // Set VSync interval (0=disabled)
void close()                       // Close window (idempotent; safe to call multiple times)
```

---

### Event Listeners (Input Handling)

SlateWindow uses a listener-based event system. All listeners are **functional interfaces** and can be registered at build time or runtime.

#### Window-Scoped Listeners

Register these on individual windows via `SlateWindowBuilder` or directly:

```java
window.addKeyListener(listener);
window.addMouseButtonListener(listener);
window.addMouseMoveListener(listener);
window.addScrollListener(listener);
window.addResizeListener(listener);
window.addFocusListener(listener);
window.addCloseListener(listener);
window.addTouchListener(listener);
```

##### KeyListener

Fired when a key is pressed or released.

```java
@FunctionalInterface
public interface KeyListener {
    void invoke(SlateWindow window, int key, int scancode, int action, int mods);
}
```

- `key`: GLFW key constant (e.g., `GLFW.GLFW_KEY_A`)
- `scancode`: Platform-specific scan code
- `action`: `GLFW.GLFW_PRESS`, `GLFW.GLFW_RELEASE`, or `GLFW.GLFW_REPEAT`
- `mods`: Modifier flags (Shift, Ctrl, Alt, Super)

**Example:**

```java
window.addKeyListener((w, key, scancode, action, mods) -> {
    if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
        w.close();
    }
});
```

##### MouseButtonListener

Fired when a mouse button is pressed or released.

```java
@FunctionalInterface
public interface MouseButtonListener {
    void invoke(SlateWindow window, int button, int action, int mods);
}
```

- `button`: `GLFW.GLFW_MOUSE_BUTTON_LEFT`, `_MIDDLE`, `_RIGHT`, or 3-7
- `action`: `GLFW.GLFW_PRESS` or `GLFW.GLFW_RELEASE`

##### MouseMoveListener

Fired when the mouse cursor moves within the window.

```java
@FunctionalInterface
public interface MouseMoveListener {
    void invoke(SlateWindow window, double xpos, double ypos);
}
```

- `xpos`, `ypos`: Cursor position in window coordinates (0,0 at top-left)

##### ScrollListener

Fired when scrolling (mouse wheel or trackpad).

```java
@FunctionalInterface
public interface ScrollListener {
    void invoke(SlateWindow window, double xoffset, double yoffset);
}
```

- `xoffset`, `yoffset`: Scroll offset (typically 1.0 or -1.0)

##### ResizeListener

Fired when the framebuffer is resized.

```java
@FunctionalInterface
public interface ResizeListener {
    void invoke(SlateWindow window, int width, int height);
}
```

**Note:** SlateWindow automatically updates internal width/height. This listener is for custom resize handling (e.g., viewport updates).

##### FocusListener

Fired when the window gains or loses focus.

```java
@FunctionalInterface
public interface FocusListener {
    void invoke(SlateWindow window, boolean focused);
}
```

##### CloseListener

Fired when the user requests window closure (e.g., clicking the close button).

```java
@FunctionalInterface
public interface CloseListener {
    void invoke(SlateWindow window);
}
```

**Note:** If no close listeners are registered, the window closes automatically. If listeners are present, they must explicitly call `window.close()`.

##### TouchListener

Emulated touch input from mouse button events. Useful for touch-enabled displays or testing.

```java
@FunctionalInterface
public interface TouchListener {
    void invoke(SlateWindow window, int id, int action, double xpos, double ypos);
}
```

- `id`: Touch point ID (0 for single-touch emulation)
- `action`: Same as mouse button action

---

### Gamepad & Joystick Support

SlateWindow provides extensive support for gamepads, joysticks, and specialized devices like SpaceMouse.

#### Registration

Register listeners on the manager (not individual windows):

```java
manager.addGamepadButtonListener(listener);
manager.addGamepadAxisListener(listener);
manager.addGamepadConnectionListener(listener);
manager.addJoystickListener(listener);
manager.addSpaceMouseListener(listener);
```

#### GamepadButtonListener

Fired when a gamepad button is pressed or released. Only active for devices recognized as gamepads by GLFW.

```java
@FunctionalInterface
public interface GamepadButtonListener {
    void invoke(int jid, GamepadButton button, InputAction action);
}
```

- `jid`: Joystick ID (GLFW.GLFW_JOYSTICK_1 + index)
- `button`: `GamepadButton` enum (A, B, X, Y, LB, RB, BACK, START, LEFT_THUMB, RIGHT_THUMB, GUIDE)
- `action`: `InputAction.PRESS` or `InputAction.RELEASE`

**Example:**

```java
manager.addGamepadButtonListener((jid, button, action) -> {
    if (button == GamepadButton.A && action == InputAction.PRESS) {
        System.out.println("Button A pressed on gamepad " + jid);
    }
});
```

#### GamepadAxisListener

Fired when a gamepad analog axis value changes beyond the deadzone (0.05).

```java
@FunctionalInterface
public interface GamepadAxisListener {
    void invoke(int jid, GamepadAxis axis, float value);
}
```

- `axis`: `GamepadAxis` enum (LEFT_X, LEFT_Y, RIGHT_X, RIGHT_Y, LEFT_TRIGGER, RIGHT_TRIGGER)
- `value`: Normalized value from -1.0 to 1.0

**Example:**

```java
manager.addGamepadAxisListener((jid, axis, value) -> {
    if (axis == GamepadAxis.LEFT_X) {
        System.out.printf("Left stick X: %.2f%n", value);
    }
});
```

#### GamepadConnectionListener

Fired when a gamepad connects or disconnects.

```java
@FunctionalInterface
public interface GamepadConnectionListener {
    void invoke(int jid, boolean connected);
}
```

#### JoystickListener

For generic joysticks (not recognized as gamepads), provides raw axis and button data.

```java
@FunctionalInterface
public interface JoystickListener {
    void invoke(int jid, float[] axes, byte[] buttons);
}
```

- `axes`: Array of raw axis values (normalized, -1.0 to 1.0)
- `buttons`: Array of button states (1 = pressed, 0 = released)

#### SpaceMouseListener

Specialized support for 3Dconnexion SpaceMouse and similar 6-DOF devices.

```java
@FunctionalInterface
public interface SpaceMouseListener {
    void invoke(int jid, float tx, float ty, float tz, float rx, float ry, float rz, byte[] buttons);
}
```

- `tx, ty, tz`: Translation (movement) along X, Y, Z axes
- `rx, ry, rz`: Rotation around X, Y, Z axes
- `buttons`: Device-specific button states

**Example:**

```java
manager.addSpaceMouseListener((jid, tx, ty, tz, rx, ry, rz, buttons) -> {
    System.out.printf("SpaceMouse: T(%.2f, %.2f, %.2f) R(%.2f, %.2f, %.2f)%n",
        tx, ty, tz, rx, ry, rz);
});
```

---

## Examples

### Example 1: Simple Single-Window Application

```java
import org.lwjgl.glfw.GLFW;
import slatewindow.*;

public class SimpleWindow {
    public static void main(String[] args) {
        SlateWindowManager manager = new SlateWindowManager();
        manager.init();
        
        SlateWindow window = manager.builder()
            .title("Hello, SlateWindow")
            .size(800, 600)
            .onClose(w -> System.out.println("Window closing..."))
            .onKey((w, key, scancode, action, mods) -> {
                if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                    w.close();
                }
            })
            .build();
        
        while (!window.isClosed()) {
            manager.update();
            // Perform rendering here on a separate thread
        }
        
        manager.terminate();
    }
}
```

### Example 2: Multi-Window Application

```java
SlateWindowManager manager = new SlateWindowManager();
manager.init();

SlateWindow window1 = manager.builder().title("Window 1").size(600, 400).build();
SlateWindow window2 = manager.builder().title("Window 2").size(800, 600).build();

while (!window1.isClosed() || !window2.isClosed()) {
    manager.update();
    
    if (window1.isVisible()) {
        // Render window1
    }
    if (window2.isVisible()) {
        // Render window2
    }
}

manager.terminate();
```

### Example 3: Gamepad Input Handling

```java
SlateWindowManager manager = new SlateWindowManager();
manager.init();

manager.addGamepadConnectionListener((jid, connected) -> {
    System.out.println("Gamepad " + jid + (connected ? " connected" : " disconnected"));
});

manager.addGamepadButtonListener((jid, button, action) -> {
    System.out.println("Button " + button + ": " + action);
});

manager.addGamepadAxisListener((jid, axis, value) -> {
    if (Math.abs(value) > 0.5f) {
        System.out.println(axis + ": " + value);
    }
});

SlateWindow window = manager.builder().title("Gamepad Demo").build();

while (!window.isClosed()) {
    manager.update();
}

manager.terminate();
```

### Example 4: Mouse Tracking & Click Handling

```java
SlateWindow window = manager.builder()
    .onMouseMove((w, xpos, ypos) -> {
        System.out.printf("Mouse: (%.0f, %.0f)%n", xpos, ypos);
    })
    .onMouseButton((w, button, action, mods) -> {
        String btn = (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) ? "LEFT" : "RIGHT";
        String act = (action == GLFW.GLFW_PRESS) ? "pressed" : "released";
        System.out.println(btn + " button " + act);
    })
    .onScroll((w, xoffset, yoffset) -> {
        System.out.printf("Scroll: (%.1f, %.1f)%n", xoffset, yoffset);
    })
    .build();
```

### Example 5: Window Resize Handling

```java
SlateWindow window = manager.builder()
    .title("Resize Aware")
    .size(800, 600)
    .onResize((w, width, height) -> {
        System.out.printf("Window resized to %d x %d%n", width, height);
        // Update your projection matrix or viewport here
    })
    .build();
```

---

## Threading Model

### Main Thread Requirements

- **`manager.update()`** must be called on the main thread.
- All GLFW operations (window creation, polling events) are single-threaded.

### Rendering Threads

- You can render on **any thread** by obtaining the window handle:

```java
long windowHandle = window.getHandle();

// On a rendering thread:
// - Make the context current (if using OpenGL)
// - Perform rendering
// - Call glfwSwapBuffers() if double-buffered
```

### Safe Multi-Threaded Access

- `SlateWindow` field reads (title, width, height, closed state) are volatile and safe.
- Listener invocation is thread-safe during `manager.update()`.

### Example: Rendering on a Separate Thread

```java
SlateWindowManager manager = new SlateWindowManager();
manager.init();

SlateWindow window = manager.builder().title("Async Render").build();

Thread renderThread = new Thread(() -> {
    while (!window.isClosed()) {
        int w = window.getWidth();
        int h = window.getHeight();
        // Render to the window's framebuffer
        // (implementation depends on your graphics library)
    }
});
renderThread.start();

while (!window.isClosed()) {
    manager.update();
    Thread.sleep(16);  // ~60 FPS
}

renderThread.join();
manager.terminate();
```

---

## Building & Running

### With Gradle

```bash
# Build
./gradlew build

# Run example
./gradlew run

# Run tests
./gradlew test
```

### Manual Compilation

```bash
javac -cp "lwjgl/*" src/main/java/slatewindow/*.java
java -cp ".:lwjgl/*" slatewindow.example.Demo
```

**Note:** Native libraries (`lwjgl-natives-*`) must be on the class/module path.

---

## License & Contributing

See the LICENSE file for licensing information. Contributions are welcome—please submit issues and pull requests to the repository.

---

## FAQ

**Q: Can I use SlateWindow with OpenGL?**  
A: Yes! Create an OpenGL context manually using the window handle and render on a separate thread.

**Q: Why no built-in OpenGL support?**  
A: Flexibility. SlateWindow focuses on window/input management. Rendering is your responsibility.

**Q: Is this thread-safe?**  
A: Window queries (size, title, closed) are thread-safe. GLFW operations must be on the main thread. Rendering can be on any thread.

**Q: Can I use custom window implementations?**  
A: Yes, via `SlateWindowFactory` and the generic `SlateWindowBuilder<W, ?>`.

**Q: How do I handle high-DPI displays?**  
A: Query framebuffer size via `window.getWidth()` / `window.getHeight()`; compare with logical window size via GLFW to compute DPI scaling.

