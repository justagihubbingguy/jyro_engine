package net.jyro.windows.testing;

import net.jyro.windows.gui.Application;
import net.jyro.windows.gui.ApplicationProperties;
import net.jyro.windows.gui.color.ColorHandler;
import net.jyro.windows.gui.input.MouseUtils;
import net.jyro.windows.gui.rects.PointUtils;

import java.util.Random;

public class ClickEventApp extends Application {
    float cx = getWidth()/2;
    float cy = getHeight()/2;
    float vx = 1.5f;
    float vy = 1.5f;
    public ClickEventApp(ApplicationProperties properties) {
        super(properties);
        batch().setViewport(getWidth(),getHeight());
    }
    /*
      WASD MouseInfo.HDWND HGLRC
      if(KeyboardUtils.isKeyDown(0x44)) {
            cx += vx;
        };
        if(KeyboardUtils.isKeyDown(0x57)) {
            cy -= vy;
        }
        if(KeyboardUtils.isKeyDown(0x53)) {
            cy += vy;
        }
        if(KeyboardUtils.isKeyDown(0x41)) {
            cx -= vx;
        }

     */
    float bw = 100;
    float bh = 50;
    float bx = getWidth()/2;
    float by = getHeight()/2;
    Random random = new Random();

    int color = 0xFF000;
    private final float[] mouse = new float[2];

    @Override
    protected void draw() {
        MouseUtils.getMousePositionInApplication(getWindowHandle(),mouse);
        batch().drawRect(bx, by, bw, bh, color);

        if (PointUtils.pointInBox(mouse[0], mouse[1], bx, by, bw, bh)
            && MouseUtils.isMouseButtonPressed(MouseUtils.LEFT_BUTTON)) {

            color = ColorHandler.of(
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
            );
        }
    }


    public static void main(String[] args) throws InterruptedException {
        launch(new ClickEventApp(new ApplicationProperties("ClickEventApp",500,500)));
    }
}
