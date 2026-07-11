SlateWindow
===========

Eine Grafik-API-agnostische Java-Bibliothek für Multi-Window-Management mit LWJGL3/GLFW.

Wichtig: Die Bibliothek verwendet `GLFW_NO_API` und bindet keinen OpenGL/Vulkan-Kontext. Rendering muss extern vom Nutzer auf eigenen Threads verwaltet werden.

Kurzes Beispiel (Initialisierung & einfacher Update-Loop):

1. Initialisieren:

```java
SlateWindowManager manager = new SlateWindowManager();
manager.init();

SlateWindow window = manager.builder().title("Demo").size(800,600).build();
manager.registerWindow(window);

while (!window.isClosed()) {
    manager.update();
    // user-managed rendering on own threads
}

manager.terminate();
```

Siehe Java-Quellcode für detailliertere API-Informationen.

