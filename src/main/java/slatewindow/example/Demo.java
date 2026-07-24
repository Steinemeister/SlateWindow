package slatewindow.example;

import org.lwjgl.glfw.GLFW;
import slatewindow.SlateMonitorInfo;
import slatewindow.window.SlateWindow;
import slatewindow.SlateWindowManager;
import slatewindow.input.keyboard.Key;
import slatewindow.input.mouse.CursorMode;

import java.util.List;

// A simple demo showing how to use the SlateWindowManager and SlateWindow classes.
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

        // Build a window with many listeners attached
        SlateWindow window = manager.builder()
                .title("SlateWindow Demo")
                .size(800, 600)
                .onClose(event -> {
                    System.out.println("Close requested for window: " + event.getWindow().getTitle());
                    // Close explicitly to allow manager to clean up
                    event.getWindow().close();
                })
                .onResize((win, width, height) -> System.out.println("Resized: " + width + "x" + height))
                .onFocus((win, focused) -> System.out.println("Focus changed: " + focused))
                .build();

        SlateWindow window2 = manager.builder()
                .title("Second Window")
                .size(400, 300)
                .onClose(event -> {
                    System.out.println("Close requested for second window: " + event.getWindow().getTitle());
                    event.getWindow().close();
                })
                .build();

        // Position window roughly centered on primary monitor
        if (!monitors.isEmpty()) {
            SlateMonitorInfo primary = monitors.get(0);
            int x = Math.max(0, (primary.getWidth() - window.getWidth()) / 2);
            int y = Math.max(0, (primary.getHeight() - window.getHeight()) / 2);
            window.setPosition(x, y);

            x = 0;
            y = Math.max(0, (primary.getHeight() - window2.getHeight()) / 2);

            window2.setPosition(x, y);
        }

        // Make visible and enable a stored vsync flag
        window.setVisible(true);
        window.setVSync(1);

        // Set cursor mode to disabled (hidden)
        window.getMouse().setCursorMode(CursorMode.HIDDEN);

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
                try { Thread.sleep(16); } catch (InterruptedException ignored) {}
                lastTime = now;
            }

            // Update title with FPS once per second using GLFW directly
            long tms = System.currentTimeMillis();
            if (tms - lastFpsUpdate >= 1000) {
                int fps = frames;
                frames = 0;
                lastFpsUpdate = tms;
                String newTitle = String.format("SlateWindow Demo - %d FPS - %dx%d", fps, window.getWidth(), window.getHeight());
                GLFW.glfwSetWindowTitle(window.getHandle(), newTitle);
            }

            if (window.getKeyboard().isKeyDown(Key.ESCAPE)) {
                System.out.println("Escape pressed, requesting window close.");
                window.requestClose();
            }
        }

        manager.terminate();
        System.out.println("Terminated.");
    }
}

