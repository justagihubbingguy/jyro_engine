package net.jyro.windows.gui.anim;

public class ObjectAnimator {
    public static float circularSineMotion(float cpos, float radi, float angle) {
        return cpos + (float)Math.sin(angle) * radi;
    }
    public static float circularCosineMotion(float cpos, float radi, float angle) {
        return cpos + (float)Math.cos(angle) * radi;
    }
    public static float[] circularMotion(float cx, float cy, float radi, float angle) {
       float radx = cx + (float)Math.cos(angle) * radi;
       float rady = cy + (float)Math.sin(angle) * radi;
       return new float[]{radx,rady};
    }
}
