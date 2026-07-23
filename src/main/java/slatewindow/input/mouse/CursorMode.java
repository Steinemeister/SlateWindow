package slatewindow.input.mouse;

import org.lwjgl.glfw.GLFW;

public enum CursorMode {
    NORMAL(GLFW.GLFW_CURSOR_NORMAL),
    HIDDEN(GLFW.GLFW_CURSOR_HIDDEN),
    DISABLED(GLFW.GLFW_CURSOR_DISABLED);

    private final int glfwMode;

    CursorMode(int glfwMode) {
        this.glfwMode = glfwMode;
    }

    public int getGlfwMode() {
        return glfwMode;
    }
}
