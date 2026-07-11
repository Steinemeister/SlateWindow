package slatewindow;

import org.lwjgl.glfw.GLFW;

public enum InputAction {
    PRESS,
    RELEASE,
    REPEAT,
    UNKNOWN;

    public static InputAction fromGlfw(int id) {
        switch (id) {
            case GLFW.GLFW_PRESS: return PRESS;
            case GLFW.GLFW_RELEASE: return RELEASE;
            case GLFW.GLFW_REPEAT: return REPEAT;
            default: return UNKNOWN;
        }
    }
}

