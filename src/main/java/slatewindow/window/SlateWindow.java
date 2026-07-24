package slatewindow.window;

import org.lwjgl.glfw.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import slatewindow.input.keyboard.Keyboard;

import slatewindow.input.mouse.Mouse;
import slatewindow.listener.Listeners.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// Represents a GLFW window with event handling and listener support
public class SlateWindow {
    private final long handle;
    private volatile String title;
    private volatile int width;
    private volatile int height;
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final AtomicBoolean focused;

    // Callbacks must be stored to avoid GC
    private GLFWFramebufferSizeCallbackI fbSizeCallback;
    private GLFWWindowFocusCallbackI focusCallback;
    private GLFWWindowContentScaleCallbackI contentScaleCallback;
    private GLFWWindowMaximizeCallbackI maximizeCallback;
    private GLFWWindowCloseCallbackI closeCallback;

    // listeners
    private final List<ResizeListener> resizeListeners = new CopyOnWriteArrayList<>();
    private final List<CloseListener> closeListeners = new CopyOnWriteArrayList<>();
    private final List<FocusListener> focusListeners = new CopyOnWriteArrayList<>();

    private final Keyboard keyboard;
    private final Mouse mouse;

    private final AtomicInteger closeAttempts = new AtomicInteger(0);
    private static final int MAX_CLOSE_ATTEMPTS = 3;

    public SlateWindow(long handle, String title, int width, int height) {
        this.handle = handle;
        this.title = title;
        this.width = width;
        this.height = height;

        this.keyboard = new Keyboard(this);
        this.mouse = new Mouse(this);
        this.focused = new AtomicBoolean(GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE);

        registerCallbacks();
    }

    // Register GLFW callbacks for window events
    private void registerCallbacks() {

        fbSizeCallback = (h, w, hh) -> {
            this.width = w; this.height = hh;
            for (ResizeListener l : resizeListeners) l.invoke(this, w, hh);
        };
        GLFW.glfwSetFramebufferSizeCallback(handle, fbSizeCallback);

        focusCallback = (h, focused) -> {
            for (FocusListener l : focusListeners) l.invoke(this, focused);
            this.focused.set(focused);
        };
        GLFW.glfwSetWindowFocusCallback(handle, focusCallback);

        contentScaleCallback = (h, xscale, yscale) -> {
            // High DPI handling can be performed by listeners via resize or content scale
        };
        GLFW.glfwSetWindowContentScaleCallback(handle, contentScaleCallback);

        maximizeCallback = (h, maximized) -> {
            // Could notify via resize as needed
        };
        GLFW.glfwSetWindowMaximizeCallback(handle, maximizeCallback);

        closeCallback = (h) -> {
            // Prevent GLFW from destroying window automatically
            GLFW.glfwSetWindowShouldClose(h, false);
            handleCloseRequest();
        };
        GLFW.glfwSetWindowCloseCallback(handle, closeCallback);
    }

    public void update() {
        keyboard.update();
        mouse.update();
    }

    public void setIcon(String iconPath) {
        // Load image using STBImage
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer image = STBImage.stbi_load(iconPath, w, h, channels, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load icon: " + STBImage.stbi_failure_reason());
            }

            GLFWImage.Buffer iconBuffer = GLFWImage.malloc(1);
            iconBuffer.position(0)
                    .width(w.get(0))
                    .height(h.get(0))
                    .pixels(image);

            GLFW.glfwSetWindowIcon(handle, iconBuffer);

            STBImage.stbi_image_free(image);
        }
    }

    // Getters and listener management
    public long getHandle() { return handle; }
    public String getTitle() { return title; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isClosed() { return closed.get(); }
    public boolean isFocused() { return focused.get(); }
    public Keyboard getKeyboard() { return keyboard; }
    public Mouse getMouse() { return mouse; }

    public void addResizeListener(ResizeListener l) { resizeListeners.add(l); }
    public void addCloseListener(CloseListener l) { closeListeners.add(l); }
    public void addFocusListener(FocusListener l) { focusListeners.add(l); }

    /** Close and destroy the GLFW window exactly once. */
    public void close() {
        if (closed.compareAndSet(false, true)) {
            nullifyCallbacks();

            GLFW.glfwDestroyWindow(handle);

            freeCallbacks();
        }
    }

    private void freeCallbacks() {
        if (fbSizeCallback instanceof AutoCloseable) saveFree((AutoCloseable) fbSizeCallback);
        if (focusCallback instanceof AutoCloseable) saveFree((AutoCloseable) focusCallback);
        if (contentScaleCallback instanceof AutoCloseable) saveFree((AutoCloseable) contentScaleCallback);
        if (maximizeCallback instanceof AutoCloseable) saveFree((AutoCloseable) maximizeCallback);
        if (closeCallback instanceof AutoCloseable) saveFree((AutoCloseable) closeCallback);
    }

    private void saveFree(AutoCloseable callback) {
        if (callback != null) {
            try {
                callback.close();
            } catch (Exception e) {
                System.err.println("Failed to free callback: " + e.getMessage());
            }
        }
    }

    private void nullifyCallbacks() {
        fbSizeCallback = null;
        focusCallback = null;
        contentScaleCallback = null;
        maximizeCallback = null;
        closeCallback = null;
    }

    public void requestClose() {
        handleCloseRequest();
    }

    private void handleCloseRequest() {
        if (closeListeners.isEmpty()) {
            close();
            return;
        }

        WindowCloseEvent event = new WindowCloseEvent(this);
        for (CloseListener l : closeListeners) {
            l.invoke(event);
        }

        if (event.isCancelled()) {
            System.err.println("Window close request was cancelled by a listener.");
            closeAttempts.set(0); // Reset attempts if cancelled
            return;
        }

        if (!closed.get()) {
            int attempts = closeAttempts.incrementAndGet();
            if (attempts >= MAX_CLOSE_ATTEMPTS) {
                System.err.println("Warning: Window close requested " + attempts + " times but ignored by listeners. Forcing close.");
                close();
            } else {
                System.err.println("Warning: Window close requested but not handled by listeners. Attempt: "
                        + attempts + "/" + MAX_CLOSE_ATTEMPTS);
            }
        }
    }

    public void setPosition(int x, int y) { GLFW.glfwSetWindowPos(handle, x, y); }

    public void setVisible(boolean visible) {
        if (visible) GLFW.glfwShowWindow(handle); else GLFW.glfwHideWindow(handle);
    }

    // Note: glfwSwapInterval requires an active context. We'll store value and expose getter.
    private volatile int vsyncInterval = 0;

    public void setVSync(int interval) { this.vsyncInterval = interval; }
    public boolean isVSyncEnabled() { return vsyncInterval > 0; }

    public boolean isVisible() {
        return GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE;
    }
}