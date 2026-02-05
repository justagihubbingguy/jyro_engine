package net.jyro.windows.graphics.TB11;

import net.jyro.windows.graphics.TB11.matrixsys.Point;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * GPU-BottleNecked ShapeRasterizer:
 * - uses a stupid VAO/VBO
 * - uses a shitty shader (position + color + ortho projection)
 * - accepts coordinates in shitty SPACE (origin at bottom-left)
 */
public class ShapeRasterizer {

    private final List<float[]> vertexList = new ArrayList<>(); // each entry = {x,y,z,r,g,b}
    private int vao = -1;
    private int vbo = -1;
    private int shaderProgram = -1;
    private int uProjLoc = -1;

    private int viewW = 800;
    private int viewH = 600;

    public ShapeRasterizer() {
        createShader();
        createBuffers();
    }
    public void drawLinePixels(
        float x1, float y1,
        float x2, float y2,
        int color
    ) {
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        float thickness = 1.0f;

        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = (float)Math.sqrt(dx * dx + dy * dy);
        if (len == 0) return;

        dx /= len;
        dy /= len;

        float px = -dy * thickness * 0.5f;
        float py =  dx * thickness * 0.5f;

        float[] v0 = {x1 + px, y1 + py, 0f, r, g, b};
        float[] v1 = {x1 - px, y1 - py, 0f, r, g, b};
        float[] v2 = {x2 - px, y2 - py, 0f, r, g, b};
        float[] v3 = {x2 + px, y2 + py, 0f, r, g, b};

        drawTriangle(v0, v1, v2);
        drawTriangle(v0, v2, v3);
    }
    public void drawLinePixels(
        Point start,
        Point source,
        int color
    ) {
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        float thickness = 1.0f;
        float x1 = start.getX();
        float x2 = source.getX();
        float y1 = start.getY();
        float y2 = source.getY();
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = (float)Math.sqrt(dx * dx + dy * dy);
        if (len == 0) return;

        dx /= len;
        dy /= len;

        float px = -dy * thickness * 0.5f;
        float py =  dx * thickness * 0.5f;

        float[] v0 = {x1 + px, y1 + py, 0f, r, g, b};
        float[] v1 = {x1 - px, y1 - py, 0f, r, g, b};
        float[] v2 = {x2 - px, y2 - py, 0f, r, g, b};
        float[] v3 = {x2 + px, y2 + py, 0f, r, g, b};

        drawTriangle(v0, v1, v2);
        drawTriangle(v0, v2, v3);
    }

    private Point[] trail;
    private int trailLength;
    private boolean trailInit = false;

    public void implementTrail(int TRAIL_LENGTH,float x, float y,int w, int h,int color) {
        if (!trailInit || trailLength != TRAIL_LENGTH) {
            trail = new Point[TRAIL_LENGTH];
            trailLength = TRAIL_LENGTH;
            trailInit = true;
        }
        for (int i = TRAIL_LENGTH - 1; i > 0; i--) {
            trail[i] = trail[i - 1];
        }
        trail[0] = new Point(x,y);
        for (int i = 0; i < TRAIL_LENGTH; i++) {
            if(trail[i] != null) {
                drawRect(trail[i].getX(),trail[i].getY(),w,h,color);
            }
        }
    }
    public void setViewport(int width, int height) {
        this.viewW = Math.max(1, width);
        this.viewH = Math.max(1, height);
        useProgram();
        float[] ortho = orthoMatrix(0, viewW, 0, viewH); // left, right, bottom, top in pixels
        glUniformMatrix4fv(uProjLoc, false, ortho);
        glUseProgram(0);
    }

    public void clear() {
        vertexList.clear();
    }

    public void drawTriangle(float[] v0, float[] v1, float[] v2) {
        vertexList.add(v0);
        vertexList.add(v1);
        vertexList.add(v2);
    }

