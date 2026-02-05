package net.jyro.windows.graphics.backends.fragment;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HGLRC;
import com.sun.jna.win32.StdCallLibrary;

public interface WGL extends StdCallLibrary {
    WGL INSTANCE = Native.load("opengl32", WGL.class);
    HGLRC wglCreateContext(HDC hdc);
    boolean wglMakeCurrent(HDC hdc, HGLRC hglrc);
    boolean wglDeleteContext(HGLRC hglrc);
}
