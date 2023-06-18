package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.graphics.shader.Shader;
import de.igelstudios.igelengine.client.graphics.texture.Texture;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public abstract class Batch<T extends BatchContent> {
    private final Shader shader;
    protected float[] vertices;
    private int vao,vbo;
    private int size;
    private boolean dirty;
    private final int orgSize;
    private int gi = 0;
    private int[] attrs;
    private int totalInBits = 0;
    private int total = 0;
    /**
     * last parameter of {@link Batch#Batch(int, Shader, boolean, int...)}  Batch
     */
    private final boolean movable;
    private BatchSupplier<T> supplier;

    /**
     * Super Constructor of any Batch child class
     * @param size should be parameter of child constructor
     * @param shader the shader the batch will use
     * @param attrs every attribute where the value is the size of the specific attribute
     * @param movable determent's weather objects rendered by this can be moved relative to the player
     */
    public Batch(int size,Shader shader,boolean movable,int ... attrs){
        this.orgSize = size;
        this.shader = shader;
        this.size = size;
        this.attrs = attrs;

        for (int attr : attrs) {
            total += attr;
        }

        this.movable = movable;

        totalInBits = total * 4;


        vertices = new float[size * totalInBits];

        load();
    }

    private int[] genIndices() {
        int[] i = new int[size * 6];
        for (int j = 0; j < size; j++) {
            loadIndices(i,j);
        }
        return i;
    }

    public abstract boolean dirtyCheck(List<T> objs);

    public void render(BatchSupplier<T> supplier){
        this.supplier = supplier;
        supplier.getSize();
        if(!dirty) dirty = dirtyCheck(supplier.getT());
        if(dirty) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            dirty = false;
        }

        shader.use();
        if(movable) {
            shader.putMat("projMat", supplier.getProjMat());
            shader.putMat("viewMat", supplier.getViewMat());
        }

        for (Texture texture : Texture.get()) {
            glActiveTexture(GL_TEXTURE0 + texture.getID());
            texture.bind();
        }
        shader.pitInt("tex", new int[]{0, 1, 2, 3, 4, 5, 6, 7});

        glBindVertexArray(vao);
        for (int i = 0; i < attrs.length; i++) {
            glEnableVertexAttribArray(i);
        }

        glDrawElements(GL_TRIANGLES,supplier.getSize() * 6,GL_UNSIGNED_INT,0);

        for (int i = 0; i < attrs.length; i++) {
            glDisableVertexAttribArray(i);
        }
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

    protected abstract void addP(int j,T obj);

    public void add(int i,T obj){
        int j = supplier.getSize(i) * totalInBits;
        if(j + obj.getLength() * totalInBits > vertices.length)widen();

        addP(j,obj);
        dirty  = true;
        gi += totalInBits * obj.getLength();
    }

    public void add(T obj){
        int j = gi * totalInBits;
        if(j + obj.getLength() * totalInBits > vertices.length)widen();


        addP(j,obj);
        dirty = true;
        gi += totalInBits * obj.getLength();
    }

    /*public void add(int i,SceneObject obj){
        int j = i * 20;
        if(j >= vertices.length)widen();

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
            vertices[j + 2] = obj.getTex();
            vertices[j + 3] = (Texture.TEX_COORDS[m].x + obj.getUv().x)  / Texture.SPRITE_PER_TEXTURE;
            vertices[j + 4] = (Texture.TEX_COORDS[m].y + obj.getUv().y)  / Texture.SPRITE_PER_TEXTURE;

            j += 5;
        }
        obj.unMarkDirty();
    }*/

    public int getSize() {
        return size;
    }

    private void widen(){
        int bSize = size;
        size += orgSize;
        float[] nvertices = new float[size * totalInBits];
        System.arraycopy(vertices,0,nvertices,0,bSize * totalInBits);

        vertices = nvertices;

        load();
    }

    private void load(){
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        glBufferData(GL_ARRAY_BUFFER,vertices.length * 4L,GL_DYNAMIC_DRAW);

        int ebo = glGenBuffers();
        int[] indices = genIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indices,GL_STATIC_DRAW);

        int j = 0;
        for (int i = 0; i < attrs.length; i++) {
            glVertexAttribPointer(i,attrs[i],GL_FLOAT,false,totalInBits,j);
            glEnableVertexAttribArray(i);

            j += attrs[i] * 4;
        }

        dirty = true;
    }
}
