package slatewindow.example;

import slatewindow.SlateWindow;
import slatewindow.SlateWindowManager;

public class Main {
    public static void main(String[] args) {
        SlateWindowManager manager = new SlateWindowManager();
        manager.init();

        SlateWindow window = manager.builder()
                .title("SlateWindow Demo")
                .size(640, 480)
                .onClose(w -> {
                    System.out.println("Close requested for window: " + w.getTitle());
                    w.close();
                })
                .build();

        manager.registerWindow(window);

        System.out.println("Entering main loop. Close the window to exit.");
        while (!window.isClosed()) {
            manager.update();
            try { Thread.sleep(16); } catch (InterruptedException ignored) {}
        }

        manager.terminate();
        System.out.println("Terminated.");
    }
}

