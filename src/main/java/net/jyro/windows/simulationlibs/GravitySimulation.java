package net.jyro.windows.simulationlibs;

import net.jyro.windows.addons.physics.GravityObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class GravitySimulation extends JPanel implements Runnable {
    private final float G = 1.0f;
    private final float dt = 0.1f;
    private GravityObject[] objects;
    private Timer timer;

    public GravitySimulation() {
        objects = new GravityObject[]{
            new GravityObject(250, 150, 2f, 0f, 40),
            new GravityObject(250, 220, 0f, 0f, 700)
        };

        timer = new Timer(16, e -> {
            updatePhysics();
            repaint();
        });
        timer.start();
    }

    public void updatePhysics() {
        for (GravityObject o : objects) {
            o.ax = 0;
            o.ay = 0;
        }
        for (int i = 0; i < objects.length; i++) {
            for (int j = i + 1; j < objects.length; j++) {
                float[] f = computeGravity(objects[i], objects[j]);
                objects[i].ax += f[0] / objects[i].m;
                objects[i].ay += f[1] / objects[i].m;

                objects[j].ax -= f[0] / objects[j].m;
                objects[j].ay -= f[1] / objects[j].m;
            }
        }
        for (GravityObject o : objects) {
            o.update(dt);
        }
    }
    public float[] computeGravity(GravityObject obj1, GravityObject obj2) {
        float dx = obj2.x - obj1.x;
        float dy = obj2.y - obj1.y;
        float r2 = dx * dx + dy * dy + 0.01f;
        float F = G * obj1.m * obj2.m / r2;
        float r = (float)Math.sqrt(r2);
        return new float[]{F * dx / r, F * dy / r};
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (GravityObject o : objects) {
            float radius = (float)Math.sqrt(o.m);
            g2d.setColor(Color.RED);
            g2d.fill(new Ellipse2D.Float(o.x - radius / 2, o.y - radius / 2, radius, radius));
        }
    }
    @Override
    public void run() {
        JFrame frame = new JFrame("Gravity Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.add(this);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GravitySimulation());
    }
}
