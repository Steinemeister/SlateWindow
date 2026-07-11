package slatewindow.example;

import org.lwjgl.glfw.GLFW;
import slatewindow.SlateMonitorInfo;
import slatewindow.SlateWindow;
import slatewindow.SlateWindowManager;

import java.util.List;

/**
 * Ausführlichere Demo, die viele Teile der SlateWindow-API verwendet.
 *
 * Funktionen:
 * - Ausgabe verfügbarer Monitore
 * - Erzeugung eines Fensters mit zahlreichen Event-Handlern
 * - Zeigt Maus-, Tastatur-, Scroll- und Touch-Ereignisse an
 * - Gamepad/Joystick-Events (global) anzeigen
 * - FPS-Zählung und Aktualisierung des Fenstertitels (über GLFW)
 */
public class Demo {
    public static void main(String[] args) {
        SlateWindowManager manager = new SlateWindowManager();
        manager.init();

        // List available monitors
        List<SlateMonitorInfo> monitors = manager.getAvailableMonitors();
        System.out.println("Detected monitors:");
        for (int i = 0; i < monitors.size(); i++) {
            SlateMonitorInfo mi = monitors.get(i);
            System.out.printf("  %d: %s - %dx%d @ %dhz\n", i, mi.getName(), mi.getWidth(), mi.getHeight(), mi.getRefreshRate());
        }

        // Register global gamepad listeners
        manager.addGamepadConnectionListener((jid, connected) -> System.out.println("Gamepad " + jid + " connected=" + connected));
        manager.addGamepadButtonListener((jid, button, action) -> System.out.println("Gamepad " + jid + " button=" + button + " action=" + action));
        manager.addGamepadAxisListener((jid, axis, value) -> System.out.println("Gamepad " + jid + " axis=" + axis + " value=" + value));

        // Build a window with many listeners attached
        SlateWindow window = manager.builder()
                .title("SlateWindow Verbose Demo")
                .size(800, 600)
                .onClose(w -> {
                    System.out.println("Close requested for window: " + w.getTitle());
                    // Close explicitly to allow manager to clean up
                    w.close();
                })
                .onKey((w, key, scancode, action, mods) -> {
                    System.out.println(String.format("Key event: key=%d scancode=%d action=%d mods=%d", key, scancode, action, mods));
                    // Close on ESC
                    if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                        System.out.println("Escape pressed - closing window.");
                        w.close();
                    }
                })
                .onResize((win, width, height) -> System.out.println("Resized: " + width + "x" + height))
                .onMouseMove((win, xpos, ypos) -> System.out.println(String.format("Mouse move: x=%.1f y=%.1f", xpos, ypos)))
                .onMouseButton((win, button, action, mods) -> System.out.println(String.format("Mouse button: button=%d action=%d mods=%d", button, action, mods)))
                .onScroll((win, xoff, yoff) -> System.out.println(String.format("Scroll: xoff=%.2f yoff=%.2f", xoff, yoff)))
                .onFocus((win, focused) -> System.out.println("Focus changed: " + focused))
                .onTouch((win, id, action, xpos, ypos) -> System.out.println(String.format("Touch: id=%d action=%d x=%.1f y=%.1f", id, action, xpos, ypos)))
                .build();

        // Position window roughly centered on primary monitor
        if (!monitors.isEmpty()) {
            SlateMonitorInfo primary = monitors.get(0);
            int x = Math.max(0, (primary.getWidth() - window.getWidth()) / 2);
            int y = Math.max(0, (primary.getHeight() - window.getHeight()) / 2);
            window.setPosition(x, y);
        }

        // Make visible and enable a stored vsync flag
        window.setVisible(true);
        window.setVSync(1);

        SlateWindow window1 = manager.builder()
                .title("Second Window")
                .size(400, 300)
                .onClose(w -> {
                    System.out.println("Close requested for second window: " + w.getTitle());
                    w.close();
                })
                .build();
        window1.setVisible(true);

        System.out.println("Entering main loop. Close the window to exit.");

        // Simple FPS counter
        long lastTime = System.nanoTime();
        int frames = 0;
        long lastFpsUpdate = System.currentTimeMillis();

        while (!window.isClosed()) {
            manager.update();

            // Here you would render using your chosen API. This demo only simulates a loop.

            frames++;
            long now = System.nanoTime();
            if (now - lastTime >= 16_000_000) {
                // Sleep a bit to avoid pegging CPU in this simple demo
                try { Thread.sleep(1); } catch (InterruptedException ignored) {}
                lastTime = now;
            }

            // Update title with FPS once per second using GLFW directly
            long tms = System.currentTimeMillis();
            if (tms - lastFpsUpdate >= 1000) {
                int fps = frames;
                frames = 0;
                lastFpsUpdate = tms;
                String newTitle = String.format("SlateWindow Verbose Demo - %d FPS - %dx%d", fps, window.getWidth(), window.getHeight());
                GLFW.glfwSetWindowTitle(window.getHandle(), newTitle);
            }
        }

        manager.terminate();
        System.out.println("Terminated.");
    }
}

