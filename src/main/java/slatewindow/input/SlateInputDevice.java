package slatewindow.input;

import slatewindow.window.SlateWindow;

public abstract class SlateInputDevice {
    private final SlateWindow window;

    public SlateInputDevice(SlateWindow window) {
        this.window = window;
    }

    public void update() {
        // Default implementation does nothing; subclasses can override to update state
    }

    protected SlateWindow getWindow() {
        return window;
    }
}
