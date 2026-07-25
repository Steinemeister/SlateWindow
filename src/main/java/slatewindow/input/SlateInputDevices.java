package slatewindow.input;

import slatewindow.input.keyboard.Keyboard;
import slatewindow.input.mouse.Mouse;
import slatewindow.window.SlateWindow;

import java.util.function.Function;

public enum SlateInputDevices {
    KEYBOARD(Keyboard::new),
    MOUSE(Mouse::new);

    private final Function<SlateWindow, SlateInputDevice> factory;

    SlateInputDevices(Function<SlateWindow, SlateInputDevice> factory) {
        this.factory = factory;
    }

    @SuppressWarnings("unchecked")
    public <T extends SlateInputDevice> T create(SlateWindow window) {
        return (T) factory.apply(window);
    }
}
