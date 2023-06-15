package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.client.ClientScene;
import de.igelstudios.igelengine.client.graphics.shader.Shader;
import de.igelstudios.igelengine.common.scene.SceneObject;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Batch {
    private Shader shader;
    private float[] vertices;
    private int vao,vbo;
    private final int size;

    public Batch(int size){
        shader = new Shader("default");
        vertices = new float[size * 24];
        this.size = size;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        glBufferData(GL_ARRAY_BUFFER,vertices.length * 4L,GL_DYNAMIC_DRAW);

        int ebo = glGenBuffers();
        int[] indices = genIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indices,GL_STATIC_DRAW);

        glVertexAttribPointer(0,2,GL_FLOAT,false,24,0);
        glEnableVertexAttribArray(0);


        glVertexAttribPointer(1,4,GL_FLOAT,false,24,8);
        glEnableVertexAttribArray(1);
    }

    private int[] genIndices() {
        int[] i = new int[size * 6];
        for (int j = 0; j < size; j++) {
            loadIndices(i,j);
        }
        return i;
    }

    public void render(ClientScene scene){
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        glBufferSubData(GL_ARRAY_BUFFER,0,vertices);

        shader.use();
        shader.putMat("projMat",scene.getCam().getProjMat());
        shader.putMat("viewMat",scene.getCam().getViewMat());

        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES,scene.getObjects().size() * 6,GL_UNSIGNED_INT,0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.unUse();
    }

    private void loadIndices(int[] i, int j) {
        int k = 6 * j;
        int l = 4 * j;
        i[k] = l + 3;
        i[k + 1] = l + 2;
        i[k + 2] = l;

        i[k + 3] = l;
        i[k + 4] = l + 2;
        i[k + 5] = l + 1;
    }

    public void add(int i,SceneObject obj){
        int j = i * 24;

        float k = 1.0f;
        float l = 1.0f;
        for (int m = 0; m < 4; m++) {
            switch (m) {
                case 1 -> l = 0.0f;
                case 2 -> k = 0.0f;
                case 3 -> l = 1.0f;
            }


            vertices[j] = obj.getPos().x + k;
            vertices[j + 1] = obj.getPos().y + l;

            vertices[j + 2] = obj.getCol().x;
            vertices[j + 3] = obj.getCol().y;
            vertices[j + 4] = obj.getCol().z;
            vertices[j + 5] = obj.getCol().w;

            j += 6;
        }
    }

    public int getSize() {
        return size;
    }
}
