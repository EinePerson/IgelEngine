package de.igelstudios.igelengine.common.scene;

import de.igelstudios.igelengine.common.util.Test;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

/**
 * This class is an Object in the Scene
 * if anything changes the Object has to be {@link #markDirty()} to make the changes be reflected on the clients
 */
public class SceneObject {
    protected Vector2f pos;
    protected Vector2i uv;

    protected int tex;
    private boolean dirty;

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
}
