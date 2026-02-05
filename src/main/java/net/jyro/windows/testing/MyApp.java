package net.jyro.windows.testing;

import net.jyro.windows.graphics.TB11.matrixsys.Point;
import net.jyro.windows.gui.Application;
import net.jyro.windows.gui.ApplicationProperties;
import net.jyro.windows.gui.color.ColorHandler;
import net.jyro.windows.gui.anim.ObjectAnimator;

public class MyApp extends Application {

    private float angle = 0f;
    private static final float RADIUS = 80f;

    private float cx = 250;
    private float cy = 350;

    private float fixedX = cx;
    private float fixedY = cy - 150;

    public MyApp(ApplicationProperties properties) {
        super(properties);
        batch().setViewport(getWidth(), getHeight());
    }
    @Override
    protected void draw() {
        angle += 0.002f;
        float[] radpos = ObjectAnimator.circularMotion(cx,cy,RADIUS,angle);

        fixedY = radpos[1] - 170;
        batch().drawRect(fixedX-50,fixedY-50,100,50,0xFF00FF);

        batch().drawCirclePixels(cx,cy,RADIUS,RADIUS,0xFFFF0F,80);
        batch().drawLinePixels(new Point(radpos[0],radpos[1]),new Point(fixedX,fixedY),ColorHandler.of(100,200,255));
        batch().drawRect(radpos[0],radpos[1],5,5, ColorHandler.of(255,0,0));
    }

    public static void main(String[] args) throws InterruptedException {
        launch(new MyApp(new ApplicationProperties("ExampleApp",500,500)));
    }
}
