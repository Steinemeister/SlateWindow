package slatewindow;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;
import org.lwjgl.glfw.GLFWWindowContentScaleCallbackI;
import org.lwjgl.glfw.GLFWWindowMaximizeCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import slatewindow.listener.*;

import slatewindow.listener.Listeners.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class SlateWindow {
    private final long handle;
    private volatile String title;
    private volatile int width;
    private volatile int height;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    // Callbacks must be stored to avoid GC
    private GLFWKeyCallbackI keyCallback;
    private GLFWMouseButtonCallbackI mouseButtonCallback;
    private GLFWCursorPosCallbackI cursorPosCallback;
    private GLFWScrollCallbackI scrollCallback;
    private GLFWFramebufferSizeCallbackI fbSizeCallback;
    private GLFWWindowFocusCallbackI focusCallback;
    private GLFWWindowContentScaleCallbackI contentScaleCallback;
    private GLFWWindowMaximizeCallbackI maximizeCallback;
    private GLFWWindowCloseCallbackI closeCallback;

    // listeners
    private final List<KeyListener> keyListeners = new CopyOnWriteArrayList<>();
    private final List<ResizeListener> resizeListeners = new CopyOnWriteArrayList<>();
    private final List<CloseListener> closeListeners = new CopyOnWriteArrayList<>();
    private final List<MouseButtonListener> mouseButtonListeners = new CopyOnWriteArrayList<>();
    private final List<MouseMoveListener> mouseMoveListeners = new CopyOnWriteArrayList<>();
    private final List<ScrollListener> scrollListeners = new CopyOnWriteArrayList<>();
    private final List<FocusListener> focusListeners = new CopyOnWriteArrayList<>();
    private final List<TouchListener> touchListeners = new CopyOnWriteArrayList<>();

    public SlateWindow(long handle, String title, int width, int height) {
        this.handle = handle;
        this.title = title;
        this.width = width;
        this.height = height;

        registerCallbacks();
    }

    private void registerCallbacks() {
        keyCallback = (h, key, scancode, action, mods) -> {
            for (KeyListener l : keyListeners) l.invoke((int)h, key, scancode, action, mods);
        };
        GLFW.glfwSetKeyCallback(handle, keyCallback);

        mouseButtonCallback = (h, button, action, mods) -> {
            for (Listeners.MouseButtonListener l : mouseButtonListeners) l.invoke((int)h, (int)button, (int)action, mods);
            // Emulate simple touch: single touch id 0
            if (!touchListeners.isEmpty()) {
                // Query cursor position
                org.lwjgl.system.MemoryStack stack = org.lwjgl.system.MemoryStack.stackGet();
                try (org.lwjgl.system.MemoryStack stack2 = org.lwjgl.system.MemoryStack.stackPush()) {
                    java.nio.DoubleBuffer xb = stack2.mallocDouble(1);
                    java.nio.DoubleBuffer yb = stack2.mallocDouble(1);
                    GLFW.glfwGetCursorPos(h, xb, yb);
                    double xpos = xb.get(0);
                    double ypos = yb.get(0);
                    for (TouchListener t : touchListeners) {
                        t.invoke(0, (int)action, xpos, ypos);
                    }
                }
            }
        };
        GLFW.glfwSetMouseButtonCallback(handle, mouseButtonCallback);

        cursorPosCallback = (h, xpos, ypos) -> {
            for (MouseMoveListener l : mouseMoveListeners) l.invoke((int)h, xpos, ypos);
        };
        GLFW.glfwSetCursorPosCallback(handle, cursorPosCallback);

        scrollCallback = (h, xoffset, yoffset) -> {
            for (ScrollListener l : scrollListeners) l.invoke((int)h, xoffset, yoffset);
        };
        GLFW.glfwSetScrollCallback(handle, scrollCallback);

        fbSizeCallback = (h, w, hh) -> {
            this.width = w; this.height = hh;
            for (ResizeListener l : resizeListeners) l.invoke((int)h, w, hh);
        };
        GLFW.glfwSetFramebufferSizeCallback(handle, fbSizeCallback);

        focusCallback = (h, focused) -> {
            for (FocusListener l : focusListeners) l.invoke((int)h, focused);
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

    public long getHandle() { return handle; }
    public String getTitle() { return title; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isClosed() { return closed.get(); }

    public void addKeyListener(KeyListener l) { keyListeners.add(l); }
    public void addResizeListener(ResizeListener l) { resizeListeners.add(l); }
    public void addCloseListener(CloseListener l) { closeListeners.add(l); }
    public void addMouseButtonListener(MouseButtonListener l) { mouseButtonListeners.add(l); }
    public void addMouseMoveListener(MouseMoveListener l) { mouseMoveListeners.add(l); }
    public void addScrollListener(ScrollListener l) { scrollListeners.add(l); }
    public void addFocusListener(FocusListener l) { focusListeners.add(l); }
    public void addTouchListener(TouchListener l) { touchListeners.add(l); }

    /** Close and destroy the GLFW window exactly once. */
    public void close() {
        if (closed.compareAndSet(false, true)) {
            GLFW.glfwDestroyWindow(handle);
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

