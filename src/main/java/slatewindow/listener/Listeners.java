package slatewindow.listener;

import slatewindow.SlateWindow;

/** Window-scoped listeners */
public class Listeners {
    @FunctionalInterface
    public interface ResizeListener { void invoke(SlateWindow window, int width, int height); }

    @FunctionalInterface
    public interface CloseListener { void invoke(SlateWindow window); }

    @FunctionalInterface
    public interface FocusListener { void invoke(SlateWindow window, boolean focused); }
}


