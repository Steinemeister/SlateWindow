package slatewindow.input.keyboard;

import static org.lwjgl.glfw.GLFW.*;

// Enum to represent modifier keys, mapping to GLFW modifier bitmasks
public enum KeyMod {
    // Modifier keys
    SHIFT(GLFW_MOD_SHIFT),
    CONTROL(GLFW_MOD_CONTROL),
    ALT(GLFW_MOD_ALT),
    SUPER(GLFW_MOD_SUPER);

    private final int glfwBit;

    KeyMod(int glfwBit) {
        this.glfwBit = glfwBit;
    }

    public int getGlfwBit() {
        return glfwBit;
    }
}
