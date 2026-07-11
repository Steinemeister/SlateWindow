package slatewindow;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.glfw.GLFWJoystickCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import slatewindow.listener.*;

import slatewindow.listener.Listeners.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manager that holds windows and global peripheral listeners.
 */
public class SlateWindowManager {
    private final List<SlateWindow> windows = new CopyOnWriteArrayList<>();
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    // Global listeners
    private final List<GamepadButtonListener> gamepadButtonListeners = new CopyOnWriteArrayList<>();
    private final List<GamepadAxisListener> gamepadAxisListeners = new CopyOnWriteArrayList<>();
    private final List<GamepadConnectionListener> gamepadConnectionListeners = new CopyOnWriteArrayList<>();
    private final List<JoystickListener> joystickListeners = new CopyOnWriteArrayList<>();
    private final List<SpaceMouseListener> spaceMouseListeners = new CopyOnWriteArrayList<>();

    // previous states
    private final boolean[] prevPresent = new boolean[16];
    private final boolean[] prevIsGamepad = new boolean[16];
    private final byte[][] prevButtons = new byte[16][];
    private final float[][] prevAxes = new float[16][];

    private GLFWJoystickCallbackI joystickCallback;

    public void init() {
        if (initialized.compareAndSet(false, true)) {
            if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
            joystickCallback = (jid, event) -> {
                int id = jid - GLFW.GLFW_JOYSTICK_1;
                boolean present = event != GLFW.GLFW_DISCONNECTED;
                for (GamepadConnectionListener l : gamepadConnectionListeners) l.invoke(jid, present);
            };
            GLFW.glfwSetJoystickCallback(joystickCallback);
        }
    }

    public void addGamepadButtonListener(GamepadButtonListener l) { gamepadButtonListeners.add(l); }
    public void addGamepadAxisListener(GamepadAxisListener l) { gamepadAxisListeners.add(l); }
    public void addGamepadConnectionListener(GamepadConnectionListener l) { gamepadConnectionListeners.add(l); }
    public void addJoystickListener(JoystickListener l) { joystickListeners.add(l); }
    public void addSpaceMouseListener(SpaceMouseListener l) { spaceMouseListeners.add(l); }

    public List<SlateMonitorInfo> getAvailableMonitors() {
        PointerBuffer monitors = GLFW.glfwGetMonitors();
        List<SlateMonitorInfo> list = new ArrayList<>();
        if (monitors == null) return list;
        for (int i = 0; i < monitors.limit(); i++) {
            long mon = monitors.get(i);
            String name = GLFW.glfwGetMonitorName(mon);
            GLFWVidMode vm = GLFW.glfwGetVideoMode(mon);
            if (vm != null) list.add(new SlateMonitorInfo(name, vm.width(), vm.height(), vm.refreshRate()));
        }
        return list;
    }

    public <W extends SlateWindow> SlateWindowBuilder<W, ?> builder() {
        return builder((handle, title, w, h) -> (W) new SlateWindow(handle, title, w, h));
    }

    public <W extends SlateWindow> SlateWindowBuilder<W, ?> builder(SlateWindowFactory<W> factory) {
        // wrap factory so created windows are automatically registered
        SlateWindowFactory<W> wrapped = (handle, title, w, h) -> {
            W win = factory.create(handle, title, w, h);
            registerWindow(win);
            return win;
        };
        return new SlateWindowBuilder<>(wrapped);
    }

    public void registerWindow(SlateWindow window) {
        windows.add(window);
    }

    private static final float DEADZONE = 0.05f;

