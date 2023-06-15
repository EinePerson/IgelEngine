package de.igelstudios.igelengine.common.scene;

import de.igelstudios.igelengine.common.util.Test;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

public class SceneObject {
    protected Vector2f pos;
    protected String name;
    protected Vector2i uv;

    protected int tex;

    public SceneObject setPos(Vector2f pos) {
        this.pos = pos;
        return this;
    }

    public SceneObject setTex(int tex) {
        this.tex = tex;
        return this;
    }

    public SceneObject setUv(Vector2i uv) {
        this.uv = uv;
        return this;
    }

    public SceneObject setUv(int u,int v) {
        this.uv = new Vector2i(u,v);
        return this;
    }

    public int getTex() {
        return tex;
    }

    public Vector2i getUv() {
        return uv;
    }

    public SceneObject setName(String name) {
        this.name = name;
        return this;
    }

    public Vector2f getPos() {
        return pos;
    }
}
