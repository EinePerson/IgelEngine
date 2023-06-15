package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.client.ClientScene;
import de.igelstudios.igelengine.client.graphics.shader.Shader;
import de.igelstudios.igelengine.client.graphics.texture.Texture;
import de.igelstudios.igelengine.common.scene.Scene;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderer {
    private Shader shader;
    private Texture tex;

    private float[] vertexArray = {
            100.5f, 0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f,1,1, // Bottom right 0
            0.5f,  100.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f,0,0, // Top left     1
            100.5f,  100.5f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f,1,0, // Top right    2
            0.5f, 0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f,0,1 // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            /*
                    x        x
                    x        x
             */
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };


    int vao, vbo,ebo;
    public Renderer(){
        tex = Texture.get("test.png");
        shader = new Shader("default");
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize + 2) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2,2,GL_FLOAT,false,36,28);
        glEnableVertexAttribArray(2);
    }

    public void render(ClientScene scene){
        shader.use();
        //shader.putTex("tex",0);
        //glActiveTexture(GL_TEXTURE0);
        //tex.bind();
        shader.putMat("projMat",scene.getCam().getProjMat());
        shader.putMat("viewMat",scene.getCam().getViewMat());
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.unUse();
    }
}
