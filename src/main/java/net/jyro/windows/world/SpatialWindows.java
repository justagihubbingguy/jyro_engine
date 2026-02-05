package net.jyro.windows.world;

import net.jyro.windows.world.models.StaticModel;
import net.jyro.windows.world.models.TexturedModel;
import net.jyro.windows.world.models.entity.Entity;
import net.jyro.windows.world.shaders.DefaultShader;
import net.jyro.windows.world.tools.Maths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.*;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.HWND;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class SpatialWindows {

    private static final float FOV = 70f;
    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 1000f;

    private final Matrix4f projectionMatrix;
    private final Matrix4f transformMatrix = new Matrix4f();

    private final int windowWidth;
    private final int windowHeight;

    public SpatialWindows(DefaultShader shader, HWND hwnd) {
        int[] size = getFramebufferSize(hwnd);
        this.windowWidth = size[0];
        this.windowHeight = size[1];

        projectionMatrix = createProjectionMatrix(windowWidth, windowHeight);

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.loadTextureUnits();
        shader.stop();

        glClearColor(0.5f, 0.58f, 0.55f, 1f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    private static int[] getFramebufferSize(HWND hwnd) {
        RECT rect = new RECT();
        User32.INSTANCE.GetClientRect(hwnd, rect);
        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;
        return new int[]{width, height};
    }

    public void beginFrame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Map<TexturedModel, List<Entity>> batches, DefaultShader shader) {
        for (TexturedModel model : batches.keySet()) {
            prepareTexturedModel(model);

            for (Entity entity : batches.get(model)) {
                prepareInstance(entity, shader);
                GL11.glDrawElements(
                    GL_TRIANGLES,
                    model.getModel().getVertexCount(),
                    GL_UNSIGNED_INT,
                    0
                );
            }

            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel model) {
        StaticModel smodel = model.getModel();

        GL30.glBindVertexArray(smodel.getVaoID());
        GL20.glEnableVertexAttribArray(0); // position
        GL20.glEnableVertexAttribArray(1); // texCoords
        GL20.glEnableVertexAttribArray(2); // normal
        GL20.glEnableVertexAttribArray(3); // tangent

        // Diffuse texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_2D, model.getDiffuseTextureId());

        // Normal map
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL_TEXTURE_2D, model.getNormalMapTextureId());
    }

    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_2D, 0);

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void prepareInstance(Entity entity, DefaultShader shader) {
        transformMatrix.identity();
        Maths.createFastTransformationMatrix(
            entity.getPosition(),
            entity.getRotX(),
            entity.getRotY(),
            entity.getRotZ(),
            entity.getScale(),
            transformMatrix
        );
        shader.loadTransformationMatrix(transformMatrix);
    }

    private Matrix4f createProjectionMatrix(int width, int height) {
        float aspect = (float) width / (float) height;
        return new Matrix4f().perspective(
            (float) Math.toRadians(FOV),
            aspect,
            Z_NEAR,
            Z_FAR
        );
    }
}
