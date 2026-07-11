package slatewindow;

import org.lwjgl.glfw.GLFW;

public enum GamepadAxis {
    LEFT_X, LEFT_Y, RIGHT_X, RIGHT_Y, LEFT_TRIGGER, RIGHT_TRIGGER, UNKNOWN;

    public static GamepadAxis fromGlfw(int id) {
        switch (id) {
            case GLFW.GLFW_GAMEPAD_AXIS_LEFT_X: return LEFT_X;
            case GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y: return LEFT_Y;
            case GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X: return RIGHT_X;
            case GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y: return RIGHT_Y;
            case GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER: return LEFT_TRIGGER;
            case GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER: return RIGHT_TRIGGER;
            default: return UNKNOWN;
        }
    }
}

