package net.jyro.windows.gui.backends;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinUser.*;

public interface User32Ex extends StdCallLibrary {

    User32Ex INSTANCE = (User32Ex) Native.loadLibrary("user32", User32Ex.class, W32APIOptions.DEFAULT_OPTIONS);
    boolean ScreenToClient(WinDef.HWND hWnd, WinDef.POINT lpPoint);
    int ShowCursor(boolean bShow);
    HWND CreateWindowEx(
        int dwExStyle,
        String lpClassName,
        String lpWindowName,
        int dwStyle,
        int x,
        int y,
        int nWidth,
        int nHeight,
        HWND hWndParent,
        HMENU hMenu,
        HMODULE hInstance,
        Pointer lpParam
    );

    boolean ShowWindow(HWND hWnd, int nCmdShow);
    boolean UpdateWindow(HWND hWnd);
    HDC GetDC(HWND hWnd);
    boolean ReleaseDC(HWND hWnd, HDC hdc);

    int GetMessage(MSG lpMsg, HWND hWnd, int wMsgFilterMin, int wMsgFilterMax);
    boolean TranslateMessage(MSG lpMsg);
    LRESULT DispatchMessage(MSG lpMsg);
    boolean PeekMessage(MSG lpMsg, HWND hWnd, int wMsgFilterMin, int wMsgFilterMax, int wRemoveMsg);
    LRESULT DefWindowProc(HWND hWnd, int Msg, WPARAM wParam, LPARAM lParam);
    void PostQuitMessage(int nExitCode);

    Pointer LoadImage(HINSTANCE hInst, String lpszName, int uType, int cxDesired, int cyDesired, int fuLoad);

    boolean MoveWindow(HWND hWnd, int X, int Y, int nWidth, int nHeight, boolean bRepaint);
    void InvalidateRect(HWND hWnd, Pointer lpRect, boolean bErase);

    // Constants
    int IMAGE_CURSOR = 2;
    int IMAGE_ICON = 1;
    int LR_DEFAULTSIZE = 0x00000040;
    int LR_SHARED = 0x00008000;
    int PM_REMOVE = 0x0001;

    // Virtual keys
    int VK_LEFT = 0x25;
    int VK_UP = 0x26;
    int VK_RIGHT = 0x27;
    int VK_DOWN = 0x28;
    int VK_W = 0x57;
    int VK_A = 0x41;
    int VK_S = 0x53;
    int VK_D = 0x44;

    // Overload for long HWND if needed
    HDC GetDC(long hWnd);
}
