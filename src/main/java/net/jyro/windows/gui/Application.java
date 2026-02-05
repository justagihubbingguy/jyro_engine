package net.jyro.windows.gui;

import net.jyro.windows.graphics.TB11.ShapeRasterizer;
import net.jyro.windows.graphics.backends.fragment.W32Helper;
import net.jyro.windows.graphics.backends.fragment.WGL;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinUser;
import net.jyro.windows.graphics.backends.GDI32Blit;
import net.jyro.windows.gui.input.Input;
import net.jyro.windows.gui.input.MouseManager;

import static net.jyro.windows.gui.backends.User32Ex.PM_REMOVE;
import static org.lwjgl.opengl.GL11.*;

public abstract class Application {
    public static final int WM_MOUSEMOVE     = 0x0200;
    public static final int WM_LBUTTONDOWN   = 0x0201;
    public static final int WM_LBUTTONUP     = 0x0202;
    public static final int WM_RBUTTONDOWN   = 0x0204;
    public static final int WM_RBUTTONUP     = 0x0205;
    public static final int WM_MOUSEWHEEL    = 0x020A;
    private int HEIGHT;
    private int WIDTH;
    private W32Helper w32;
    private HWND hwnd;
    protected HDC hdc;
    protected HGLRC hglrc;
    private ShapeRasterizer rasterizer;

    protected Input.KeyManager keys = new Input.KeyManager();
    protected MouseManager mouse = new MouseManager();

    public Application(String title,int width,int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        w32 = new W32Helper(this) {
            public WinUser.WindowProc createWndProc() {
                return new WinUser.WindowProc() {
                    @Override
                    public WinDef.LRESULT callback(HWND hWnd, int uMsg, WinDef.WPARAM wParam, WinDef.LPARAM lParam) {
                        switch(uMsg) {
                            case WinUser.WM_DESTROY: User32.INSTANCE.PostQuitMessage(0); return new WinDef.LRESULT(0);
                            case WinUser.WM_KEYDOWN: keys.handleKeyDown(wParam.intValue()); return new WinDef.LRESULT(0);
                            case WinUser.WM_KEYUP: keys.handleKeyUp(wParam.intValue()); return new WinDef.LRESULT(0);
                            case WM_MOUSEMOVE:
                                int mx = lParam.intValue() & 0xFFFF;
                                int my = (lParam.intValue() >> 16) & 0xFFFF;
                                mouse.updatePosition(mx,my);
                                return new WinDef.LRESULT(0);
                            case WM_LBUTTONDOWN: mouse.handleButtonDown(1); return new WinDef.LRESULT(0);
                            case WM_LBUTTONUP: mouse.handleButtonUp(1); return new WinDef.LRESULT(0);
                            case WM_RBUTTONDOWN: mouse.handleButtonDown(2); return new WinDef.LRESULT(0);
                            case WM_RBUTTONUP: mouse.handleButtonUp(2); return new WinDef.LRESULT(0);
                        }
                        return User32.INSTANCE.DefWindowProc(hWnd,uMsg,wParam,lParam);
                    }
                };
            }
        };
        hwnd = w32.createWindow(WIDTH, HEIGHT, title);
        hdc = User32.INSTANCE.GetDC(hwnd);
        hglrc = WGL.INSTANCE.wglCreateContext(hdc);
        WGL.INSTANCE.wglMakeCurrent(hdc,hglrc);
        org.lwjgl.opengl.GL.createCapabilities();
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        rasterizer = new ShapeRasterizer();
        rasterizer.setViewport(WIDTH,HEIGHT);
        setup();
    }
    public Application(ApplicationProperties properties) {
        this.WIDTH = properties.getConcurrentApplicationWidth();
        this.HEIGHT = properties.getConcurrentApplicationHeight();
        w32 = new W32Helper(this) {
            public WinUser.WindowProc createWndProc() {
                return new WinUser.WindowProc() {
                    @Override
                    public WinDef.LRESULT callback(HWND hWnd, int uMsg, WinDef.WPARAM wParam, WinDef.LPARAM lParam) {
                        switch(uMsg) {
                            case WinUser.WM_DESTROY: User32.INSTANCE.PostQuitMessage(0); return new WinDef.LRESULT(0);
                            case WinUser.WM_KEYDOWN: keys.handleKeyDown(wParam.intValue()); return new WinDef.LRESULT(0);
                            case WinUser.WM_KEYUP: keys.handleKeyUp(wParam.intValue()); return new WinDef.LRESULT(0);

                            case WM_MOUSEMOVE:
                                int mx = lParam.intValue() & 0xFFFF;
                                int my = (lParam.intValue() >> 16) & 0xFFFF;
                                mouse.updatePosition(mx, my);
                                break;

                            case WM_LBUTTONDOWN:
                                mouse.handleButtonDown(1);
                                break;
                            case WM_LBUTTONUP:
                                mouse.handleButtonUp(1);
                                break;

                            case WM_RBUTTONDOWN:
                                mouse.handleButtonDown(2);
                                break;
                            case WM_RBUTTONUP:
                                mouse.handleButtonUp(2);
                                break;
                        }
                        return User32.INSTANCE.DefWindowProc(hWnd,uMsg,wParam,lParam);
                    }
                };
            }
        };
        hwnd = w32.createWindow(WIDTH, HEIGHT, properties.getApplicationTitle());
        hdc = User32.INSTANCE.GetDC(hwnd);
        hglrc = WGL.INSTANCE.wglCreateContext(hdc);
        WGL.INSTANCE.wglMakeCurrent(hdc,hglrc);
        org.lwjgl.opengl.GL.createCapabilities();
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        rasterizer = new ShapeRasterizer();
        rasterizer.setViewport(WIDTH,HEIGHT);
        setup();
    }

