package slatewindow.input;

import slatewindow.SlateWindow;

public abstract class InputDevice {
    private final SlateWindow window;

    public InputDevice(SlateWindow window) {
        this.window = window;
    }

    public void update() {
        // Default implementation does nothing; subclasses can override to update state
    }

    protected SlateWindow getWindow() {
        return window;
    }
}
