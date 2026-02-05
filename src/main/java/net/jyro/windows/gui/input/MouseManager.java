package net.jyro.windows.gui.input;

public class MouseManager {
    private static int x, y;
    private static boolean left, right;

    public static void updatePosition(int px, int py) {
        x = px;
        y = py;
    }

    public void handleButtonDown(int button) {
        if (button == 1) left = true;
        if (button == 2) right = true;
    }

    public void handleButtonUp(int button) {
        if (button == 1) left = false;
        if (button == 2) right = false;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isLeftDown() { return left; }
    public boolean isRightDown() { return right; }
}
