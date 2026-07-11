package slatewindow;

import org.lwjgl.glfw.GLFW;

public enum GamepadButton {
    A, B, X, Y,
    LEFT_BUMPER, RIGHT_BUMPER,
    BACK, START, GUIDE,
    LEFT_THUMB, RIGHT_THUMB,
    DPAD_UP, DPAD_RIGHT, DPAD_DOWN, DPAD_LEFT,
    UNKNOWN;

    public static GamepadButton fromGlfw(int id) {
        switch (id) {
            case GLFW.GLFW_GAMEPAD_BUTTON_A: return A;
            case GLFW.GLFW_GAMEPAD_BUTTON_B: return B;
            case GLFW.GLFW_GAMEPAD_BUTTON_X: return X;
            case GLFW.GLFW_GAMEPAD_BUTTON_Y: return Y;
            case GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER: return LEFT_BUMPER;
            case GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER: return RIGHT_BUMPER;
            case GLFW.GLFW_GAMEPAD_BUTTON_BACK: return BACK;
            case GLFW.GLFW_GAMEPAD_BUTTON_START: return START;
            case GLFW.GLFW_GAMEPAD_BUTTON_GUIDE: return GUIDE;
            case GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB: return LEFT_THUMB;
            case GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB: return RIGHT_THUMB;
            case GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP: return DPAD_UP;
            case GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT: return DPAD_RIGHT;
            case GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN: return DPAD_DOWN;
            case GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT: return DPAD_LEFT;
            default: return UNKNOWN;
        }
    }
}

