package net.jyro.windows.gui.rects;

public class PointUtils {
    public static boolean pointInBoxRobust(
        float px, float py,
        float ax, float ay,
        float bx, float by
    ) {
        float minX = Math.min(ax, bx);
        float maxX = Math.max(ax, bx);
        float minY = Math.min(ay, by);
        float maxY = Math.max(ay, by);

        return px >= minX && px <= maxX &&
            py >= minY && py <= maxY;
    }
    public static boolean pointInBox(
        float px, float py,
        float x, float y,
        float width, float height
    ) {
        return pointInBoxRobust(
            px, py,
            x, y,
            x + width, y + height
        );
    }


}
