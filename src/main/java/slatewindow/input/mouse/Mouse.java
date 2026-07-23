package slatewindow.input.mouse;

import org.lwjgl.glfw.GLFW;
import slatewindow.SlateWindow;
import slatewindow.input.InputDevice;
import slatewindow.input.keyboard.KeyMod;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

// Class to manage mouse input, tracking button states and cursor position
public class Mouse extends InputDevice {
    private final Map<MouseButton, MouseButtonState> buttonStates = new EnumMap<>(MouseButton.class);
    private final Set<KeyMod> activeMods = EnumSet.noneOf(KeyMod.class);

    private double cursorX, cursorY;
    private double lastCursorX, lastCursorY;
    private double deltaX, deltaY;
    private double scrollX, scrollY;

    private CursorMode cursorMode = CursorMode.NORMAL;

    public Mouse(SlateWindow window) {
        super(window);

        for (MouseButton button : MouseButton.values()) {
            buttonStates.put(button, MouseButtonState.NONE);
        }
        setCallbacks(window.getHandle());
    }

    private void setCallbacks(long windowHandle) {
        // Set GLFW mouse button callback
        GLFW.glfwSetMouseButtonCallback(windowHandle, (handle, buttonCode, action, mods) -> {
            updateModifiers(mods);

            MouseButton button = MouseButton.fromGlfwCode(buttonCode);
            if (button == MouseButton.UNKNOWN) {
                System.err.println("Unknown mouse button code: " + buttonCode);
                return;
            }

            switch (action) {
                case GLFW.GLFW_PRESS -> buttonStates.put(button, MouseButtonState.PRESSED);
                case GLFW.GLFW_RELEASE -> buttonStates.put(button, MouseButtonState.RELEASED);
                default -> {
                    buttonStates.put(button, MouseButtonState.NONE);
                    System.err.println("Unknown mouse button action: " + action);
                }
            }
        });

        // Set GLFW cursor position callback
        GLFW.glfwSetCursorPosCallback(windowHandle, (handle, xpos, ypos) -> {
            cursorX = xpos;
            cursorY = ypos;
        });

        // Set GLFW scroll callback
        GLFW.glfwSetScrollCallback(windowHandle, (handle, xoffset, yoffset) -> {
            scrollX += xoffset;
            scrollY += yoffset;
        });
    }

    private void updateModifiers(int mods) {
        activeMods.clear();
        for (KeyMod mod : KeyMod.values()) {
            if ((mods & mod.getGlfwBit()) != 0) {
                activeMods.add(mod);
            }
        }
    }

    @Override
    public void update() {
        for (Map.Entry<MouseButton, MouseButtonState> entry : buttonStates.entrySet()) {
            MouseButton button = entry.getKey();
            MouseButtonState state = entry.getValue();
            switch (state) {
                case PRESSED -> buttonStates.put(button, MouseButtonState.HELD);
                case RELEASED -> buttonStates.put(button, MouseButtonState.NONE);
                default -> {
                    // Do nothing for HELD and NONE states
                }
            }
        }

        deltaX = cursorX - lastCursorX;
        deltaY = cursorY - lastCursorY;

        lastCursorX = cursorX;
        lastCursorY = cursorY;

        scrollX = 0;
        scrollY = 0;

        if (this.getWindow().isFocused()) {
            GLFW.glfwSetInputMode(this.getWindow().getHandle(), GLFW.GLFW_CURSOR, cursorMode.getGlfwMode());

            if (cursorMode == CursorMode.DISABLED) {
                double[] x = new double[1];
                double[] y = new double[1];
                GLFW.glfwGetCursorPos(this.getWindow().getHandle(), x, y);
                this.cursorX = x[0];
                this.cursorY = y[0];
                this.lastCursorX = x[0];
                this.lastCursorY = y[0];
                this.deltaX = 0;
                this.deltaY = 0;
            }
        } else {
            GLFW.glfwSetInputMode(this.getWindow().getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }

    public void setCursorMode(CursorMode cursorMode) {
        this.cursorMode = cursorMode;
        //GLFW.glfwSetInputMode(this.getWindow().getHandle(), GLFW.GLFW_CURSOR, cursorMode.getGlfwMode());
    }

    public CursorMode getCursorMode() {
        return cursorMode;
    }

    public MouseButtonState getState(MouseButton button) {
        return buttonStates.get(button);
    }

    public boolean isPressed(MouseButton button) {
        return buttonStates.get(button) == MouseButtonState.PRESSED;
    }

    public boolean isHeld(MouseButton button) {
        return buttonStates.get(button) == MouseButtonState.HELD;
    }

    public boolean isReleased(MouseButton button) {
        return buttonStates.get(button) == MouseButtonState.RELEASED;
    }

    public boolean isModifierActive(KeyMod mod) {
        return activeMods.contains(mod);
    }

    public double getX() { return cursorX; }
    public double getY() { return cursorY; }
    public double getDx() { return deltaX; }
    public double getDy() { return deltaY; }
    public double getScrollX() { return scrollX; }
    public double getScrollY() { return scrollY; }
}
