package slatewindow.input.mouse;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public enum MouseButton {
    ONE(GLFW_MOUSE_BUTTON_1),
    TWO(GLFW_MOUSE_BUTTON_2),
    THREE(GLFW_MOUSE_BUTTON_3),
    FOUR(GLFW_MOUSE_BUTTON_4),
    FIVE(GLFW_MOUSE_BUTTON_5),
    SIX(GLFW_MOUSE_BUTTON_6),
    SEVEN(GLFW_MOUSE_BUTTON_7),
    EIGHT(GLFW_MOUSE_BUTTON_8),
    LEFT(GLFW_MOUSE_BUTTON_LEFT),
    RIGHT(GLFW_MOUSE_BUTTON_RIGHT),
    MIDDLE(GLFW_MOUSE_BUTTON_MIDDLE),
    UNKNOWN(-1);

    private final int glfwCode;
    private static final Map<Integer, MouseButton> BY_GLFW_CODE = new HashMap<>();

    static {
        for (MouseButton button : values()) {
            BY_GLFW_CODE.put(button.getGlfwCode(), button);
        }
    }

    MouseButton(int glfwCode) {
        this.glfwCode = glfwCode;
    }

    public int getGlfwCode() {
        return glfwCode;
    }

    public static MouseButton fromGlfwCode(int glfwCode) {
        return BY_GLFW_CODE.getOrDefault(glfwCode, UNKNOWN);
    }
}
