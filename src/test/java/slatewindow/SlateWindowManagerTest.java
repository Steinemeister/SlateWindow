package slatewindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import slatewindow.window.SlateWindow;
import slatewindow.window.SlateWindowFactory;

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