    public void drawRectPixels(int x, int y, int w, int h, int color) {
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        // bottom-left origin (pixel-space)
        float[] v0 = {x,     y,     0f, r, g, b};
        float[] v1 = {x + w, y,     0f, r, g, b};
        float[] v2 = {x + w, y + h, 0f, r, g, b};
        float[] v3 = {x,     y + h, 0f, r, g, b};

        drawTriangle(v0, v1, v2);
        drawTriangle(v0, v2, v3);
    }

    public void drawRect(float x, float y, float w, float h, int color) {
        // interpret x,y,w,h as pixel-space same as drawRectPixels
        drawRectPixels(Math.round(x), Math.round(y), Math.round(w), Math.round(h), color);
    }

    public void drawCirclePixels(float cx, float cy, float radiusX, float radiusY, int color, int segments) {
        float rcol = ((color >> 16) & 0xFF) / 255f;
        float gcol = ((color >> 8) & 0xFF) / 255f;
        float bcol = (color & 0xFF) / 255f;
        float[] center = {cx, cy, 0f, rcol, gcol, bcol};

        for (int i = 0; i < segments; i++) {
            double t1 = 2 * Math.PI * i / segments;
            double t2 = 2 * Math.PI * (i + 1) / segments;
            float[] v1 = {cx + (float)Math.cos(t1) * radiusX, cy + (float)Math.sin(t1) * radiusY, 0f, rcol, gcol, bcol};
            float[] v2 = {cx + (float)Math.cos(t2) * radiusX, cy + (float)Math.sin(t2) * radiusY, 0f, rcol, gcol, bcol};
            drawTriangle(center, v1, v2);
        }
    }

    public void drawCirclePixels(float cx, float cy, float radius, int color, int segments) {
        drawCirclePixels(cx, cy, radius, radius, color, segments);
    }

    public void drawRoundedRect(float cx, float cy, float w, float h, float radius, int color, float angleDegrees) {
        // cx,cy in pixel space (center), radius in pixels
        // compute corner centers in unrotated local coords
        float hw = w / 2f;
        float hh = h / 2f;

        float[] topLeft     = rotatePoint(cx - hw + radius, cy + hh - radius, cx, cy, angleDegrees);
        float[] topRight    = rotatePoint(cx + hw - radius, cy + hh - radius, cx, cy, angleDegrees);
        float[] bottomRight = rotatePoint(cx + hw - radius, cy - hh + radius, cx, cy, angleDegrees);
        float[] bottomLeft  = rotatePoint(cx - hw + radius, cy - hh + radius, cx, cy, angleDegrees);

        // corner circles (pixel radii)
        drawCirclePixels(topLeft[0], topLeft[1], radius, color, 12);
        drawCirclePixels(topRight[0], topRight[1], radius, color, 12);
        drawCirclePixels(bottomRight[0], bottomRight[1], radius, color, 12);
        drawCirclePixels(bottomLeft[0], bottomLeft[1], radius, color, 12);

        // connecting rectangles (use rotated rect helper)
        drawRotatedRect(cx, cy - hh + radius, w - 2 * radius, 2 * radius, angleDegrees, color);
        drawRotatedRect(cx, cy + hh - radius, w - 2 * radius, 2 * radius, angleDegrees, color);
        drawRotatedRect(cx - hw + radius, cy, 2 * radius, h - 2 * radius, angleDegrees, color);
        drawRotatedRect(cx + hw - radius, cy, 2 * radius, h - 2 * radius, angleDegrees, color);
        drawRotatedRect(cx, cy, w - 2 * radius, h - 2 * radius, angleDegrees, color);
    }

    // draw a rotated rectangle given center cx,cy (in pixels)
    public void drawRotatedRect(float cx, float cy, float w, float h, float angleDegrees, int color) {
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        float hw = w / 2f;
        float hh = h / 2f;

        float[] p0 = rotatePoint(cx - hw, cy - hh, cx, cy, angleDegrees);
        float[] p1 = rotatePoint(cx + hw, cy - hh, cx, cy, angleDegrees);
        float[] p2 = rotatePoint(cx + hw, cy + hh, cx, cy, angleDegrees);
        float[] p3 = rotatePoint(cx - hw, cy + hh, cx, cy, angleDegrees);

        float[] v0 = {p0[0], p0[1], 0f, r, g, b};
        float[] v1 = {p1[0], p1[1], 0f, r, g, b};
        float[] v2 = {p2[0], p2[1], 0f, r, g, b};
        float[] v3 = {p3[0], p3[1], 0f, r, g, b};

        drawTriangle(v0, v1, v2);
        drawTriangle(v0, v2, v3);
    }

