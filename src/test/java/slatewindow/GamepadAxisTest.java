package slatewindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GamepadAxis Enum Tests")
class GamepadAxisTest {

    @Test
    @DisplayName("Should map LEFT_X axis correctly")
    void testLeftXAxisMapping() {
        assertEquals(GamepadAxis.LEFT_X, GamepadAxis.fromGlfw(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X));
    }

    @Test
    @DisplayName("Should map LEFT_Y axis correctly")
    void testLeftYAxisMapping() {
        assertEquals(GamepadAxis.LEFT_Y, GamepadAxis.fromGlfw(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y));
    }

    @Test
    @DisplayName("Should map RIGHT_X axis correctly")
    void testRightXAxisMapping() {
        assertEquals(GamepadAxis.RIGHT_X, GamepadAxis.fromGlfw(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X));
    }

    @Test
    @DisplayName("Should map RIGHT_Y axis correctly")
    void testRightYAxisMapping() {
        assertEquals(GamepadAxis.RIGHT_Y, GamepadAxis.fromGlfw(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y));
    }

    @Test
    @DisplayName("Should map LEFT_TRIGGER axis correctly")
    void testLeftTriggerMapping() {
        assertEquals(GamepadAxis.LEFT_TRIGGER, GamepadAxis.fromGlfw(GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER));
    }

    @Test
    @DisplayName("Should map RIGHT_TRIGGER axis correctly")
    void testRightTriggerMapping() {
        assertEquals(GamepadAxis.RIGHT_TRIGGER, GamepadAxis.fromGlfw(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER));
    }

    @Test
    @DisplayName("Should map unknown axis codes to UNKNOWN")
    void testUnknownAxisMapping() {
        assertEquals(GamepadAxis.UNKNOWN, GamepadAxis.fromGlfw(-9999));
        assertEquals(GamepadAxis.UNKNOWN, GamepadAxis.fromGlfw(9999));
    }
}

