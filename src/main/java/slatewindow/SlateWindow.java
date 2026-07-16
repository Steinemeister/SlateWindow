package slatewindow;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;
import org.lwjgl.glfw.GLFWWindowContentScaleCallbackI;
import org.lwjgl.glfw.GLFWWindowMaximizeCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import slatewindow.input.keyboard.Keyboard;

import slatewindow.input.mouse.Mouse;
import slatewindow.listener.Listeners.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

// Represents a GLFW window with event handling and listener support
public class SlateWindow {
    private final long handle;
    private volatile String title;
    private volatile int width;
    private volatile int height;
    private final AtomicBoolean closed = new AtomicBoolean(false);

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

    public SlateWindow(long handle, String title, int width, int height) {
        this.handle = handle;
        this.title = title;
        this.width = width;
        this.height = height;

        this.keyboard = new Keyboard(handle);
        this.mouse = new Mouse(handle);

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
            if (closeListeners.isEmpty()) {
                close();
            } else {
                for (CloseListener l : closeListeners) l.invoke((this));
            }
        };
        GLFW.glfwSetWindowCloseCallback(handle, closeCallback);
    }

    public void update() {
        keyboard.update();
        mouse.update();
    }

    // Getters and listener management
    public long getHandle() { return handle; }
    public String getTitle() { return title; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isClosed() { return closed.get(); }
    public Keyboard getKeyboard() { return keyboard; }
    public Mouse getMouse() { return mouse; }

    public void addResizeListener(ResizeListener l) { resizeListeners.add(l); }
    public void addCloseListener(CloseListener l) { closeListeners.add(l); }
    public void addFocusListener(FocusListener l) { focusListeners.add(l); }

    /** Close and destroy the GLFW window exactly once. */
    public void close() {
        if (closed.compareAndSet(false, true)) {
            GLFW.glfwDestroyWindow(handle);
        }
    }

    public void requestClose() {
        if (closeListeners.isEmpty()) {
            close();
        } else {
            for (CloseListener l : closeListeners) l.invoke((this));
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

