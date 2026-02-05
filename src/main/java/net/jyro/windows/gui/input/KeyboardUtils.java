package net.jyro.windows.gui.input;

import com.sun.jna.platform.win32.User32;

public class KeyboardUtils {

    public static final int KEY_A = 0x41;
    public static final int KEY_B = 0x42;
    public static final int KEY_C = 0x43;
    public static final int KEY_D = 0x44;
    public static final int KEY_E = 0x45;
    public static final int KEY_F = 0x46;
    public static final int KEY_G = 0x47;
    public static final int KEY_H = 0x48;
    public static final int KEY_I = 0x49;
    public static final int KEY_J = 0x4A;
    public static final int KEY_K = 0x4B;
    public static final int KEY_L = 0x4C;
    public static final int KEY_M = 0x4D;
    public static final int KEY_N = 0x4E;
    public static final int KEY_O = 0x4F;
    public static final int KEY_P = 0x50;
    public static final int KEY_Q = 0x51;
    public static final int KEY_R = 0x52;
    public static final int KEY_S = 0x53;
    public static final int KEY_T = 0x54;
    public static final int KEY_U = 0x55;
    public static final int KEY_V = 0x56;
    public static final int KEY_W = 0x57;
    public static final int KEY_X = 0x58;
    public static final int KEY_Y = 0x59;
    public static final int KEY_Z = 0x5A;

    public static final int KEY_0 = 0x30;
    public static final int KEY_1 = 0x31;
    public static final int KEY_2 = 0x32;
    public static final int KEY_3 = 0x33;
    public static final int KEY_4 = 0x34;
    public static final int KEY_5 = 0x35;
    public static final int KEY_6 = 0x36;
    public static final int KEY_7 = 0x37;
    public static final int KEY_8 = 0x38;
    public static final int KEY_9 = 0x39;

    // Function keys
    public static final int KEY_F1  = 0x70;
    public static final int KEY_F2  = 0x71;
    public static final int KEY_F3  = 0x72;
    public static final int KEY_F4  = 0x73;
    public static final int KEY_F5  = 0x74;
    public static final int KEY_F6  = 0x75;
    public static final int KEY_F7  = 0x76;
    public static final int KEY_F8  = 0x77;
    public static final int KEY_F9  = 0x78;
    public static final int KEY_F10 = 0x79;
    public static final int KEY_F11 = 0x7A;
    public static final int KEY_F12 = 0x7B;

    public static final int KEY_LEFT  = 0x25;
    public static final int KEY_UP    = 0x26;
    public static final int KEY_RIGHT = 0x27;
    public static final int KEY_DOWN  = 0x28;

    public static final int KEY_BACKSPACE = 0x08;
    public static final int KEY_TAB       = 0x09;
    public static final int KEY_ENTER     = 0x0D;
    public static final int KEY_SHIFT     = 0x10;
    public static final int KEY_CTRL      = 0x11;
    public static final int KEY_ALT       = 0x12;
    public static final int KEY_PAUSE     = 0x13;
    public static final int KEY_CAPSLOCK  = 0x14;
    public static final int KEY_ESCAPE    = 0x1B;
    public static final int KEY_SPACE     = 0x20;
    public static final int KEY_PAGEUP    = 0x21;
    public static final int KEY_PAGEDOWN  = 0x22;
    public static final int KEY_END       = 0x23;
    public static final int KEY_HOME      = 0x24;
    public static final int KEY_INSERT    = 0x2D;
    public static final int KEY_DELETE    = 0x2E;

    // Numpad
    public static final int KEY_NUMPAD0 = 0x60;
    public static final int KEY_NUMPAD1 = 0x61;
    public static final int KEY_NUMPAD2 = 0x62;
    public static final int KEY_NUMPAD3 = 0x63;
    public static final int KEY_NUMPAD4 = 0x64;
    public static final int KEY_NUMPAD5 = 0x65;
    public static final int KEY_NUMPAD6 = 0x66;
    public static final int KEY_NUMPAD7 = 0x67;
    public static final int KEY_NUMPAD8 = 0x68;
    public static final int KEY_NUMPAD9 = 0x69;
    public static final int KEY_NUMPAD_MULTIPLY = 0x6A;
    public static final int KEY_NUMPAD_ADD      = 0x6B;
    public static final int KEY_NUMPAD_SEPARATOR= 0x6C;
    public static final int KEY_NUMPAD_SUBTRACT = 0x6D;
    public static final int KEY_NUMPAD_DECIMAL  = 0x6E;
    public static final int KEY_NUMPAD_DIVIDE   = 0x6F;


    public static final int KEY_MINUS      = 0xBD; // '-' key
    public static final int KEY_EQUALS     = 0xBB; // '=' key
    public static final int KEY_LBRACKET   = 0xDB; // '['
    public static final int KEY_RBRACKET   = 0xDD; // ']'
    public static final int KEY_BACKSLASH  = 0xDC; // '\'
    public static final int KEY_SEMICOLON  = 0xBA; // ';'
    public static final int KEY_APOSTROPHE = 0xDE; // '''
    public static final int KEY_COMMA      = 0xBC; // ','
    public static final int KEY_PERIOD     = 0xBE; // '.'
    public static final int KEY_SLASH      = 0xBF; // '/'

    public static final int KEY_NUMLOCK   = 0x90;
    public static final int KEY_SCROLL    = 0x91;

    public static final int KEY_LWIN      = 0x5B;
    public static final int KEY_RWIN      = 0x5C;
    public static final int KEY_APPS      = 0x5D;

    public static final int KEY_PRINTSCREEN = 0x2C;
    public static final int KEY_PAUSEBREAK   = 0x13;

    public static boolean isKeyDown(int vkCode) {
        short state = User32.INSTANCE.GetAsyncKeyState(vkCode);
        return state < 0;
    }
}
