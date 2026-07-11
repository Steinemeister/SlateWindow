package slatewindow;

import org.lwjgl.glfw.GLFW;

public enum MouseButton {
    LEFT,
    RIGHT,
    MIDDLE,
    UNKNOWN;

    public static MouseButton fromGlfw(int id) {
        switch (id) {
            case GLFW.GLFW_MOUSE_BUTTON_LEFT: return LEFT;
            case GLFW.GLFW_MOUSE_BUTTON_RIGHT: return RIGHT;
            case GLFW.GLFW_MOUSE_BUTTON_MIDDLE: return MIDDLE;
            default: return UNKNOWN;
        }
    }
}

