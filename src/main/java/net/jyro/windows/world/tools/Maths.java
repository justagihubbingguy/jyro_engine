package net.jyro.windows.world.tools;

import net.jyro.windows.world.camera.CameraView;
import net.jyro.windows.world.camera.CameraViewer;
import net.jyro.windows.world.camera.CameraWindows;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Maths {
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();

        matrix.translate(translation);

        matrix.rotateX((float) Math.toRadians(rx))
            .rotateY((float) Math.toRadians(ry))
            .rotateZ((float) Math.toRadians(rz));

        matrix.scale(scale);

        return matrix;
    }
    public static void createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale,Matrix4f transformationMatrix) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();

        matrix.translate(translation);

        matrix.rotateX((float) Math.toRadians(rx))
            .rotateY((float) Math.toRadians(ry))
            .rotateZ((float) Math.toRadians(rz));

        matrix.scale(scale);
        transformationMatrix = matrix;
    }
    public static Matrix4f createFastTransformationMatrix(
        Vector3f translation,
        float rx, float ry, float rz,
        float scale,
        Matrix4f dest
    ) {
        dest.identity()
            .translate(translation)
            .rotateXYZ(
                (float) Math.toRadians(rx),
                (float) Math.toRadians(ry),
                (float) Math.toRadians(rz)
            )
            .scale(scale);

        return dest;
    }

    public static Matrix4f createViewMatrix(CameraView camera) {
        Vector3f pos = camera.getPosition();

        return new Matrix4f()
            .identity()
            .rotateX((float) Math.toRadians(camera.getPitch()))
            .rotateY((float) Math.toRadians(camera.getYaw()))
            .translate(-pos.x, -pos.y, -pos.z);
    }
    public static Matrix4f createViewMatrix(CameraWindows camera) {
        Vector3f pos = camera.getPosition();

        return new Matrix4f()
            .identity()
            .rotateX((float) Math.toRadians(camera.getPitch()))
            .rotateY((float) Math.toRadians(camera.getYaw()))
            .translate(-pos.x, -pos.y, -pos.z);
    }

}
