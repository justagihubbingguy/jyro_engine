package net.jyro.windows.world.camera;

import net.jyro.windows.world.tools.Maths;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class MouseInteractor {
    private Vector3f currentRay;
    private Matrix4f projection;
    private Matrix4f viewMatrix;

    // FIXED: Arrays must be initialized with size 1 to receive values from GLFW
    private double[] mouseXi = new double[1];
    private double[] mouseYi = new double[1];
    private int[] width = new int[1];
    private int[] height = new int[1];

    private int windowWidth;
    private int windowHeight;
    private float mouseX;
    private float mouseY;
    private CameraView cameraView;

    public MouseInteractor(Matrix4f projection, CameraView cameraView) {
        this.projection = projection;
        this.cameraView = cameraView;
        this.viewMatrix = Maths.createViewMatrix(cameraView);

        // Fetch initial window and cursor data
        GLFW.glfwGetCursorPos(cameraView.window, mouseXi, mouseYi);
        GLFW.glfwGetWindowSize(cameraView.window, width, height);

        this.windowWidth = width[0];
        this.windowHeight = height[0];
        this.mouseX = (float) mouseXi[0];
        this.mouseY = (float) mouseYi[0];
    }

    public MouseInteractor(Matrix4f projection, CameraViewer cameraViewer) {
        this.projection = projection;
        this.cameraView = cameraViewer;
        this.viewMatrix = cameraViewer.getViewMatrix();

        GLFW.glfwGetCursorPos(cameraViewer.getWindow(), mouseXi, mouseYi);
        GLFW.glfwGetWindowSize(cameraViewer.getWindow(), width, height);

        this.windowWidth = width[0];
        this.windowHeight = height[0];
        this.mouseX = (float) mouseXi[0];
        this.mouseY = (float) mouseYi[0];
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }

    public void update() {
        // Update window size and cursor position every frame
        GLFW.glfwGetCursorPos(cameraView.window, mouseXi, mouseYi);
        GLFW.glfwGetWindowSize(cameraView.window, width, height);

        this.windowWidth = width[0];
        this.windowHeight = height[0];
        this.mouseX = (float) mouseXi[0];
        this.mouseY = (float) mouseYi[0];

        viewMatrix = Maths.createViewMatrix(cameraView);
        currentRay = calculateRay();
    }

    private Vector3f calculateRay() {
        Vector2f normalized = toNormalizedDeviceCoordinates(mouseX, mouseY);
        Vector4f clipCoordinates = new Vector4f(normalized.x, normalized.y, -1f, 1f);
        Vector4f eyeCoordinates = toEyeCoordinates(clipCoordinates);
        Vector3f worldRay = toWorldCoordinates(eyeCoordinates);
        return worldRay;
    }

    private Vector4f toEyeCoordinates(Vector4f clip) {
        // Create a copy to avoid modifying original projection
        Matrix4f invertedProjection = new Matrix4f(projection).invert();
        Vector4f eyeCoordinates = invertedProjection.transform(clip, new Vector4f());
        return new Vector4f(eyeCoordinates.x, eyeCoordinates.y, -1f, 0f);
    }

    private Vector3f toWorldCoordinates(Vector4f eyeCoordinates) {
        Matrix4f invertedView = new Matrix4f(viewMatrix).invert();
        Vector4f rayWorld = invertedView.transform(eyeCoordinates, new Vector4f());
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalize();
        return mouseRay;
    }

    private Vector2f toNormalizedDeviceCoordinates(float mouseX, float mouseY) {
        float x = (2.0f * mouseX) / windowWidth - 1.0f;
        float y = (2.0f * mouseY) / windowHeight - 1.0f;
        // Flip Y because OpenGL's origin is bottom-left, but GLFW is top-left
        return new Vector2f(x, -y);
    }
}
