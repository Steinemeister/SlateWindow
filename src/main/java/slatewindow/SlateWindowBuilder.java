package slatewindow;

import org.lwjgl.glfw.GLFW;

import slatewindow.listener.Listeners.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic builder for SlateWindow instances.
 */
public class SlateWindowBuilder<W extends SlateWindow, B extends SlateWindowBuilder<W,B>> {
    protected int width = 800;
    protected int height = 600;
    protected String title = "SlateWindow";
    protected boolean decorated = true;
    protected boolean maximized = false;

    protected final List<KeyListener> keyListeners = new ArrayList<>();
    protected final List<ResizeListener> resizeListeners = new ArrayList<>();
    protected final List<CloseListener> closeListeners = new ArrayList<>();
    protected final List<MouseButtonListener> mouseButtonListeners = new ArrayList<>();
    protected final List<MouseMoveListener> mouseMoveListeners = new ArrayList<>();
    protected final List<ScrollListener> scrollListeners = new ArrayList<>();
    protected final List<FocusListener> focusListeners = new ArrayList<>();
    protected final List<TouchListener> touchListeners = new ArrayList<>();

    private final SlateWindowFactory<W> factory;

    public SlateWindowBuilder(SlateWindowFactory<W> factory) {
        this.factory = factory;
    }

    @SuppressWarnings("unchecked")
    protected B self() { return (B) this; }

    public B size(int w, int h) { this.width = w; this.height = h; return self(); }
    public B title(String t) { this.title = t; return self(); }
    public B decorated(boolean d) { this.decorated = d; return self(); }
    public B maximized(boolean m) { this.maximized = m; return self(); }

    public B onKey(KeyListener l) { keyListeners.add(l); return self(); }
    public B onResize(ResizeListener l) { resizeListeners.add(l); return self(); }
    public B onClose(CloseListener l) { closeListeners.add(l); return self(); }
    public B onMouseButton(MouseButtonListener l) { mouseButtonListeners.add(l); return self(); }
    public B onMouseMove(MouseMoveListener l) { mouseMoveListeners.add(l); return self(); }
    public B onScroll(ScrollListener l) { scrollListeners.add(l); return self(); }
    public B onFocus(FocusListener l) { focusListeners.add(l); return self(); }
    public B onTouch(TouchListener l) { touchListeners.add(l); return self(); }

    public W build() {
        // Reset hints and enforce no client API
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_NO_API);
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, decorated ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
        if (maximized) GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        long handle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (handle == 0L) throw new RuntimeException("Failed to create GLFW window");

        W window = factory.create(handle, title, width, height);

        // register listeners
        keyListeners.forEach(window::addKeyListener);
        resizeListeners.forEach(window::addResizeListener);
        closeListeners.forEach(window::addCloseListener);
        mouseButtonListeners.forEach(window::addMouseButtonListener);
        mouseMoveListeners.forEach(window::addMouseMoveListener);
        scrollListeners.forEach(window::addScrollListener);
        focusListeners.forEach(window::addFocusListener);
        touchListeners.forEach(window::addTouchListener);

        return window;
    }
}

