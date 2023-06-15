package de.igelstudios.igelengine.common.scene;

import de.igelstudios.igelengine.common.util.Test;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SceneObject {
    protected Vector2f pos;
    protected String name;

    @Test
    protected Vector4f col;

    public SceneObject setPos(Vector2f pos) {
        this.pos = pos;
        return this;
    }

    @Test
    public SceneObject setCol(Vector4f col) {
        this.col = col;
        return this;
    }

    @Test
    public Vector4f getCol() {
        return col;
    }

    public SceneObject setName(String name) {
        this.name = name;
        return this;
    }

    public Vector2f getPos() {
        return pos;
    }
}
