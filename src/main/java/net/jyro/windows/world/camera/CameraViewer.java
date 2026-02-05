package net.jyro.windows.world.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class CameraViewer extends CameraView {
    private long window;

    private Vector3f position = new Vector3f();
    private Vector3f target = new Vector3f(0, 0, 0);

    private float distance = 5.0f;
    private float yaw = 0.0f;
    private float pitch = 20.0f;

    private static final float MOUSE_SENSITIVITY = 0.3f;
    private static final float SCROLL_SENSITIVITY = 2.0f;
    private static final float MIN_DISTANCE = 1.0f;
    private static final float MAX_DISTANCE = 3000.0f;
    private static final float MAX_PITCH = 89f;

    private boolean firstMouse = true;
    private double lastMouseX;
    private double lastMouseY;

    public long getWindow() {
        return window;
    }

    public CameraViewer(long window) {
        super(window);
        this.window=window;
    }
    public void update() {
        float speed = 0.5f; // adjust as needed
        float yawRad = (float) Math.toRadians(yaw);

        Vector3f forward = new Vector3f(
            (float) Math.sin(yawRad),
            0,
            (float) Math.cos(yawRad)
        ).normalize();

        Vector3f right = new Vector3f(
            (float) Math.sin(yawRad - Math.PI / 2),
            0,
            (float) Math.cos(yawRad - Math.PI / 2)
        ).normalize();

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            target.add(new Vector3f(forward).mul(speed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            target.sub(new Vector3f(forward).mul(speed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            target.add(new Vector3f(right).mul(speed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            target.sub(new Vector3f(right).mul(speed));
        }

        updateMouseLook();

        updatePosition();
    }


    public void updateMouseLook() {
        double[] x = new double[1];
        double[] y = new double[1];
        GLFW.glfwGetCursorPos(window, x, y);

        if (firstMouse) {
            lastMouseX = x[0];
            lastMouseY = y[0];
            firstMouse = false;
            return;
        }

        double dx = x[0] - lastMouseX;
        double dy = y[0] - lastMouseY;

        lastMouseX = x[0];
        lastMouseY = y[0];

        if (GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
            yaw -= dx * MOUSE_SENSITIVITY;
            pitch += dy * MOUSE_SENSITIVITY;
        }
        pitch = Math.max(-MAX_PITCH, Math.min(MAX_PITCH, pitch));
    }

    public void onScroll(double yOffset) {
        distance -= yOffset * SCROLL_SENSITIVITY;
        distance = Math.max(MIN_DISTANCE, Math.min(MAX_DISTANCE, distance));
    }

    public void pan(float dx, float dy) {
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        Vector3f right = new Vector3f(
            (float) Math.sin(yawRad - Math.PI / 2.0),
            0,
            (float) -Math.cos(yawRad - Math.PI / 2.0)
        );
        Vector3f up = new Vector3f(0, 1, 0);

        target.add(right.mul(-dx, new Vector3f()));
        target.add(up.mul(dy, new Vector3f()));
    }

    public void updatePosition() {
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        position.x = target.x + distance * (float) (Math.cos(pitchRad) * Math.sin(yawRad));
        position.y = target.y + distance * (float) Math.sin(pitchRad);
        position.z = target.z + distance * (float) (Math.cos(pitchRad) * Math.cos(yawRad));
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, target, new Vector3f(0, 1, 0));
    }

    public Vector3f getPosition() { return position; }
    public Vector3f getTarget() { return target; }
    public void setTarget(Vector3f target) { this.target.set(target); }
    public float getDistance() { return distance; }
    public void setDistance(float distance) { this.distance = distance; }
}
