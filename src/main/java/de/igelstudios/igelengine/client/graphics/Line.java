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
    private boolean mirror;

    private Line(Vector2f start){
        this.start = start;
        dirty = true;
    }

    public Line(Vector2f start, Vector2f end,float thickness) {
        this(start, (float) Math.toDegrees(new Vector2f(end).sub(start).angle(new Vector2f(1,0))),(float) Math.sqrt(Math.pow(end.x - start.x,2) + Math.pow(end.y - start.y,2)),thickness,
                false,0,0,0,1);
    }

    public Line(Vector2f start,float angleD,float length,float thickness,boolean mirror){
        this(start,angleD,length,thickness,mirror,0,0,0,1);
    }

    public Line(Vector2f start,float angleD,float length,float thickness,boolean mirror,float r,float g,float b,float a) {
        this.start = new Vector2f(start);
        this.angle = (float) Math.toRadians(angleD);
        this.length = length;
        this.thickness = thickness;

        this.mirror = mirror;

        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        recalculate();

        dirty = true;
    }

    public Line cloneFromEnd(float angleD,float length,float thickness,boolean mirror){
        Line clone = new Line(new Vector2f(end));
        clone.endUp = new Vector2f(endUp);
        clone.end = new Vector2f(end);
        clone.startUp = new Vector2f(startUp);
        boolean rec = false;
        if(this.angle != Math.toRadians(angleD)) rec = true;
        clone.angle = (float) Math.toRadians(angleD);

        if(this.length != length) rec = true;
        clone.length = length;

        if(this.thickness != thickness)rec = true;
        clone.thickness = thickness;

        if(this.mirror != mirror) rec = true;
        clone.mirror = mirror;

        clone.r = r;
        clone.g = g;
        clone.b = b;
        clone.a = a;
        if(rec){
            clone.recalculate();
        }

        return clone;
    }

    public Line cloneMoved(Vector2f deltaPox){
        return cloneMoved(deltaPox.x,deltaPox.y);
    }

    public Line cloneMoved(float deltaX, float deltaY){
        Line clone = clone();
        clone.start.x += deltaX;
        clone.start.y += deltaY;
        clone.end.x += deltaX;
        clone.end.y += deltaY;
        clone.startUp.x += deltaX;
        clone.startUp.y += deltaY;
        clone.endUp.x += deltaX;
        clone.endUp.y += deltaY;

        return clone;
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

    private void recalculate(){
        double deltaX = length * Math.cos(angle);
        double deltaY = length * Math.sin(angle);
        end = new Vector2f((float) (start.x + deltaX), (float) (start.y + deltaY));

        Vector2f directional = (mirror ?new Vector2f((float) deltaY, (float) -deltaX):new Vector2f((float) -deltaY, (float) deltaX)).normalize().mul(thickness);
        startUp = directional.add(start);
        endUp = new Vector2f(startUp).add(end).sub(start);

        dirty = true;
    }

    public Line mirror(boolean mirror) {
        if(this.mirror == mirror)return this;
        this.mirror = mirror;

        recalculate();

        return this;
    }

    public Line setThickness(float thickness) {
        if(thickness == this.thickness)return this;
        this.thickness = thickness;

        endUp.sub(end).mul(thickness).add(end);
        startUp.sub(start).mul(thickness).add(start);

        dirty = true;
        return this;
    }

    public Line setIncline(float angleD) {
        if((float) Math.toRadians(angleD) == this.angle)return this;
        this.angle = (float) Math.toRadians(angleD);

        recalculate();

        return this;
    }

    public Line setLength(float length) {
        if(this.length == length)return this;

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

    @Override
    public Line clone(){
        Line clone = new Line(new Vector2f(start));
        clone.end = new  Vector2f(end);
        clone.startUp = new  Vector2f(startUp);
        clone.endUp = new  Vector2f(endUp);

        clone.length = this.length;
        clone.thickness = this.thickness;
        clone.mirror = this.mirror;
        clone.angle = this.angle;

        clone.r = this.r;
        clone.g = this.g;
        clone.b = this.b;
        clone.a = this.a;

        return clone;
    }
}
