package de.igelstudios.igelengine.common.entity;

import org.joml.Vector2f;

public abstract class Entity {
    protected Vector2f pos;

    public Vector2f getPos() {
        return pos;
    }

    public void setPos(Vector2f pos) {
        this.pos = pos;
    }
}
