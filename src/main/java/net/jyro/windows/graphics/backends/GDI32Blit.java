package net.jyro.windows.graphics.backends;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;

import java.util.Arrays;
import java.util.List;

public interface GDI32Blit extends StdCallLibrary {

    GDI32Blit INSTANCE = Native.load("gdi32", GDI32Blit.class);

    int SRCCOPY = 0x00CC0020;

    // swap buffers
    void SwapBuffers(WinDef.HDC hdc);
    void SwapBuffers(long hdc);

    int ChoosePixelFormat(WinDef.HDC hdc, PIXELFORMATDESCRIPTOR pfd);
    boolean SetPixelFormat(WinDef.HDC hdc, int format, PIXELFORMATDESCRIPTOR pfd);

    // BITMAPINFOHEADER
    class BITMAPINFOHEADER extends Structure {
        public int biSize = size();
        public int biWidth;
        public int biHeight;
        public short biPlanes = 1;
        public short biBitCount = 32; // 32-bit ARGB
        public int biCompression = 0;  // BI_RGB
        public int biSizeImage = 0;
        public int biXPelsPerMeter = 0;
        public int biYPelsPerMeter = 0;
        public int biClrUsed = 0;
        public int biClrImportant = 0;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(
                "biSize", "biWidth", "biHeight", "biPlanes", "biBitCount",
                "biCompression", "biSizeImage", "biXPelsPerMeter",
                "biYPelsPerMeter", "biClrUsed", "biClrImportant"
            );
        }
    }

    // BITMAPINFO
    class BITMAPINFO extends Structure {
        public BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
        public int[] bmiColors = new int[1];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("bmiHeader", "bmiColors");
        }
    }

    int SetDIBitsToDevice(
        WinDef.HDC hdc,
        int xDest, int yDest, int w, int h,
        int xSrc, int ySrc, int StartScan, int cLines,
        byte[] lpvBits,
        BITMAPINFO lpbi,
        int ColorUse
    );

    /**
     * Updated PIXELFORMATDESCRIPTOR to correct flags and sizes
     */
    class PIXELFORMATDESCRIPTOR extends Structure {
        public short nSize = (short) size(); // size of this struct
        public short nVersion = 1;
        public int dwFlags = 0x00000004 | 0x00000020 | 0x00000001;
        // PFD_DRAW_TO_WINDOW | PFD_DOUBLEBUFFER | PFD_SUPPORT_OPENGL
        public byte iPixelType = 0; // PFD_TYPE_RGBA
        public byte cColorBits = 32;
        public byte cRedBits = 0;
        public byte cRedShift = 0;
        public byte cGreenBits = 0;
        public byte cGreenShift = 0;
        public byte cBlueBits = 0;
        public byte cBlueShift = 0;
        public byte cAlphaBits = 0;
        public byte cAlphaShift = 0;
        public byte cAccumBits = 0;
        public byte cAccumRedBits = 0;
        public byte cAccumGreenBits = 0;
        public byte cAccumBlueBits = 0;
        public byte cAccumAlphaBits = 0;
        public byte cDepthBits = 24;
        public byte cStencilBits = 8;
        public byte cAuxBuffers = 0;
        public byte iLayerType = 0; // PFD_MAIN_PLANE
        public byte bReserved = 0;
        public int dwLayerMask = 0;
        public int dwVisibleMask = 0;
        public int dwDamageMask = 0;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(
                "nSize", "nVersion", "dwFlags", "iPixelType",
                "cColorBits", "cRedBits", "cRedShift", "cGreenBits", "cGreenShift",
                "cBlueBits", "cBlueShift", "cAlphaBits", "cAlphaShift",
                "cAccumBits", "cAccumRedBits", "cAccumGreenBits", "cAccumBlueBits",
                "cAccumAlphaBits", "cDepthBits", "cStencilBits", "cAuxBuffers",
                "iLayerType", "bReserved", "dwLayerMask", "dwVisibleMask", "dwDamageMask"
            );
        }
    }
}
