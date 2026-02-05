package net.jyro.windows.world.models.entity.loader;

import net.jyro.windows.world.ModelLoader;
import net.jyro.windows.world.models.entity.loader.internal.AdvancedModelData;
import org.lwjgl.assimp.*;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class PBREntityLoader {

    public static AdvancedModelData loadModel(String path) {
        int flags = aiProcess_Triangulate
            | aiProcess_GenSmoothNormals
            | aiProcess_FlipUVs
            | aiProcess_JoinIdenticalVertices
            | aiProcess_ImproveCacheLocality;

        AIScene scene = aiImportFile(path, flags);
        if (scene == null || scene.mRootNode() == null) {
            System.err.println("Assimp Error: " + aiGetErrorString());
            return null;
        }

        List<Float> vertices = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Float> tangents = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float furthestPoint = 0;

        int diffuseTextureId = 0;
        int normalMapTextureId = 0;
        int metallicRoughnessTextureId = 0;

        for (int m = 0; m < scene.mNumMeshes(); m++) {
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(m));

            for (int i = 0; i < mesh.mNumVertices(); i++) {
                AIVector3D pos = mesh.mVertices().get(i);
                vertices.add(pos.x());
                vertices.add(pos.y());
                vertices.add(pos.z());

                float length = (float) Math.sqrt(pos.x()*pos.x() + pos.y()*pos.y() + pos.z()*pos.z());
                if (length > furthestPoint) furthestPoint = length;

                // Normals
                if (mesh.mNormals() != null) {
                    AIVector3D norm = mesh.mNormals().get(i);
                    normals.add(norm.x());
                    normals.add(norm.y());
                    normals.add(norm.z());
                } else {
                    normals.add(0f); normals.add(1f); normals.add(0f);
                }

                // Texture coordinates
                if (mesh.mTextureCoords(0) != null) {
                    AIVector3D tex = mesh.mTextureCoords(0).get(i);
                    textures.add(tex.x());
                    textures.add(tex.y());
                } else {
                    textures.add(0f); textures.add(0f);
                }

                // Tangents
                if (mesh.mTangents() != null) {
                    AIVector3D tan = mesh.mTangents().get(i);
                    tangents.add(tan.x());
                    tangents.add(tan.y());
                    tangents.add(tan.z());
                } else {
                    tangents.add(1f); tangents.add(0f); tangents.add(0f);
                }
            }

            // Indices
            AIFace.Buffer facesBuffer = mesh.mFaces();
            for (int i = 0; i < mesh.mNumFaces(); i++) {
                AIFace face = facesBuffer.get(i);
                IntBuffer indicesBuffer = face.mIndices();
                while (indicesBuffer.hasRemaining()) {
                    indices.add(indicesBuffer.get() + vertices.size()/3 - mesh.mNumVertices());
                }
            }

            // Material textures (only first material for now)
            if (scene.mNumMaterials() > 0) {
                AIMaterial material = AIMaterial.create(scene.mMaterials().get(mesh.mMaterialIndex()));

                AIString texPath = AIString.calloc();

                // Diffuse texture
                if(getMaterialTexture(material, aiTextureType_DIFFUSE, texPath)) {
                    File baseDir = new File(path).getParentFile();
                    File fullPath = new File(baseDir, texPath.dataString());
                    if(fullPath.exists()) diffuseTextureId = ModelLoader.loadTexture(fullPath.getAbsolutePath());
                }

                // Normal map
                if(getMaterialTexture(material, aiTextureType_NORMALS, texPath)) {
                    File baseDir = new File(path).getParentFile();
                    File fullPath = new File(baseDir, texPath.dataString());
                    if(fullPath.exists()) normalMapTextureId = ModelLoader.loadTexture(fullPath.getAbsolutePath());
                }
                if (getMaterialTexture(material, aiTextureType_METALNESS, texPath)) {
                    File baseDir = new File(path).getParentFile();
                    File fullPath = new File(baseDir, texPath.dataString());
                    if (fullPath.exists()) {
                        metallicRoughnessTextureId = ModelLoader.loadTexture(fullPath.getAbsolutePath());
                    }
                }

                else if (getMaterialTexture(material, aiTextureType_UNKNOWN, texPath)) {
                    File baseDir = new File(path).getParentFile();
                    File fullPath = new File(baseDir, texPath.dataString());
                    if (fullPath.exists()) {
                        metallicRoughnessTextureId = ModelLoader.loadTexture(fullPath.getAbsolutePath());
                    }
                }

                // Base color fallback if no diffuse texture
                if(diffuseTextureId == 0) {
                    AIColor4D color = AIColor4D.create();
                    if(aiGetMaterialColor(material, AI_MATKEY_COLOR_DIFFUSE, 0, 0, color) == aiReturn_SUCCESS) {
                        diffuseTextureId = ModelLoader.createColorTexture(color.r(), color.g(), color.b(), color.a());
                    }
                }
            }
        }

        // Fallback white textures
        if(diffuseTextureId == 0) diffuseTextureId = ModelLoader.createWhiteTexture();
        if(normalMapTextureId == 0) normalMapTextureId = ModelLoader.createWhiteTexture();

        aiReleaseImport(scene);

        return new AdvancedModelData(
            listToArray(vertices),
            listToArray(textures),
            listToArray(normals),
            listToArray(tangents),
            indices.stream().mapToInt(i -> i).toArray(),
            furthestPoint,
            diffuseTextureId,
            normalMapTextureId,
            metallicRoughnessTextureId
        );
    }
    private static boolean getMaterialTexture(AIMaterial material, int type, AIString texPath) {
        IntBuffer i1 = org.lwjgl.BufferUtils.createIntBuffer(1);
        IntBuffer i2 = org.lwjgl.BufferUtils.createIntBuffer(1);
        FloatBuffer f1 = org.lwjgl.BufferUtils.createFloatBuffer(1);
        IntBuffer i3 = org.lwjgl.BufferUtils.createIntBuffer(1);
        IntBuffer i4 = org.lwjgl.BufferUtils.createIntBuffer(1);
        IntBuffer i5 = org.lwjgl.BufferUtils.createIntBuffer(1);

        return aiGetMaterialTexture(material, type, 0, texPath, i1, i2, f1, i3, i4, i5) == aiReturn_SUCCESS;
    }

    private static float[] listToArray(List<Float> list) {
        float[] array = new float[list.size()];
        for(int i=0; i<list.size(); i++) array[i] = list.get(i);
        return array;
    }
}