    public void update() {
        // Must be called on main thread
        GLFW.glfwPollEvents();

        for (int i = 0; i < 16; i++) {
            int jid = GLFW.GLFW_JOYSTICK_1 + i;
            boolean present = GLFW.glfwJoystickPresent(jid);

            if (present != prevPresent[i]) {
                prevPresent[i] = present;
                for (GamepadConnectionListener l : gamepadConnectionListeners) l.invoke(jid, present);
            }

            if (!present) continue;

            boolean isGamepad = GLFW.glfwJoystickIsGamepad(jid);
            prevIsGamepad[i] = isGamepad;

            if (isGamepad) {
                GLFWGamepadState state = GLFWGamepadState.create();
                boolean ok = GLFW.glfwGetGamepadState(jid, state);
                if (!ok) continue;

                ByteBuffer buttons = state.buttons();
                FloatBuffer axes = state.axes();

                if (prevButtons[i] == null) prevButtons[i] = new byte[buttons.limit()];
                if (prevAxes[i] == null) prevAxes[i] = new float[axes.limit()];

                for (int b = 0; b < buttons.limit(); b++) {
                    byte val = buttons.get(b);
                    if (val != prevButtons[i][b]) {
                        prevButtons[i][b] = val;
                        GamepadButton gb = GamepadButton.fromGlfw(b);
                        InputAction action = val == GLFW.GLFW_PRESS ? InputAction.PRESS : InputAction.RELEASE;
                        for (GamepadButtonListener l : gamepadButtonListeners) l.invoke(jid, gb, action);
                    }
                }

                for (int a = 0; a < axes.limit(); a++) {
                    float v = axes.get(a);
                    float prev = prevAxes[i][a];
                    if (Math.abs(v - prev) > DEADZONE) {
                        prevAxes[i][a] = v;
                        GamepadAxis ga = GamepadAxis.fromGlfw(a);
                        for (GamepadAxisListener l : gamepadAxisListeners) l.invoke(jid, ga, v);
                    }
                }
            } else {
                FloatBuffer axes = GLFW.glfwGetJoystickAxes(jid);
                ByteBuffer buttons = GLFW.glfwGetJoystickButtons(jid);
                String name = GLFW.glfwGetJoystickName(jid);

                if (axes != null && axes.limit() == 6 && (name != null && (name.contains("SpaceMouse") || name.contains("3Dconnexion")))) {
                    // SpaceMouse handling
                    if (prevAxes[i] == null) prevAxes[i] = new float[6];
                    if (prevButtons[i] == null) prevButtons[i] = new byte[buttons != null ? buttons.limit() : 0];

                    float tx = axes.get(0);
                    float ty = axes.get(1);
                    float tz = axes.get(2);
                    float rx = axes.get(3);
                    float ry = axes.get(4);
                    float rz = axes.get(5);

                    boolean changed = false;
                    float[] prev = prevAxes[i];
                    if (Math.abs(tx - prev[0]) > DEADZONE) { prev[0] = tx; changed = true; }
                    if (Math.abs(ty - prev[1]) > DEADZONE) { prev[1] = ty; changed = true; }
                    if (Math.abs(tz - prev[2]) > DEADZONE) { prev[2] = tz; changed = true; }
                    if (Math.abs(rx - prev[3]) > DEADZONE) { prev[3] = rx; changed = true; }
                    if (Math.abs(ry - prev[4]) > DEADZONE) { prev[4] = ry; changed = true; }
                    if (Math.abs(rz - prev[5]) > DEADZONE) { prev[5] = rz; changed = true; }

                    if (changed) {
                        byte[] btns = new byte[buttons != null ? buttons.limit() : 0];
                        if (buttons != null) for (int b = 0; b < buttons.limit(); b++) btns[b] = buttons.get(b);
                        for (SpaceMouseListener l : spaceMouseListeners) l.invoke(jid, tx, ty, tz, rx, ry, rz, btns);
                    }
                } else {
                    // Generic joystick
                    int na = axes != null ? axes.limit() : 0;
                    int nb = buttons != null ? buttons.limit() : 0;
                    if (prevAxes[i] == null) prevAxes[i] = new float[na];
                    if (prevButtons[i] == null) prevButtons[i] = new byte[nb];

                    boolean changed = false;
                    float[] curAxes = new float[na];
                    byte[] curButtons = new byte[nb];
                    if (axes != null) for (int a = 0; a < na; a++) { curAxes[a] = axes.get(a); if (Math.abs(curAxes[a] - prevAxes[i][a]) > DEADZONE) { prevAxes[i][a] = curAxes[a]; changed = true; } }
                    if (buttons != null) for (int b = 0; b < nb; b++) { curButtons[b] = buttons.get(b); if (curButtons[b] != prevButtons[i][b]) { prevButtons[i][b] = curButtons[b]; changed = true; } }

                    if (changed) {
                        for (JoystickListener l : joystickListeners) l.invoke(jid, curAxes, curButtons);
                    }
                }
            }
        }

        // Remove closed windows
        windows.removeIf(SlateWindow::isClosed);
    }

    public void terminate() {
        for (SlateWindow w : windows) {
            if (!w.isClosed()) w.close();
        }
        windows.clear();
        if (initialized.get()) GLFW.glfwTerminate();
    }
}

