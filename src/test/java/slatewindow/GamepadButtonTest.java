package slatewindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GamepadButton Enum Tests")
class GamepadButtonTest {

    @Test
    @DisplayName("Should map A button correctly")
    void testAButtonMapping() {
        assertEquals(GamepadButton.A, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_A));
    }

    @Test
    @DisplayName("Should map B button correctly")
    void testBButtonMapping() {
        assertEquals(GamepadButton.B, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_B));
    }

    @Test
    @DisplayName("Should map X button correctly")
    void testXButtonMapping() {
        assertEquals(GamepadButton.X, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_X));
    }

    @Test
    @DisplayName("Should map Y button correctly")
    void testYButtonMapping() {
        assertEquals(GamepadButton.Y, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_Y));
    }

    @Test
    @DisplayName("Should map bumper buttons correctly")
    void testBumperButtonMappings() {
        assertEquals(GamepadButton.LEFT_BUMPER, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER));
        assertEquals(GamepadButton.RIGHT_BUMPER, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER));
    }

    @Test
    @DisplayName("Should map BACK button correctly")
    void testBackButtonMapping() {
        assertEquals(GamepadButton.BACK, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_BACK));
    }

    @Test
    @DisplayName("Should map START button correctly")
    void testStartButtonMapping() {
        assertEquals(GamepadButton.START, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_START));
    }

    @Test
    @DisplayName("Should map GUIDE button correctly")
    void testGuideButtonMapping() {
        assertEquals(GamepadButton.GUIDE, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_GUIDE));
    }

    @Test
    @DisplayName("Should map thumb buttons correctly")
    void testThumbButtonMappings() {
        assertEquals(GamepadButton.LEFT_THUMB, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB));
        assertEquals(GamepadButton.RIGHT_THUMB, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB));
    }

    @Test
    @DisplayName("Should map D-pad buttons correctly")
    void testDpadButtonMappings() {
        assertEquals(GamepadButton.DPAD_UP, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP));
        assertEquals(GamepadButton.DPAD_DOWN, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN));
        assertEquals(GamepadButton.DPAD_LEFT, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT));
        assertEquals(GamepadButton.DPAD_RIGHT, GamepadButton.fromGlfw(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT));
    }

    @Test
    @DisplayName("Should map unknown button codes to UNKNOWN")
    void testUnknownButtonMapping() {
        assertEquals(GamepadButton.UNKNOWN, GamepadButton.fromGlfw(-9999));
        assertEquals(GamepadButton.UNKNOWN, GamepadButton.fromGlfw(9999));
    }
}