    private static float[] rotatePoint(float x, float y, float cx, float cy, float angleDegrees) {
        float rad = (float) Math.toRadians(angleDegrees);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        float dx = x - cx;
        float dy = y - cy;
        float nx = dx * cos - dy * sin + cx;
        float ny = dx * sin + dy * cos + cy;
        return new float[]{nx, ny};
    }

    private float[] toFloatArray() {
        float[] arr = new float[vertexList.size() * 6];
        int idx = 0;
        for (float[] v : vertexList) for (float f : v) arr[idx++] = f;
        return arr;
    }

    public void render() {
        if (vertexList.isEmpty()) return;

        useProgram(); // bind shader

        float[] verts = toFloatArray();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer fb = MemoryUtil.memAllocFloat(verts.length);
        fb.put(verts).flip();
        glBufferData(GL_ARRAY_BUFFER, fb, GL_DYNAMIC_DRAW);
        MemoryUtil.memFree(fb);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawArrays(GL_TRIANGLES, 0, vertexList.size());

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        glUseProgram(0);
    }


    public void cleanup() {
        if (vbo != -1) glDeleteBuffers(vbo);
        if (vao != -1) glDeleteVertexArrays(vao);
        if (shaderProgram != -1) glDeleteProgram(shaderProgram);
    }

    // ---------- GL helper / shader / buffers ----------

    private void createBuffers() {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        // initially empty buffer allocation (will be reallocated in render)
        glBufferData(GL_ARRAY_BUFFER, 4 * 1024, GL_DYNAMIC_DRAW);

        // layout: vec3 position (x,y,z), vec3 color (r,g,b) -- floats
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private void createShader() {
        String vs = "#version 330 core\n" +
            "layout(location=0) in vec3 inPos;\n" +
            "layout(location=1) in vec3 inColor;\n" +
            "uniform mat4 uProj;\n" +
            "out vec3 fragColor;\n" +
            "void main(){ fragColor = inColor; gl_Position = uProj * vec4(inPos,1.0); }";

        String fs = "#version 330 core\n" +
            "in vec3 fragColor;\n" +
            "out vec4 outColor;\n" +
            "void main(){ outColor = vec4(fragColor,1.0); }";

        int vsId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vsId, vs);
        glCompileShader(vsId);
        if (glGetShaderi(vsId, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("VS compile: " + glGetShaderInfoLog(vsId));

        int fsId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fsId, fs);
        glCompileShader(fsId);
        if (glGetShaderi(fsId, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("FS compile: " + glGetShaderInfoLog(fsId));

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vsId);
        glAttachShader(shaderProgram, fsId);
        glLinkProgram(shaderProgram);
        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException("Program link: " + glGetProgramInfoLog(shaderProgram));

        glDeleteShader(vsId);
        glDeleteShader(fsId);

        uProjLoc = glGetUniformLocation(shaderProgram, "uProj");
    }

    private void useProgram() {
        if (shaderProgram != -1) glUseProgram(shaderProgram);
    }
    private float[] orthoMatrix(float left, float right, float bottom, float top) {
        float near = -1f;
        float far = 1f;
        float tx = -(right + left) / (right - left);
        float ty = (top + bottom) / (top - bottom); // flip y
        float tz = -(far + near) / (far - near);

        float sx = 2f / (right - left);
        float sy = -2f / (top - bottom); // flip y
        float sz = -2f / (far - near);

        return new float[]{
            sx, 0f, 0f, 0f,
            0f, sy, 0f, 0f,
            0f, 0f, sz, 0f,
            tx, ty, tz, 1f
        };
    }
}
