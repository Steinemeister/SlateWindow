package slatewindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Key Enum Tests")
class KeyTest {

    @Test
    @DisplayName("Should map ESCAPE key correctly")
    void testEscapeKeyMapping() {
        assertEquals(Key.ESCAPE, Key.fromGlfw(GLFW.GLFW_KEY_ESCAPE));
    }

    @Test
    @DisplayName("Should map SPACE key correctly")
    void testSpaceKeyMapping() {
        assertEquals(Key.SPACE, Key.fromGlfw(GLFW.GLFW_KEY_SPACE));
    }

    @Test
    @DisplayName("Should map ENTER key correctly")
    void testEnterKeyMapping() {
        assertEquals(Key.ENTER, Key.fromGlfw(GLFW.GLFW_KEY_ENTER));
    }

    @Test
    @DisplayName("Should map TAB key correctly")
    void testTabKeyMapping() {
        assertEquals(Key.TAB, Key.fromGlfw(GLFW.GLFW_KEY_TAB));
    }

    @Test
    @DisplayName("Should map BACKSPACE key correctly")
    void testBackspaceKeyMapping() {
        assertEquals(Key.BACKSPACE, Key.fromGlfw(GLFW.GLFW_KEY_BACKSPACE));
    }

    @Test
    @DisplayName("Should map LEFT_SHIFT to SHIFT")
    void testLeftShiftMapping() {
        assertEquals(Key.SHIFT, Key.fromGlfw(GLFW.GLFW_KEY_LEFT_SHIFT));
    }

    @Test
    @DisplayName("Should map RIGHT_SHIFT to SHIFT")
    void testRightShiftMapping() {
        assertEquals(Key.SHIFT, Key.fromGlfw(GLFW.GLFW_KEY_RIGHT_SHIFT));
    }

    @Test
    @DisplayName("Should map LEFT_CONTROL to CONTROL")
    void testLeftControlMapping() {
        assertEquals(Key.CONTROL, Key.fromGlfw(GLFW.GLFW_KEY_LEFT_CONTROL));
    }

    @Test
    @DisplayName("Should map RIGHT_CONTROL to CONTROL")
    void testRightControlMapping() {
        assertEquals(Key.CONTROL, Key.fromGlfw(GLFW.GLFW_KEY_RIGHT_CONTROL));
    }

    @Test
    @DisplayName("Should map LEFT_ALT to ALT")
    void testLeftAltMapping() {
        assertEquals(Key.ALT, Key.fromGlfw(GLFW.GLFW_KEY_LEFT_ALT));
    }

    @Test
    @DisplayName("Should map RIGHT_ALT to ALT")
    void testRightAltMapping() {
        assertEquals(Key.ALT, Key.fromGlfw(GLFW.GLFW_KEY_RIGHT_ALT));
    }

    @Test
    @DisplayName("Should map CAPS_LOCK key correctly")
    void testCapsLockMapping() {
        assertEquals(Key.CAPS_LOCK, Key.fromGlfw(GLFW.GLFW_KEY_CAPS_LOCK));
    }

    @Test
    @DisplayName("Should map arrow keys correctly")
    void testArrowKeyMappings() {
        assertEquals(Key.LEFT, Key.fromGlfw(GLFW.GLFW_KEY_LEFT));
        assertEquals(Key.RIGHT, Key.fromGlfw(GLFW.GLFW_KEY_RIGHT));
        assertEquals(Key.UP, Key.fromGlfw(GLFW.GLFW_KEY_UP));
        assertEquals(Key.DOWN, Key.fromGlfw(GLFW.GLFW_KEY_DOWN));
    }

    @Test
    @DisplayName("Should map letter keys correctly")
    void testLetterKeyMappings() {
        assertEquals(Key.A, Key.fromGlfw(GLFW.GLFW_KEY_A));
        assertEquals(Key.B, Key.fromGlfw(GLFW.GLFW_KEY_B));
        assertEquals(Key.Z, Key.fromGlfw(GLFW.GLFW_KEY_Z));
    }

    @Test
    @DisplayName("Should map function keys correctly")
    void testFunctionKeyMappings() {
        assertEquals(Key.F1, Key.fromGlfw(GLFW.GLFW_KEY_F1));
        assertEquals(Key.F6, Key.fromGlfw(GLFW.GLFW_KEY_F6));
        assertEquals(Key.F12, Key.fromGlfw(GLFW.GLFW_KEY_F12));
    }

    @Test
    @DisplayName("Should map HOME, END, PAGE_UP, PAGE_DOWN correctly")
    void testNavigationKeyMappings() {
        assertEquals(Key.HOME, Key.fromGlfw(GLFW.GLFW_KEY_HOME));
        assertEquals(Key.END, Key.fromGlfw(GLFW.GLFW_KEY_END));
        assertEquals(Key.PAGE_UP, Key.fromGlfw(GLFW.GLFW_KEY_PAGE_UP));
        assertEquals(Key.PAGE_DOWN, Key.fromGlfw(GLFW.GLFW_KEY_PAGE_DOWN));
    }

    @Test
    @DisplayName("Should map INSERT, DELETE keys correctly")
    void testEditKeyMappings() {
        assertEquals(Key.INSERT, Key.fromGlfw(GLFW.GLFW_KEY_INSERT));
        assertEquals(Key.DELETE, Key.fromGlfw(GLFW.GLFW_KEY_DELETE));
    }

    @Test
    @DisplayName("Should map numpad keys correctly")
    void testNumpadKeyMappings() {
        assertEquals(Key.NUMPAD_0, Key.fromGlfw(GLFW.GLFW_KEY_KP_0));
        assertEquals(Key.NUMPAD_5, Key.fromGlfw(GLFW.GLFW_KEY_KP_5));
        assertEquals(Key.NUMPAD_9, Key.fromGlfw(GLFW.GLFW_KEY_KP_9));
    }

    @Test
    @DisplayName("Should map unknown key codes to UNKNOWN")
    void testUnknownKeyMapping() {
        assertEquals(Key.UNKNOWN, Key.fromGlfw(-9999));
        assertEquals(Key.UNKNOWN, Key.fromGlfw(0));
    }
}

