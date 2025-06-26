package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.client.graphics.batch.BatchContent;
import org.joml.Vector2f;

public class Polygon implements BatchContent {
    private Vector2f[] coords;
    private float r,g,b,a;
    private boolean dirty = true;

    public Polygon(Vector2f[] coords,float r,float g,float b,float a) {
        this.coords = coords;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Polygon(Vector2f ... coords){
        this(coords,0f,0f,0f,1.0f);
    }

    public boolean isDirty() {
        return dirty;
    }

    public void unMarkDirty(){
        dirty = false;
    }

    public void markDirty(){
        dirty = true;
    }

    public Vector2f[] getCoords() {
        return coords;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public float getA() {
        return a;
    }

    @Override
    public int getLength() {
        return coords.length;
    }

    @Override
    public int formerLength() {
        return coords.length;
    }
}
