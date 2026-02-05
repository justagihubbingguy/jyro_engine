package net.jyro.windows.graphics.TB11.matrixsys;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Mat4 {
    public float[][] m = new float[4][4];

    public Mat4() {
        for (int i = 0; i < 4; i++) m[i][i] = 1;
    }

    public static Mat4 translation(float x, float y, float z) {
        Mat4 t = new Mat4();
        t.m[0][3] = x;
        t.m[1][3] = y;
        t.m[2][3] = z;
        return t;
    }

    public static Mat4 rotation(float x, float y, float z) {
        Mat4 rx = new Mat4();
        Mat4 ry = new Mat4();
        Mat4 rz = new Mat4();
        float cosX = (float)Math.cos(x), sinX = (float)Math.sin(x);
        float cosY = (float)Math.cos(y), sinY = (float)Math.sin(y);
        float cosZ = (float)Math.cos(z), sinZ = (float)Math.sin(z);

        rx.m[1][1] = cosX; rx.m[1][2] = -sinX; rx.m[2][1] = sinX; rx.m[2][2] = cosX;
        ry.m[0][0] = cosY; ry.m[0][2] = sinY; ry.m[2][0] = -sinY; ry.m[2][2] = cosY;
        rz.m[0][0] = cosZ; rz.m[0][1] = -sinZ; rz.m[1][0] = sinZ; rz.m[1][1] = cosZ;

        return rz.mul(ry).mul(rx);
    }

    public static Mat4 perspective(float fov, float aspect, float near, float far) {
        Mat4 p = new Mat4();
        float f = 1f / (float)Math.tan(fov / 2f);
        p.m[0][0] = f / aspect;
        p.m[1][1] = f;
        p.m[2][2] = (far + near) / (near - far);
        p.m[2][3] = (2 * far * near) / (near - far);
        p.m[3][2] = -1;
        p.m[3][3] = 0;
        return p;
    }

    public Mat4 mul(Mat4 o) {
        Mat4 r = new Mat4();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                r.m[i][j] = 0;
                for (int k = 0; k < 4; k++)
                    r.m[i][j] += this.m[i][k] * o.m[k][j];
            }
        return r;
    }

    public Vec4 mul(Vec4 v) {
        float[] r = new float[4];
        float[] vec = {v.x, v.y, v.z, v.w};
        for (int i = 0; i < 4; i++) {
            r[i] = 0;
            for (int j = 0; j < 4; j++) r[i] += m[i][j] * vec[j];
        }
        return new Vec4(r[0], r[1], r[2], r[3], v.color);
    }
    public float[] toFloatArray() {
        float[] f = new float[16];
        // OpenGL expects column-major order
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                f[col * 4 + row] = m[row][col];
            }
        }
        return f;
    }
    public FloatBuffer toFloatBuffer() {
        FloatBuffer fb = MemoryUtil.memAllocFloat(16);
        fb.put(toFloatArray()).flip();
        return fb;
    }
}
