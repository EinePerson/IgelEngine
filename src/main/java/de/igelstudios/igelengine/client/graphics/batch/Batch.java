package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.graphics.shader.Shader;
import de.igelstudios.igelengine.client.graphics.texture.Texture;
import de.igelstudios.igelengine.client.graphics.texture.TexturePool;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * This is an Engine internal super class which helps with drawing all kinds of objects to the screen
 * @param <T> the object that this batch renders, to be set by the implementing class
 */
public abstract class Batch<T extends BatchContent> {
    private final Shader shader;
    protected float[] vertices;
    private int vao,vbo;
    private int size;
    private boolean dirty;
    private final int orgSize;
    private int gi = 0;
    private int[] attrs;
    protected final int totalInBits;
    private int total = 0;
    private int ebo;
    protected int[] indices;
    private boolean dynamic;
    protected int id;
    /**
     * last parameter of {@link Batch#Batch(int, Shader, boolean,boolean, int...)}  Batch
     */
    private final boolean movable;
    //private BatchSupplier<T> supplier;

    /**
     * Super Constructor of any Batch child class
     * @param size should be parameter of child constructor
     * @param shader the shader the batch will use
     * @param attrs every attribute where the value is the size of the specific attribute
     * @param movable determent's weather objects rendered by this can be moved relative to the player
     * @param dynamic whether the indices are dynamic or static rectangles
     */
    public Batch(int size,Shader shader,boolean movable,boolean dynamic,int ... attrs){
        this.orgSize = size;
        this.shader = shader;
        this.size = size;
        this.attrs = attrs;
        this.dynamic = dynamic;

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
        if(!dynamic) {
            for (int j = 0; j < size; j++) {
                loadIndices(i, j);
            }
        }
        return i;
    }

    public abstract boolean dirtyCheck(List<T> objs, BatchSupplier<T> supplier);

    public void clearP(int i,List<T> objs){

    }

    /**
     * removes the given object from the list
     * @param i the list index where the object should be removed from
     * @param objs the list of objects
     */
    protected void clear(int i, List<T> objs){
        if(dynamic){
            clearP(i,objs);
            return;
        }
        //System.out.println("A     A\n" + Arrays.toString(vertices) + "A     A");
        int k = 0;
        for (int j = 0; j < objs.size() && j < i; j++) {
            k += objs.get(j).formerLength();
        }
        k *= totalInBits;
        int l = k + totalInBits * objs.get(i).formerLength();
        for (int j = k; j < l; j++) {
            vertices[j] = 0;
        }
        //System.out.println("B     B\n" + Arrays.toString(vertices) + "B     B");
        /*float[] nvertecies = new float[vertices.length];
        System.arraycopy(vertices,0,nvertecies,0,k);
        System.arraycopy(vertices,l,nvertecies,k,vertices.length - l);
        //System.out.println("C     C\n" + Arrays.toString(nvertecies) + "C     C");
        vertices = nvertecies;*/

        System.arraycopy(vertices,l,vertices,k,vertices.length - l);
    }

    /**
     * clears everything from this Batch
     */
    public void clearBatch(){
        Arrays.fill(vertices,0);
        if(dynamic)Arrays.fill(indices,0);
    }


    public void render(BatchSupplier<T> supplier){
        supplier.getSize();
        if(!dirty) dirty = dirtyCheck(supplier.getT(),supplier);
        if(dirty) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

            if(dynamic){
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ebo);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER,indices,GL_STATIC_DRAW);
            }
            dirty = false;
        }

        shader.use();
        if(movable) {
            shader.putMat("projMat", supplier.getProjMat());
            shader.putMat("viewMat", supplier.getViewMat());
        }

        if(shader.usesTexture()) {
            for (Texture texture : TexturePool.get()) {
                glActiveTexture(GL_TEXTURE0 + texture.getID());
                texture.bind();
            }
            shader.pitInt("tex", new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        }

        glBindVertexArray(vao);
        for (int i = 0; i < attrs.length; i++) {
            glEnableVertexAttribArray(i);
        }

        glDrawElements(GL_TRIANGLES,supplier.getVertexCount(),GL_UNSIGNED_INT,0);

        for (int i = 0; i < attrs.length; i++) {
            glDisableVertexAttribArray(i);
        }
        glBindVertexArray(0);

        if(shader.usesTexture()) {
            for (Texture texture : TexturePool.get()) {
                glActiveTexture(GL_TEXTURE0 + texture.getID());
                texture.unbind();
            }
        }
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

    protected void loadIndicesP(int[] indices,int startID,int vertexStart,T obj){

    }

    protected abstract void addP(int j,T obj);

    /**
     * overrides the object specified by the index
     * @param i the index of where in the list the object should be overridden
     * @param obj the list that contains all the objects
     */
    public void add(int i,T obj,BatchSupplier<T> supplier){
        int j = supplier.getSize(i) * totalInBits;
        if(dynamic)j /= 4;
        while (j + obj.getLength() * totalInBits > vertices.length)widen();

        addP(j,obj);
        if(dynamic)loadIndicesP(indices,supplier.getIndicesSize(i),supplier.getSize(i),obj);
        dirty  = true;
        gi += totalInBits * obj.getLength();
    }

    public int add(T obj,BatchSupplier<T> supplier){
        int j = gi * totalInBits;
        if(dynamic)j /= 4;
        while (j + obj.getLength() * totalInBits > vertices.length)widen();


        addP(j,obj);
        if(dynamic)loadIndicesP(indices,supplier.getIndicesSize(),gi,obj);
        dirty = true;
        int i = gi;
        gi += totalInBits * obj.getLength();
        return i;
    }

    private void widen(){
        int bSize = size;
        size += orgSize;
        float[] nvertices = new float[size * totalInBits];
        System.arraycopy(vertices,0,nvertices,0,bSize * totalInBits);

        vertices = nvertices;

        load();
        //ClientEngine.queueForRenderThread(this::load);
    }

    private void load(){
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        glBufferData(GL_ARRAY_BUFFER,vertices.length * 4L,GL_DYNAMIC_DRAW);

        ebo = glGenBuffers();
        indices = genIndices();
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

    public void setId(int id){
        this.id = id;
    }
}
