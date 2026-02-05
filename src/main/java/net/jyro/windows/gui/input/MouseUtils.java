package net.jyro.windows.gui.input;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import net.jyro.windows.gui.backends.User32Ex;

public class MouseUtils {
    public static final int LEFT_BUTTON = 0x01;
    public static final int RIGHT_BUTTON = 0x02;
    public static final int MIDDLE_BUTTON = 0x04;
    public static final int KEY_DOWN_MASK = 0x8000;

    public static boolean isMouseButtonPressed(int mouseVkCode) {
        short state = User32.INSTANCE.GetAsyncKeyState(mouseVkCode);
        return (state & KEY_DOWN_MASK) != 0;
    }
    public static float[] getCurrentMousePositionInApplication(WinDef.HWND hwnd) {
        WinDef.POINT pt = new WinDef.POINT();

        User32.INSTANCE.GetCursorPos(pt);
        User32Ex.INSTANCE.ScreenToClient(hwnd,pt);

        return new float[] { (float) pt.x, (float) pt.y };
    }
    public static void ApplyCurrentMousePositionInApplication(WinDef.HWND hwnd,float[] x, float[]y) {
        WinDef.POINT pt = new WinDef.POINT();

        User32.INSTANCE.GetCursorPos(pt);
        User32Ex.INSTANCE.ScreenToClient(hwnd,pt);


        x[0] = pt.x;
        y[0] = pt.y;
    }
    public static void ApplyCurrentMousePositionInApplication(WinDef.HWND hwnd,double[] x, double[]y) {
        WinDef.POINT pt = new WinDef.POINT();

        User32.INSTANCE.GetCursorPos(pt);
        User32Ex.INSTANCE.ScreenToClient(hwnd,pt);


        x[0] = pt.x;
        y[0] = pt.y;
    }
    public static void getMousePositionInApplication(WinDef.HWND hwnd, float[] out) {
        WinDef.POINT pt = new WinDef.POINT();
        User32.INSTANCE.GetCursorPos(pt);
        User32Ex.INSTANCE.ScreenToClient(hwnd, pt);

        out[0] = pt.x;
        out[1] = pt.y;
    }
}
