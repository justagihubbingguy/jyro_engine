package net.jyro.windows.graphics.backends.fragment;

import net.jyro.windows.graphics.backends.GDI32Blit;
import net.jyro.windows.gui.Application;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

import static org.lwjgl.system.windows.User32.CW_USEDEFAULT;

public class W32Helper {

    private final Application window;
    private WinDef.HWND hwnd;
    private final WinUser.WindowProc wndProcRef; // strong reference

    public W32Helper(Application window) {
        this.window = window;

        // Create strong reference to WindowProc
        wndProcRef = new WinUser.WindowProc() {
            @Override
            public WinDef.LRESULT callback(WinDef.HWND hWnd, int uMsg, WinDef.WPARAM wParam, WinDef.LPARAM lParam) {
                switch (uMsg) {
                    case WinUser.WM_DESTROY:
                        User32.INSTANCE.PostQuitMessage(0);
                        return new WinDef.LRESULT(0);

                    case WinUser.WM_KEYDOWN:
                        int key = wParam.intValue();
                        if (key == 0x1B) { // ESC
                            User32.INSTANCE.PostQuitMessage(0);
                            return new WinDef.LRESULT(0);
                        }
                        if (key == 0x7A) { // F11
                            return new WinDef.LRESULT(0);
                        }
                        break;
                }
                return User32.INSTANCE.DefWindowProc(hWnd, uMsg, wParam, lParam);
            }
        };
    }

    private static final int CS_VREDRAW  = 0x0001;
    private static final int CS_HREDRAW  = 0x0002;
    private static final int CS_OWNDC    = 0x0020;

    public WinDef.HWND createWindow(int width, int height, String title) {
        WinUser.WNDCLASSEX wc = new WinUser.WNDCLASSEX();
        wc.cbSize = wc.size();
        wc.style = CS_OWNDC | CS_HREDRAW | CS_VREDRAW;
        wc.hInstance = null;
        wc.lpszClassName = "TabiomWindowClass";
        wc.lpfnWndProc = wndProcRef; // assign strong reference here

        User32.INSTANCE.RegisterClassEx(wc);

        hwnd = User32.INSTANCE.CreateWindowEx(
            0,
            wc.lpszClassName,
            title,
            WinUser.WS_OVERLAPPEDWINDOW | WinUser.WS_VISIBLE,
            CW_USEDEFAULT,
            CW_USEDEFAULT,
            width,
            height,
            null,
            null,
            wc.hInstance,
            null
        );

        if (hwnd == null)
            throw new RuntimeException("CreateWindowEx failed");

        WinDef.HDC hdc = User32.INSTANCE.GetDC(hwnd);
        if (hdc == null)
            throw new RuntimeException("GetDC failed");

        setupPixelFormat(hdc);

        return hwnd;
    }

    private void setupPixelFormat(WinDef.HDC hdc) {
        GDI32Blit.PIXELFORMATDESCRIPTOR pfd = new GDI32Blit.PIXELFORMATDESCRIPTOR();
        pfd.dwFlags = 0x00000004 | 0x00000020 | 0x00000001; // PFD_DRAW_TO_WINDOW | PFD_DOUBLEBUFFER | PFD_SUPPORT_OPENGL
        pfd.iPixelType = 0; // PFD_TYPE_RGBA
        pfd.cColorBits = 32;
        pfd.cDepthBits = 24;
        pfd.iLayerType = 0; // PFD_MAIN_PLANE

        int format = GDI32Blit.INSTANCE.ChoosePixelFormat(hdc, pfd);
        if (format == 0) throw new RuntimeException("ChoosePixelFormat failed");

        if (!GDI32Blit.INSTANCE.SetPixelFormat(hdc, format, pfd))
            throw new RuntimeException("SetPixelFormat failed");
    }

    public Application getWindow() {
        return window;
    }

    public WinDef.HWND getHwnd() {
        return hwnd;
    }
}
