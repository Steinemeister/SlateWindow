package slatewindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InputAction Enum Tests")
class InputActionTest {

    @Test
    @DisplayName("Should map PRESS action correctly")
    void testPressActionMapping() {
        assertEquals(InputAction.PRESS, InputAction.fromGlfw(GLFW.GLFW_PRESS));
    }

    @Test
    @DisplayName("Should map RELEASE action correctly")
    void testReleaseActionMapping() {
        assertEquals(InputAction.RELEASE, InputAction.fromGlfw(GLFW.GLFW_RELEASE));
    }

    @Test
    @DisplayName("Should map REPEAT action correctly")
    void testRepeatActionMapping() {
        assertEquals(InputAction.REPEAT, InputAction.fromGlfw(GLFW.GLFW_REPEAT));
    }

    @Test
    @DisplayName("Should map unknown action codes to UNKNOWN")
    void testUnknownActionMapping() {
        assertEquals(InputAction.UNKNOWN, InputAction.fromGlfw(-9999));
        assertEquals(InputAction.UNKNOWN, InputAction.fromGlfw(9999));
    }
}

