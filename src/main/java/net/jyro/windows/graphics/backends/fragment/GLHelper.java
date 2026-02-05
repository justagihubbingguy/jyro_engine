package net.jyro.windows.graphics.backends.fragment;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class GLHelper {

    private int vao;
    private int vbo;

    private int shaderProgram;
    private int mvpLoc;

    public GLHelper() {}

    public void setupVAO(float[] vertices) {

        if (vao != 0) glDeleteVertexArrays(vao);
        if (vbo != 0) glDeleteBuffers(vbo);

        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        FloatBuffer fb = MemoryUtil.memAllocFloat(vertices.length);
        fb.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
        MemoryUtil.memFree(fb);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    public void setupShaders() {

        String vs = "#version 330 core\n" +
            "layout(location=0) in vec3 inPos;\n" +
            "layout(location=1) in vec3 inColor;\n" +
            "uniform mat4 modelViewProjection;\n" +
            "out vec3 fragColor;\n" +
            "void main(){ gl_Position = modelViewProjection * vec4(inPos,1.0); fragColor=inColor; }";

        String fs = "#version 330 core\n" +
            "in vec3 fragColor;\n" +
            "out vec4 outColor;\n" +
            "void main(){ outColor = vec4(fragColor, 1.0); }";

        int vsID = compile(GL_VERTEX_SHADER, vs);
        int fsID = compile(GL_FRAGMENT_SHADER, fs);

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vsID);
        glAttachShader(shaderProgram, fsID);
        glLinkProgram(shaderProgram);

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException("Shader link failed: " + glGetProgramInfoLog(shaderProgram));

        glDeleteShader(vsID);
        glDeleteShader(fsID);

        mvpLoc = glGetUniformLocation(shaderProgram, "modelViewProjection");
    }

    private int compile(int type, String source) {
        int s = glCreateShader(type);
        glShaderSource(s, source);
        glCompileShader(s);
        if (glGetShaderi(s, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Shader error: " + glGetShaderInfoLog(s));
        return s;
    }

    public int getVao() { return vao; }
    public int getShaderProgram() { return shaderProgram; }
    public int getMvpLoc() { return mvpLoc; }
}
