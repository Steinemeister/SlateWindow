package slatewindow.window;

public class WindowCloseEvent {
    private final SlateWindow window;
    private boolean cancelled = false;

    public WindowCloseEvent(SlateWindow window) {
        this.window = window;
    }

    public SlateWindow getWindow() { return window; }
    public boolean isCancelled() { return cancelled; }

    public void cancel() { this.cancelled = true; }
}
