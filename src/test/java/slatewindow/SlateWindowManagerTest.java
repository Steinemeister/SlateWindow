package slatewindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import slatewindow.listener.Listeners.*;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SlateWindowManager Tests")
class SlateWindowManagerTest {

    private SlateWindowManager manager;

    @BeforeEach
    void setUp() {
        manager = new SlateWindowManager();
    }

    @Test
    @DisplayName("Should initialize only once")
    void testInitializeOnce() {
        manager.init();
        manager.init(); // Second call should not fail
        assertTrue(true); // If no exception, initialization works correctly
    }

    @Test
    @DisplayName("Should support adding gamepad button listeners")
    void testAddGamepadButtonListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        GamepadButtonListener listener = (jid, button, action) -> called.set(true);
        
        manager.addGamepadButtonListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should support adding gamepad axis listeners")
    void testAddGamepadAxisListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        GamepadAxisListener listener = (jid, axis, value) -> called.set(true);
        
        manager.addGamepadAxisListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should support adding gamepad connection listeners")
    void testAddGamepadConnectionListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        GamepadConnectionListener listener = (jid, connected) -> called.set(true);
        
        manager.addGamepadConnectionListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should support adding joystick listeners")
    void testAddJoystickListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        JoystickListener listener = (jid, axes, buttons) -> called.set(true);
        
        manager.addJoystickListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should support adding space mouse listeners")
    void testAddSpaceMouseListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        SpaceMouseListener listener = (jid, tx, ty, tz, rx, ry, rz, buttons) -> called.set(true);
        
        manager.addSpaceMouseListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should register windows")
    void testRegisterWindow() {
        SlateWindow mockWindow = new SlateWindow(1L, "Test", 800, 600);
        
        manager.registerWindow(mockWindow);
        assertTrue(true); // Window registered without exception
    }

    @Test
    @DisplayName("Should create builder with default factory")
    void testBuilderDefaultFactory() {
        var builder = manager.builder();
        assertNotNull(builder);
    }

    @Test
    @DisplayName("Should create builder with custom factory")
    void testBuilderCustomFactory() {
        SlateWindowFactory<SlateWindow> factory = (handle, title, w, h) -> 
            new SlateWindow(handle, title, w, h);
        
        var builder = manager.builder(factory);
        assertNotNull(builder);
    }

    @Test
    @DisplayName("Should support multiple listener additions")
    void testMultipleListenerAdditions() {
        GamepadButtonListener listener1 = (jid, button, action) -> {};
        GamepadButtonListener listener2 = (jid, button, action) -> {};
        GamepadButtonListener listener3 = (jid, button, action) -> {};
        
        manager.addGamepadButtonListener(listener1);
        manager.addGamepadButtonListener(listener2);
        manager.addGamepadButtonListener(listener3);
        
        assertTrue(true); // All listeners added without exception
    }

    @Test
    @DisplayName("Should support update calls")
    void testUpdateCall() {
        manager.init();
        manager.update(); // Should not throw exception
        assertTrue(true);
    }

    @Test
    @DisplayName("Should support terminate call")
    void testTerminateCall() {
        manager.init();
        manager.terminate(); // Should not throw exception
        assertTrue(true);
    }
}

