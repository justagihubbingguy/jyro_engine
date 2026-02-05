package net.jyro.windows.world.models.entity;

import net.jyro.windows.world.models.entity.loader.internal.ModelData;
import net.jyro.windows.world.models.entity.loader.internal.PreEntityData;

import java.util.ArrayList;
import java.util.List;

public class PreEntity {
    public static PreEntityData generateTexturedSphere(
        float radius,
        int stacks,
        int slices
    ) {

        List<Float> verts = new ArrayList<>();
        List<Integer> inds = new ArrayList<>();

        for (int i = 0; i <= stacks; i++) {
            float v = (float) i / stacks;
            float theta = (float) (v * Math.PI);

            float sinTheta = (float) Math.sin(theta);
            float cosTheta = (float) Math.cos(theta);

            for (int j = 0; j <= slices; j++) {
                float u = (float) j / slices;
                float phi = (float) (u * Math.PI * 2.0);

                float sinPhi = (float) Math.sin(phi);
                float cosPhi = (float) Math.cos(phi);

                float nx = sinTheta * cosPhi;
                float ny = cosTheta;
                float nz = sinTheta * sinPhi;

                verts.add(radius * nx);
                verts.add(radius * ny);
                verts.add(radius * nz);
                verts.add(nx);
                verts.add(ny);
                verts.add(nz);

                verts.add(u);
                verts.add(1.0f - v);
            }
        }

        int vertsPerRow = slices + 1;

        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j < slices; j++) {
                int first  = i * vertsPerRow + j;
                int second = first + vertsPerRow;

                inds.add(first);
                inds.add(second);
                inds.add(first + 1);

                inds.add(second);
                inds.add(second + 1);
                inds.add(first + 1);
            }
        }

        PreEntityData data = new PreEntityData();
        data.vertices = toFloatArray(verts);
        data.indices = inds.stream().mapToInt(i -> i).toArray();
        return data;
    }
    private static float[] toFloatArray(List<Float> list) {
        float[] arr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }
}
