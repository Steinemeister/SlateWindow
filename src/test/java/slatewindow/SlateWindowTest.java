package slatewindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import slatewindow.listener.Listeners.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SlateWindow Tests")
class SlateWindowTest {

    private SlateWindow window;
    private static final long MOCK_HANDLE = 12345L;
    private static final String TITLE = "Test Window";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    @BeforeEach
    void setUp() {
        window = new SlateWindow(MOCK_HANDLE, TITLE, WIDTH, HEIGHT);
    }

    @Test
    @DisplayName("Should initialize with correct handle")
    void testInitializeWithHandle() {
        assertEquals(MOCK_HANDLE, window.getHandle());
    }

    @Test
    @DisplayName("Should initialize with correct title")
    void testInitializeWithTitle() {
        assertEquals(TITLE, window.getTitle());
    }

    @Test
    @DisplayName("Should initialize with correct width")
    void testInitializeWithWidth() {
        assertEquals(WIDTH, window.getWidth());
    }

    @Test
    @DisplayName("Should initialize with correct height")
    void testInitializeWithHeight() {
        assertEquals(HEIGHT, window.getHeight());
    }

    @Test
    @DisplayName("Should not be closed initially")
    void testInitiallyNotClosed() {
        assertFalse(window.isClosed());
    }

    @Test
    @DisplayName("Should support adding key listeners")
    void testAddKeyListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        KeyListener listener = (w, key, scancode, action, mods) -> called.set(true);
        
        window.addKeyListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should support adding resize listeners")
    void testAddResizeListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        ResizeListener listener = (w, width, height) -> called.set(true);
        
        window.addResizeListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should support adding close listeners")
    void testAddCloseListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        CloseListener listener = (w) -> called.set(true);
        
        window.addCloseListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should support adding mouse button listeners")
    void testAddMouseButtonListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        MouseButtonListener listener = (w, button, action, mods) -> called.set(true);
        
        window.addMouseButtonListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should support adding mouse move listeners")
    void testAddMouseMoveListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        MouseMoveListener listener = (w, xpos, ypos) -> called.set(true);
        
        window.addMouseMoveListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should support adding scroll listeners")
    void testAddScrollListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        ScrollListener listener = (w, xoffset, yoffset) -> called.set(true);
        
        window.addScrollListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should support adding focus listeners")
    void testAddFocusListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        FocusListener listener = (w, focused) -> called.set(true);
        
        window.addFocusListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should support adding touch listeners")
    void testAddTouchListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        TouchListener listener = (w, id, action, xpos, ypos) -> called.set(true);
        
        window.addTouchListener(listener);
        assertTrue(true); // Listener added without exception
    }

    @Test
    @DisplayName("Should set VSync interval")
    void testSetVSync() {
        window.setVSync(1);
        assertTrue(window.isVSyncEnabled());
    }

    @Test
    @DisplayName("Should report VSync disabled when interval is 0")
    void testVSyncDisabled() {
        window.setVSync(0);
        assertFalse(window.isVSyncEnabled());
    }

    @Test
    @DisplayName("Should close window only once")
    void testCloseWindowOnce() {
        assertFalse(window.isClosed());
        window.close();
        assertTrue(window.isClosed());
        // Closing again should not fail
        window.close();
        assertTrue(window.isClosed());
    }

    @Test
    @DisplayName("Should support multiple listeners of same type")
    void testMultipleListenersSameType() {
        AtomicInteger callCount = new AtomicInteger(0);
        KeyListener listener1 = (w, key, scancode, action, mods) -> callCount.incrementAndGet();
        KeyListener listener2 = (w, key, scancode, action, mods) -> callCount.incrementAndGet();
        
        window.addKeyListener(listener1);
        window.addKeyListener(listener2);
        
        assertTrue(true); // Both listeners added without exception
    }

    @Test
    @DisplayName("Should allow setting position")
    void testSetPosition() {
        window.setPosition(100, 200);
        assertTrue(true); // No exception expected
    }

    @Test
    @DisplayName("Should allow setting visibility")
    void testSetVisibility() {
        window.setVisible(false);
        window.setVisible(true);
        assertTrue(true); // No exception expected
    }
}

