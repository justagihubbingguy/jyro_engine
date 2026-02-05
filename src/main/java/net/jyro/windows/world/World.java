package net.jyro.windows.world;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import net.jyro.windows.gui.Application;
import net.jyro.windows.graphics.backends.GDI32Blit;
import net.jyro.windows.graphics.backends.fragment.WGL;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.WGLARBCreateContext;
import org.lwjgl.opengl.WGLARBPixelFormat;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

public abstract class World extends Application {

    protected WinDef.HGLRC hglrc;

    protected World(String title, int width, int height) {
        super(title, width, height);
        createGL3Context();
        init(); // VAOs, shaders, glTF
    }

    /* ============================================================
       OpenGL 3.3 Core Context (Win32 + LWJGL WGL)
       ============================================================ */
    private void createGL3Context() {
        WinDef.HDC hdc = getHDC();

        /* -------- 1. Dummy legacy context (JNA WGL) -------- */
        WinDef.HGLRC dummy = WGL.INSTANCE.wglCreateContext(hdc);
        WGL.INSTANCE.wglMakeCurrent(hdc, dummy);
        GL.createCapabilities();

        /* -------- 2. Convert HDC -> long for LWJGL -------- */
        long hdcLong = Pointer.nativeValue(hdc.getPointer());

        /* -------- 3. Choose pixel format (LWJGL WGL) -------- */
        IntBuffer pfAttribs = BufferUtils.createIntBuffer(15);
        pfAttribs
            .put(WGLARBPixelFormat.WGL_DRAW_TO_WINDOW_ARB).put(1)
            .put(WGLARBPixelFormat.WGL_SUPPORT_OPENGL_ARB).put(1)
            .put(WGLARBPixelFormat.WGL_DOUBLE_BUFFER_ARB).put(1)
            .put(WGLARBPixelFormat.WGL_PIXEL_TYPE_ARB)
            .put(WGLARBPixelFormat.WGL_TYPE_RGBA_ARB)
            .put(WGLARBPixelFormat.WGL_COLOR_BITS_ARB).put(24)
            .put(WGLARBPixelFormat.WGL_DEPTH_BITS_ARB).put(24)
            .put(0)
            .flip();

        IntBuffer formats = BufferUtils.createIntBuffer(1);
        IntBuffer count   = BufferUtils.createIntBuffer(1);

        if (!WGLARBPixelFormat.wglChoosePixelFormatARB(
            hdcLong, pfAttribs, null, formats, count) ||
            count.get(0) == 0) {
            throw new IllegalStateException("wglChoosePixelFormatARB failed");
        }

        GDI32.INSTANCE.SetPixelFormat(hdc, formats.get(0), null);

        IntBuffer ctxAttribs = BufferUtils.createIntBuffer(7);
        ctxAttribs
            .put(WGLARBCreateContext.WGL_CONTEXT_MAJOR_VERSION_ARB).put(3)
            .put(WGLARBCreateContext.WGL_CONTEXT_MINOR_VERSION_ARB).put(3)
            .put(0)
            .flip();

        long gl3 = WGLARBCreateContext.wglCreateContextAttribsARB(
            hdcLong, MemoryUtil.NULL, ctxAttribs);

        if (gl3 == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to create OpenGL 3.3 context");
        }

        /* -------- 5. Replace dummy with real context -------- */
        WGL.INSTANCE.wglMakeCurrent(null, null);
        WGL.INSTANCE.wglDeleteContext(dummy);

        hglrc = new WinDef.HGLRC(new Pointer(gl3));
        WGL.INSTANCE.wglMakeCurrent(hdc, hglrc);
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    /* ============================================================
       Win32 Render Loop
       ============================================================ */
    @Override
    public final void renderLoop() throws InterruptedException {
        WinUser.MSG msg = new WinUser.MSG();
        boolean running = true;

        while (running) {
            while (User32.INSTANCE.PeekMessage(
                msg, getWindowHandle(), 0, 0, 1)) {

                User32.INSTANCE.TranslateMessage(msg);
                User32.INSTANCE.DispatchMessage(msg);

                if (msg.message == WinUser.WM_QUIT) {
                    running = false;
                }
            }

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render();
            GDI32Blit.INSTANCE.SwapBuffers(getHDC());
            Thread.sleep(16);
        }

        cleanup();
    }

    protected abstract void init();

    protected abstract void render();

    protected abstract void cleanup();

    protected WinDef.HDC getHDC() {
        return hdc;
    }
}

