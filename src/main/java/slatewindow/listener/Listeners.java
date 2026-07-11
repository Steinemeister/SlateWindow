package slatewindow.listener;

import slatewindow.GamepadAxis;
import slatewindow.GamepadButton;
import slatewindow.InputAction;
import slatewindow.SlateWindow;

/** Window-scoped listeners */
public class Listeners {
    @FunctionalInterface
    public interface KeyListener { void invoke(int windowHandle, int key, int scancode, int action, int mods); }

    @FunctionalInterface
    public interface ResizeListener { void invoke(int windowHandle, int width, int height); }

    @FunctionalInterface
    public interface CloseListener { void invoke(SlateWindow window); }

    @FunctionalInterface
    public interface MouseButtonListener { void invoke(int windowHandle, int button, int action, int mods); }

    @FunctionalInterface
    public interface MouseMoveListener { void invoke(int windowHandle, double xpos, double ypos); }

    @FunctionalInterface
    public interface ScrollListener { void invoke(int windowHandle, double xoffset, double yoffset); }

    @FunctionalInterface
    public interface FocusListener { void invoke(int windowHandle, boolean focused); }

    @FunctionalInterface
    public interface TouchListener { void invoke(int id, int action, double xpos, double ypos); }

    /** Global peripheral listeners for SlateWindowManager */
    @FunctionalInterface
    public interface GamepadButtonListener { void invoke(int jid, GamepadButton button, InputAction action); }

    @FunctionalInterface
    public interface GamepadAxisListener { void invoke(int jid, GamepadAxis axis, float value); }

    @FunctionalInterface
    public interface GamepadConnectionListener { void invoke(int jid, boolean connected); }

    @FunctionalInterface
    public interface JoystickListener { void invoke(int jid, float[] axes, byte[] buttons); }

    @FunctionalInterface
    public interface SpaceMouseListener { void invoke(int jid, float translationX, float translationY, float translationZ, float rotationX, float rotationY, float rotationZ, byte[] buttons); }
}


