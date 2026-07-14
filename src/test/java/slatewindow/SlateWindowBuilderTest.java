package slatewindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import slatewindow.listener.Listeners.*;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SlateWindowBuilder Tests")
class SlateWindowBuilderTest {

    private SlateWindowBuilder<SlateWindow, ?> builder;
    private SlateWindowFactory<SlateWindow> testFactory;

    @BeforeEach
    void setUp() {
        // Create a test factory that creates windows without GLFW
        testFactory = (handle, title, w, h) -> new SlateWindow(handle, title, w, h);
        builder = new SlateWindowBuilder<>(testFactory);
    }

    @Test
    @DisplayName("Should have default size 800x600")
    void testDefaultSize() {
        assertEquals(800, builder.width);
        assertEquals(600, builder.height);
    }

    @Test
    @DisplayName("Should have default title 'SlateWindow'")
    void testDefaultTitle() {
        assertEquals("SlateWindow", builder.title);
    }

    @Test
    @DisplayName("Should have default decorated true")
    void testDefaultDecorated() {
        assertTrue(builder.decorated);
    }

    @Test
    @DisplayName("Should have default maximized false")
    void testDefaultMaximized() {
        assertFalse(builder.maximized);
    }

    @Test
    @DisplayName("Should set custom size")
    void testSetSize() {
        SlateWindowBuilder<SlateWindow, ?> result = builder.size(1920, 1080);
        
        assertSame(builder, result); // Fluent API
        assertEquals(1920, builder.width);
        assertEquals(1080, builder.height);
    }

    @Test
    @DisplayName("Should set custom title")
    void testSetTitle() {
        SlateWindowBuilder<SlateWindow, ?> result = builder.title("Custom Title");
        
        assertSame(builder, result); // Fluent API
        assertEquals("Custom Title", builder.title);
    }

    @Test
    @DisplayName("Should set decorated flag")
    void testSetDecorated() {
        SlateWindowBuilder<SlateWindow, ?> result = builder.decorated(false);
        
        assertSame(builder, result); // Fluent API
        assertFalse(builder.decorated);
    }

    @Test
    @DisplayName("Should set maximized flag")
    void testSetMaximized() {
        SlateWindowBuilder<SlateWindow, ?> result = builder.maximized(true);
        
        assertSame(builder, result); // Fluent API
        assertTrue(builder.maximized);
    }

    @Test
    @DisplayName("Should add resize listener")
    void testAddResizeListener() {
        ResizeListener listener = (w, width, height) -> {};
        SlateWindowBuilder<SlateWindow, ?> result = builder.onResize(listener);
        
        assertSame(builder, result); // Fluent API
        assertTrue(builder.resizeListeners.contains(listener));
    }

    @Test
    @DisplayName("Should add close listener")
    void testAddCloseListener() {
        CloseListener listener = (w) -> {};
        SlateWindowBuilder<SlateWindow, ?> result = builder.onClose(listener);
        
        assertSame(builder, result); // Fluent API
        assertTrue(builder.closeListeners.contains(listener));
    }

    @Test
    @DisplayName("Should add focus listener")
    void testAddFocusListener() {
        FocusListener listener = (w, focused) -> {};
        SlateWindowBuilder<SlateWindow, ?> result = builder.onFocus(listener);
        
        assertSame(builder, result); // Fluent API
        assertTrue(builder.focusListeners.contains(listener));
    }
}

