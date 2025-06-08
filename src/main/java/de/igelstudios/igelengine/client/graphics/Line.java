package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.client.graphics.batch.BatchContent;
import org.joml.Vector2f;

public class Line implements BatchContent {
    private final Vector2f start;
    private Vector2f startUp;
    private Vector2f end;
    private Vector2f endUp;
    private float length;
    private float angle;
    private float thickness;
    private float r,g,b,a;
    private boolean dirty;
    private boolean remove;

    public Line(Vector2f start, Vector2f end,float thickness) {
        this(start,(end.x - start.x) / (end.y - start.y),(float) Math.sqrt(Math.pow(end.x - start.x,2) + Math.pow(end.y - start.y,2)),thickness);
    }

    public Line(Vector2f start,float angleD,float length,float thickness) {
        this.start = start;
        this.angle = (float) Math.toRadians(angleD);
        this.length = length;
        this.thickness = thickness;

        //double angle = Math.tanh(incline);
        double deltaX = length * Math.cos(angle);
        double deltaY = length * Math.sin(angle);
        end = new Vector2f((float) (start.x + deltaX), (float) (start.y + deltaY));
        Vector2f directional = new Vector2f((float) -deltaY, (float) deltaX).normalize().mul(thickness);
        startUp = directional.add(start);
        Vector2f distance = new Vector2f(end).sub(start);
        endUp = new Vector2f(startUp).add(distance);

        r = 0.0f;
        g = 0.0f;
        b = 0.0f;
        a = 1.0f;
        dirty = true;
    }

    public void remove(){
        remove = true;
    }

    public boolean toRemove(){
        return remove;
    }

    public float getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public float getG() {
        return g;
    }

    public float getR() {
        return r;
    }

    public Line setThickness(float thickness) {
        this.thickness = thickness;

        endUp.sub(end).mul(thickness).add(end);
        startUp.sub(start).mul(thickness).add(start);

        dirty = true;
        return this;
    }

    public Line setIncline(float angleD) {
        this.angle = (float) Math.toRadians(angleD);

        double deltaX = length * Math.cos(angle);
        double deltaY = length * Math.sin(angle);
        end = new Vector2f((float) (start.x + deltaX), (float) (start.y + deltaY));
        Vector2f directional = new Vector2f((float) -deltaY, (float) deltaX).mul(thickness);
        startUp = directional.add(start);
        Vector2f distance = new Vector2f(end).sub(start);
        endUp = new Vector2f(startUp).add(distance);

        dirty = true;
        return this;
    }

    public Line setLength(float length) {


        endUp.sub(startUp).div(this.length).mul(length).add(startUp);
        end.sub(endUp).div(this.length).mul(length).add(endUp);

        this.length = length;
        dirty = true;
        return this;
    }

    public Line setRGBA(float r, float g, float b,float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        dirty = true;
        return this;
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public int formerLength() {
        return 1;
    }

    public Vector2f getStart() {
        return start;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty(){
        dirty = true;
    }

    public void unMarkDirty(){
        dirty = false;
    }

    public void removed() {
        remove = false;
    }

    public Vector2f getEnd() {
        return end;
    }

    public Vector2f getEndUp() {
        return endUp;
    }

    public Vector2f getStartUp() {
        return startUp;
    }
}
