package slatewindow.input.keyboard;

import slatewindow.window.SlateWindow;
import slatewindow.input.SlateInputDevice;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

// Class to manage keyboard input, tracking key states and active modifiers
public class Keyboard extends SlateInputDevice {
    // Map to hold the state of each key
    private final Map<Key, KeyState> keyStates = new EnumMap<>(Key.class);

    // Set to hold the currently active modifier keys
    private final Set<KeyMod> activeMods = EnumSet.noneOf(KeyMod.class);

    // Constructor that initializes the keyboard state and sets up GLFW callbacks
    public Keyboard(SlateWindow window) {
        super(window);
        for (Key key : Key.values()) {
            keyStates.put(key, KeyState.NONE);
        }
        setCallbacks(window.getHandle());
    }

    // Set up GLFW key callback to update key states and modifier keys
    private void setCallbacks(long windowHandle) {
        glfwSetKeyCallback(windowHandle, (handle, keyCode, scancode, action, mods) -> {
            updateModifiers(mods);

            Key key = Key.fromGlfwCode(keyCode);
            if (key == Key.UNKNOWN) {
                System.err.println("Unknown key code: " + keyCode);
                return;
            }

            switch (action) {
                case GLFW_PRESS -> keyStates.put(key, KeyState.PRESSED);
                case GLFW_REPEAT -> keyStates.put(key, KeyState.REPEATING);
                case GLFW_RELEASE -> keyStates.put(key, KeyState.RELEASED);
                default -> {
                    keyStates.put(key, KeyState.NONE);
                    System.err.println("Unknown key action: " + action);
                }
            }
        });
    }

    // Update the active modifier keys based on the GLFW modifier bitmask
    private void updateModifiers(int mods) {
        activeMods.clear();
        for (KeyMod mod : KeyMod.values()) {
            if ((mods & mod.getGlfwBit()) != 0) {
                activeMods.add(mod);
            }
        }
    }

    @Override
    public void update() {
        for (Map.Entry<Key, KeyState> entry : keyStates.entrySet()) {
            Key key = entry.getKey();
            KeyState state = entry.getValue();
            switch (state) {
                case PRESSED -> keyStates.put(key, KeyState.HELD);
                case RELEASED -> keyStates.put(key, KeyState.NONE);
                default -> {
                    // Do nothing for HELD and NONE states
                }
            }
        }
    }

    // Useful methods to query the state of keys and modifiers

    public KeyState getKeyState(Key key) {
        return keyStates.getOrDefault(key, KeyState.NONE);
    }

    public boolean isKeyDown(Key key) {
        KeyState state = getKeyState(key);
        return state == KeyState.PRESSED || state == KeyState.HELD;
    }

    public KeyState getKeyState(Key key, KeyMod... mods) {
        if (!isModsDown(mods)) {
            return KeyState.NONE;
        }
        return getKeyState(key);
    }

    public boolean isModDown(KeyMod mod) {
        return activeMods.contains(mod);
    }

    public boolean isModsDown(KeyMod... mods) {
        for (KeyMod mod : mods) {
            if (!activeMods.contains(mod)) {
                return false;
            }
        }
        return true;
    }

    public Set<KeyMod> getActiveMods() {
        return EnumSet.copyOf(activeMods);
    }
}
