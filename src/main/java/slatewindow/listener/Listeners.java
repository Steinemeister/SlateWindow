package slatewindow.listener;

import slatewindow.window.SlateWindow;
import slatewindow.window.WindowCloseEvent;

/** Window-scoped listeners */
public class Listeners {
    @FunctionalInterface
    public interface ResizeListener { void invoke(SlateWindow window, int width, int height); }

    @FunctionalInterface
    public interface CloseListener { void invoke(WindowCloseEvent event); }

    @FunctionalInterface
    public interface FocusListener { void invoke(SlateWindow window, boolean focused); }
}


