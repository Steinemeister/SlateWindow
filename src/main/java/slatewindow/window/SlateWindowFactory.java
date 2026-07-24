package slatewindow.window;

/** Factory interface to create SlateWindow instances from a GLFW handle. */
public interface SlateWindowFactory<T extends SlateWindow> {
    T create(long handle, String title, int width, int height);
}

