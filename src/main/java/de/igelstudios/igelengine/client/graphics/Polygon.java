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

    public Polygon(Polygon polygon){
        coords = new  Vector2f[polygon.coords.length];
        System.arraycopy(polygon.coords, 0, coords, 0, polygon.coords.length);
        r = polygon.r;
        g = polygon.g;
        b = polygon.b;
        a = polygon.a;
    }

    public Polygon(Vector2f ... coords){
        this(coords,0f,0f,0f,1.0f);
    }

    public void setColor(float r,float g,float b,float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        markDirty();
    }

    public void setA(float a) {
        this.a = a;
        markDirty();
    }

    public void setR(float r) {
        this.r = r;
        markDirty();
    }

    public void setG(float g) {
        this.g = g;
        markDirty();
    }

    public void setB(float b) {
        this.b = b;
        markDirty();
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

    public static Polygon fromLines(Line ... lines){
        Vector2f[] coords = new Vector2f[lines.length];
        if(!lines[0].getStart().equals(lines[lines.length - 1].getEnd()))throw new IllegalArgumentException("Lines in a polygon must connect");
        coords[0] = new Vector2f(lines[0].getEnd());
        for (int i = 1; i < lines.length; i++) {
            if(!coords[i - 1].equals(lines[i].getStart()))throw new IllegalArgumentException("Lines in a polygon must connect");
            coords[i] = new Vector2f(lines[i].getEnd());
        }

        return new Polygon(coords);
    }

    public Polygon getAtDelta(float x,float y){
        Polygon copy = new Polygon(this);
        for (Vector2f coord : copy.coords) {
            coord.x += x;
            coord.y += y;
        }
        return copy;
    }
}
