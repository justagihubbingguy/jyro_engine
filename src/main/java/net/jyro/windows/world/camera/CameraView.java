package net.jyro.windows.world.camera;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class CameraView {
    private Vector3f position = new Vector3f(0,0,0);

    private float pitch;
    private float yaw;
    private float roll;

    public Vector3f getPosition() {
        return position;
    }


    public CameraView(long window) {
        this.window = window;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public void incrementPosition(Vector3f position) {
        this.position.x += position.x;
        this.position.y += position.y;
        this.position.z += position.z;
    }
    long window;

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }
    public void move(float deltaTime) {
        float speed = 10f;
        float velocity = speed * deltaTime;

        Vector3f movement = new Vector3f();

        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        Vector3f forward = new Vector3f(
            (float) (Math.sin(yawRad) * Math.cos(pitchRad)),
            (float) (-Math.sin(pitchRad)),
            (float) (-Math.cos(yawRad) * Math.cos(pitchRad))
        );
        Vector3f right = new Vector3f(
            (float) Math.sin(yawRad - Math.PI / 2.0),
            0,
            (float) -Math.cos(yawRad - Math.PI / 2.0)
        );

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS)
            movement.add(forward.mul(velocity, new Vector3f()));

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS)
            movement.sub(forward.mul(velocity, new Vector3f()));
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS)
            movement.sub(right.mul(velocity, new Vector3f()));

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS)
            movement.add(right.mul(velocity, new Vector3f()));

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS)
            movement.y += velocity;

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS)
            movement.y -= velocity;
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS)
            GLFW.glfwSetWindowShouldClose(window,true);

        incrementPosition(movement);
    }

    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MAX_PITCH = 89.0f;

    private boolean firstMouse = true;
    private double lastMouseX;
    private double lastMouseY;

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

        double xOffset = x[0] - lastMouseX;
        double yOffset = lastMouseY - y[0];

        lastMouseX = x[0];
        lastMouseY = y[0];

        yaw   += (float) (xOffset * MOUSE_SENSITIVITY);
        pitch -= (float) (yOffset * MOUSE_SENSITIVITY);

        if (pitch > MAX_PITCH)  pitch = MAX_PITCH;
        if (pitch < -MAX_PITCH) pitch = -MAX_PITCH;
    }



    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
