package net.jyro.windows.world;

import net.jyro.windows.world.models.StaticModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


public class ModelLoader {
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private static List<Integer> textures = new ArrayList<Integer>();

    //Vbos are like streams of positions , color  or anything that's data. you can store anything in them, vertex data whatever you want.
    //Vaos are like teachers , they tell the gpu how to load that vbo data and how to access them to load them onto the screen.

    public StaticModel loadToVertexArray(float[] positions, int[] indices,float[]normals,float[] textureCoords,float[] tangents) {
        int vaoID = createVertexArrayObjects();
        bindIndicesBufferEBO(indices);
        storeVBOAttributeData(0,3,positions);
        storeVBOAttributeData(1, 2,textureCoords);
        storeVBOAttributeData(2,3,normals);
        storeVBOAttributeData(3, 3, tangents);
        unbindVertexArrayObjects();
        return new StaticModel(vaoID,indices.length);
    }
    public StaticModel loadInterleaved(
        float[] vertices,
        int[] indices
    ) {
        int vaoID = createVertexArrayObjects();
        bindIndicesBufferEBO(indices);

        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeFloatBufferData(vertices);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        int stride = 8 * Float.BYTES;

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, stride, 0);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, stride, 3 * Float.BYTES);
        GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, stride, 6 * Float.BYTES);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        unbindVertexArrayObjects();

        return new StaticModel(vaoID, indices.length);
    }

    public static int createColorTexture(float r, float g, float b, float a) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4);
        buffer.put((byte) (r * 255));
        buffer.put((byte) (g * 255));
        buffer.put((byte) (b * 255));
        buffer.put((byte) (a * 255));
        buffer.flip();

        int textureID = GL11.glGenTextures();
        textures.add(textureID);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        GL11.glTexImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            GL11.GL_RGBA8,
            1,
            1,
            0,
            GL11.GL_RGBA,
            GL11.GL_UNSIGNED_BYTE,
            buffer
        );

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        return textureID;
    }

    public static int createWhiteTexture() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4); // 1 pixel, RGBA
        buffer.put((byte) 255); // R
        buffer.put((byte) 255); // G
        buffer.put((byte) 255); // B
        buffer.put((byte) 255); // A
        buffer.flip();

        int textureID = GL11.glGenTextures();
        textures.add(textureID);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        GL11.glTexImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            GL11.GL_RGBA8,
            1,
            1,
            0,
            GL11.GL_RGBA,
            GL11.GL_UNSIGNED_BYTE,
            buffer
        );

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        return textureID;
    }

    public void cleanUpVertexData() {
        for(int vao:vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo:vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for(int texture : textures) {
            GL15.glDeleteTextures(texture);
        }
    }
    //binding indices
    private void bindIndicesBufferEBO(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,vboID);
        IntBuffer buffer = storeIntegerBufferData(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeIntegerBufferData(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    //creating vao
    private int createVertexArrayObjects() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    //storing data in vbo
    private void storeVBOAttributeData(int attribCount,int coordinateSize,float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
        FloatBuffer buffer = storeFloatBufferData(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attribCount,coordinateSize, GL11.GL_FLOAT,false,0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
    }
    private void unbindVertexArrayObjects() {
        GL30.glBindVertexArray(0);
    }
    public static int loadTexture(String path) {
        BufferedImage image;
        int width, height;

        try {
            image = ImageIO.read(new File(path));
            if (image == null) {
                throw new RuntimeException("ImageIO failed to decode: " + path);
            }
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load texture file: " + path, e);
        }

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = ByteBuffer
            .allocateDirect(width * height * 4)
            .order(ByteOrder.nativeOrder());

        // ARGB -> RGBA
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];

                buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
                buffer.put((byte) ((pixel >> 8)  & 0xFF)); // G
                buffer.put((byte) ( pixel        & 0xFF)); // B
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // A
            }
        }

        buffer.flip();

        int textureID = GL11.glGenTextures();
        textures.add(textureID);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        GL11.glTexImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            GL11.GL_RGBA8,
            width,
            height,
            0,
            GL11.GL_RGBA,
            GL11.GL_UNSIGNED_BYTE,
            buffer
        );

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        return textureID;
    }

    private FloatBuffer storeFloatBufferData(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
