package de.igelstudios.igelengine.common.scene;

import de.igelstudios.igelengine.client.graphics.batch.BatchContent;
import org.joml.Vector2f;
import org.joml.Vector2i;

/**
 * This class is an Object in the Scene
 * if anything changes the Object has to be {@link #markDirty()} to make the changes be reflected on the clients
 */
public class SceneObject implements BatchContent {
    protected Vector2f pos = new Vector2f();
    protected Vector2i uv = new Vector2i();
    protected Vector2f size = new Vector2f();
    private Vector2f texSize = new Vector2f();
    protected int tex;
    private boolean dirty;

    public SceneObject setPos(float x,float y) {
        setPos(new Vector2f(x,y));
        return this;
    }

    public SceneObject setPos(Vector2f pos) {
        this.pos = pos;
        markDirty();
        return this;
    }

    public SceneObject setTex(int tex) {
        this.tex = tex;
        markDirty();
        return this;
    }

    public SceneObject setTexSize(Vector2f texSize) {
        this.texSize = texSize;
        return this;
    }

    public Vector2f getTexSize() {
        return texSize;
    }

    public SceneObject setUv(Vector2i uv) {
        this.uv = uv;
        markDirty();
        return this;
    }

    public SceneObject setUv(int u,int v) {
        this.uv = new Vector2i(u,v);
        markDirty();
        return this;
    }

    public SceneObject setSize(Vector2f size) {
        this.size = size;
        return this;
    }

    public Vector2f getSize() {
        return size;
    }

    public int getTex() {
        return tex;
    }

    public Vector2i getUv() {
        return uv;
    }


    public Vector2f getPos() {
        return pos;
    }

    public void markDirty(){
        dirty = true;
    }

    public void unMarkDirty(){
        dirty = false;
    }

    public boolean isDirty() {
        return dirty;
    }

    @Override
    public int getLength() {
        return 1;
    }
}
