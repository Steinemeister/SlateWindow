package slatewindow;

import org.lwjgl.glfw.GLFW;

/**
 * Enum for keyboard keys with helper to map from GLFW key codes.
 */
public enum Key {
    UNKNOWN,
    ESCAPE,
    SPACE,
    ENTER,
    TAB,
    BACKSPACE,
    SHIFT,
    CONTROL,
    ALT,
    CAPS_LOCK,
    LEFT,
    RIGHT,
    UP,
    DOWN,
    A, B, C, D, E, F, G, H, I, J, K, L, M,
    N, O, P, Q, R, S, T, U, V, W, X, Y, Z,
    F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12,
    HOME, END, PAGE_UP, PAGE_DOWN, INSERT, DELETE,
    NUMPAD_0, NUMPAD_1, NUMPAD_2, NUMPAD_3, NUMPAD_4, NUMPAD_5, NUMPAD_6, NUMPAD_7, NUMPAD_8, NUMPAD_9;

    public static Key fromGlfw(int id) {
        switch (id) {
            case GLFW.GLFW_KEY_ESCAPE: return ESCAPE;
            case GLFW.GLFW_KEY_SPACE: return SPACE;
            case GLFW.GLFW_KEY_ENTER: return ENTER;
            case GLFW.GLFW_KEY_TAB: return TAB;
            case GLFW.GLFW_KEY_BACKSPACE: return BACKSPACE;
            case GLFW.GLFW_KEY_LEFT_SHIFT: case GLFW.GLFW_KEY_RIGHT_SHIFT: return SHIFT;
            case GLFW.GLFW_KEY_LEFT_CONTROL: case GLFW.GLFW_KEY_RIGHT_CONTROL: return CONTROL;
            case GLFW.GLFW_KEY_LEFT_ALT: case GLFW.GLFW_KEY_RIGHT_ALT: return ALT;
            case GLFW.GLFW_KEY_CAPS_LOCK: return CAPS_LOCK;
            case GLFW.GLFW_KEY_LEFT: return LEFT;
            case GLFW.GLFW_KEY_RIGHT: return RIGHT;
            case GLFW.GLFW_KEY_UP: return UP;
            case GLFW.GLFW_KEY_DOWN: return DOWN;
            case GLFW.GLFW_KEY_A: return A;
            case GLFW.GLFW_KEY_B: return B;
            case GLFW.GLFW_KEY_C: return C;
            case GLFW.GLFW_KEY_D: return D;
            case GLFW.GLFW_KEY_E: return E;
            case GLFW.GLFW_KEY_F: return F;
            case GLFW.GLFW_KEY_G: return G;
            case GLFW.GLFW_KEY_H: return H;
            case GLFW.GLFW_KEY_I: return I;
            case GLFW.GLFW_KEY_J: return J;
            case GLFW.GLFW_KEY_K: return K;
            case GLFW.GLFW_KEY_L: return L;
            case GLFW.GLFW_KEY_M: return M;
            case GLFW.GLFW_KEY_N: return N;
            case GLFW.GLFW_KEY_O: return O;
            case GLFW.GLFW_KEY_P: return P;
            case GLFW.GLFW_KEY_Q: return Q;
            case GLFW.GLFW_KEY_R: return R;
            case GLFW.GLFW_KEY_S: return S;
            case GLFW.GLFW_KEY_T: return T;
            case GLFW.GLFW_KEY_U: return U;
            case GLFW.GLFW_KEY_V: return V;
            case GLFW.GLFW_KEY_W: return W;
            case GLFW.GLFW_KEY_X: return X;
            case GLFW.GLFW_KEY_Y: return Y;
            case GLFW.GLFW_KEY_Z: return Z;
            case GLFW.GLFW_KEY_F1: return F1;
            case GLFW.GLFW_KEY_F2: return F2;
            case GLFW.GLFW_KEY_F3: return F3;
            case GLFW.GLFW_KEY_F4: return F4;
            case GLFW.GLFW_KEY_F5: return F5;
            case GLFW.GLFW_KEY_F6: return F6;
            case GLFW.GLFW_KEY_F7: return F7;
            case GLFW.GLFW_KEY_F8: return F8;
            case GLFW.GLFW_KEY_F9: return F9;
            case GLFW.GLFW_KEY_F10: return F10;
            case GLFW.GLFW_KEY_F11: return F11;
            case GLFW.GLFW_KEY_F12: return F12;
            case GLFW.GLFW_KEY_HOME: return HOME;
            case GLFW.GLFW_KEY_END: return END;
            case GLFW.GLFW_KEY_PAGE_UP: return PAGE_UP;
            case GLFW.GLFW_KEY_PAGE_DOWN: return PAGE_DOWN;
            case GLFW.GLFW_KEY_INSERT: return INSERT;
            case GLFW.GLFW_KEY_DELETE: return DELETE;
            case GLFW.GLFW_KEY_KP_0: return NUMPAD_0;
            case GLFW.GLFW_KEY_KP_1: return NUMPAD_1;
            case GLFW.GLFW_KEY_KP_2: return NUMPAD_2;
            case GLFW.GLFW_KEY_KP_3: return NUMPAD_3;
            case GLFW.GLFW_KEY_KP_4: return NUMPAD_4;
            case GLFW.GLFW_KEY_KP_5: return NUMPAD_5;
            case GLFW.GLFW_KEY_KP_6: return NUMPAD_6;
            case GLFW.GLFW_KEY_KP_7: return NUMPAD_7;
            case GLFW.GLFW_KEY_KP_8: return NUMPAD_8;
            case GLFW.GLFW_KEY_KP_9: return NUMPAD_9;
            default: return UNKNOWN;
        }
    }
}