    protected abstract void draw();
    protected void setup() {}
    public static float normalize(int value, int min, int max) {
        return (float)(value - min) / (max - min);
    }
    public float ndcX(float px, float width) {
        return (px / width) * 2f - 1f;
    }

    public float ndcY(float py, float height) {
        return -((py / height) * 2f - 1f);
    }

    public float ndcRadiusX(float r, float width) {
        return (r / width) * 2f;
    }

    public float ndcRadiusY(float r, float height) {
        return (r / height) * 2f;
    }
    public static float ndc(float value, float min, float max) {
        return normalize(value, min, max) * 2f - 1f; // maps 0..1 to -1..1
    }
    public static float normalize(float value, float min, float max) {
        return (value - min) / (max - min);
    }
    public static double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }
    public static float normalizeClamped(float value, float min, float max) {
        float n = (value - min) / (max - min);
        return Math.max(0f, Math.min(1f, n));
    }
    public void renderLoop() throws InterruptedException {
        WinUser.MSG msg = new WinUser.MSG();
        boolean running = true;
        while(running){
            while(User32.INSTANCE.PeekMessage(msg, hwnd, 0,0,PM_REMOVE)){
                User32.INSTANCE.TranslateMessage(msg);
                User32.INSTANCE.DispatchMessage(msg);
                if(msg.message==WinUser.WM_QUIT) running=false;
            }
            glClearColor(0f,0f,0f,1f);
            glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
            rasterizer.clear();
            draw();
            rasterizer.render();
            GDI32Blit.INSTANCE.SwapBuffers(hdc);
            Thread.sleep(16);
        }
        if(hglrc!=null){
            WGL.INSTANCE.wglMakeCurrent(null,null);
            WGL.INSTANCE.wglDeleteContext(hglrc);
        }
        rasterizer.cleanup();
    }

    public ShapeRasterizer batch(){ return rasterizer; }
    public Input.KeyManager getKeys(){ return keys; }
    public MouseManager getMouse(){ return mouse; }
    public int getHeight() {
        return HEIGHT;
    }
    public int getWidth() {
        return WIDTH;
    }
    public float[] implementBoundedCollision(float x, float y, float sizeX, float sizeY,float dx, float dy) {
        if(x - sizeX < 0 || x + sizeX > WIDTH) dx = -dx;
        if(y - sizeY < 0 || y + sizeY > HEIGHT) dy = -dy;
        return new float[]{dx,dy};
    }
    public HWND getWindowHandle() {
        return hwnd;
    }
    public static void launch(Application w) throws InterruptedException { w.renderLoop(); }
}
