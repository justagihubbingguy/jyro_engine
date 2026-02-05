package net.jyro.windows.gui.backends;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.win32.StdCallLibrary;

public interface GDI32Ex extends StdCallLibrary {
    GDI32Ex INSTANCE = Native.load("gdi32", GDI32Ex.class);

    boolean TextOutW(HDC hdc, int nXStart, int nYStart, char[] lpString, int cchString);
}

