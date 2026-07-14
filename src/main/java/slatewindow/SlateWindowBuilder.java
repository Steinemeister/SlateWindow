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

    protected final List<ResizeListener> resizeListeners = new ArrayList<>();
    protected final List<CloseListener> closeListeners = new ArrayList<>();
    protected final List<FocusListener> focusListeners = new ArrayList<>();

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

    public B onResize(ResizeListener l) { resizeListeners.add(l); return self(); }
    public B onClose(CloseListener l) { closeListeners.add(l); return self(); }
    public B onFocus(FocusListener l) { focusListeners.add(l); return self(); }

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
        resizeListeners.forEach(window::addResizeListener);
        closeListeners.forEach(window::addCloseListener);
        focusListeners.forEach(window::addFocusListener);

        return window;
    }
}

