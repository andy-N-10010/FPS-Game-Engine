package engine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
    private Vertex[] vertices;
    private int[] indices;
    // vertex array object, position buffer object, indices buffer object
    private int vao, pbo, ibo, cbo;

    public Mesh(Vertex[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }

    public void create() {
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] positionData = new float[vertices.length * 3];
        for (int i = 0; i < vertices.length; i++) {
            // get0, get1, get2 are just getX, getY, getZ
            positionData[i * 3] = (float) vertices[i].getPosition().get0();
            positionData[i * 3 + 1] = (float) vertices[i].getPosition().get1();
            positionData[i * 3 + 2] = (float) vertices[i].getPosition().get2();
        }
        positionBuffer.put(positionData).flip();

        pbo = storeData(positionBuffer, 0, 3);


        FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] colorData = new float[vertices.length * 3];
        for (int i = 0; i < vertices.length; i++) {
            // get0, get1, get2 are just getX, getY, getZ
            colorData[i * 3] = (float) vertices[i].getColor().get0();
            colorData[i * 3 + 1] = (float) vertices[i].getColor().get1();
            colorData[i * 3 + 2] = (float) vertices[i].getColor().get2();
        }
        colorBuffer.put(colorData).flip();

        cbo = storeData(colorBuffer, 1, 3);


        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();

        ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private int storeData(FloatBuffer buffer, int index, int size) {
        int bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return bufferID;
    }

    public void destroy() {
        GL15.glDeleteBuffers(pbo);
        GL15.glDeleteBuffers(cbo);
        GL15.glDeleteBuffers(ibo);

        GL30.glDeleteVertexArrays(vao);

    }


    public Vertex[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public int getVAO() {
        return vao;
    }

    public int getPBO() {
        return pbo;
    }

    public int getCBO(){
        return cbo;
    }

    public int getIBO() {
        return ibo;
    }
}
