package slatewindow;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import slatewindow.listener.*;

import slatewindow.listener.Listeners.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manager that holds windows and global peripheral listeners.
 */
public class SlateWindowManager {
    private final List<SlateWindow> windows = new CopyOnWriteArrayList<>();
    private final AtomicBoolean initialized = new AtomicBoolean(false);


    public void init() {
        if (initialized.compareAndSet(false, true)) {
            if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        }
    }

    public List<SlateMonitorInfo> getAvailableMonitors() {
        PointerBuffer monitors = GLFW.glfwGetMonitors();
        List<SlateMonitorInfo> list = new ArrayList<>();
        if (monitors == null) return list;
        for (int i = 0; i < monitors.limit(); i++) {
            long mon = monitors.get(i);
            String name = GLFW.glfwGetMonitorName(mon);
            GLFWVidMode vm = GLFW.glfwGetVideoMode(mon);
            if (vm != null) list.add(new SlateMonitorInfo(name, vm.width(), vm.height(), vm.refreshRate()));
        }
        return list;
    }

    public <W extends SlateWindow> SlateWindowBuilder<W, ?> builder() {
        return builder((handle, title, w, h) -> (W) new SlateWindow(handle, title, w, h));
    }

    public <W extends SlateWindow> SlateWindowBuilder<W, ?> builder(SlateWindowFactory<W> factory) {
        // wrap factory so created windows are automatically registered
        SlateWindowFactory<W> wrapped = (handle, title, w, h) -> {
            W win = factory.create(handle, title, w, h);
            registerWindow(win);
            return win;
        };
        return new SlateWindowBuilder<>(wrapped);
    }

    public void registerWindow(SlateWindow window) {
        windows.add(window);
    }

    public void update() {
        // Must be called on main thread
        GLFW.glfwPollEvents();


        // Remove closed windows
        windows.removeIf(SlateWindow::isClosed);
    }

    public void terminate() {
        for (SlateWindow w : windows) {
            if (!w.isClosed()) w.close();
        }
        windows.clear();
        if (initialized.get()) GLFW.glfwTerminate();
    }
}

