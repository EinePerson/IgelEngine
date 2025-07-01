package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.client.graphics.batch.BatchContent;
import org.joml.Vector2f;

import java.text.DecimalFormat;

public class Line implements BatchContent,AlphaColoredObject {
    public enum Type{
        RIGHT,
        CENTER,
        LEFT;
    }
    private final Vector2f org;
    private Vector2f endOrg;
    private Vector2f start;
    private Vector2f startUp;
    private Vector2f end;
    private Vector2f endUp;
    private float length;
    private float angle;
    private float thickness;
    private float r,g,b,a;
    private boolean dirty;
    private boolean remove;
    private Type mirror;

    private Line(Vector2f start){
        this.org = start;
        dirty = true;
    }

    public Line(Vector2f start, Vector2f end,float thickness) {
        this(start, (float) -Math.toDegrees(new Vector2f(end).sub(start).angle(new Vector2f(1,0))),(float) Math.sqrt(Math.pow(end.x - start.x,2) +
                Math.pow(end.y - start.y,2)),thickness,Type.LEFT,0,0,0,1);
    }

    public Line(Vector2f start,float angleD,float length,float thickness,Type mirror){
        this(start,angleD,length,thickness,mirror,0,0,0,1);
    }

    public Line(Vector2f start,float angleD,float length,float thickness,Type mirror,float r,float g,float b,float a) {
        this.org = new Vector2f(start);
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

    public Line cloneFromStart(float angleD,float length,float thickness,Type mirror){
        return cloneFromStart(angleD,length,thickness,mirror,0,0,0,1);
    }

    public Line cloneFromStart(float angleD,float length,float thickness,Type mirror,float r,float g,float b,float a) {
        Line clone = new Line(new Vector2f(mirror == Type.CENTER?org:start));
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

    public Line spanStartToEnd(Line other){
        return spanHelper(this.start,this.org,other.end,other.org);
    }

    public Line spanStartToStart(Line other){
        return spanHelper(this.start,this.org,other.start,other.org);
    }

    public Line spanEndToStart(Line other){
        Vector2f org = switch (mirror) {
            case CENTER -> new Vector2f(end).sub(endUp).normalize().mul(thickness / 2).add(endUp);
            case LEFT,RIGHT -> new Vector2f(this.end);
        };
        return spanHelper(this.end,this.endOrg,other.start,other.org);
    }

    public Line spanEndToEnd(Line other){
        Vector2f org = switch (mirror) {
            case CENTER -> new Vector2f(end).sub(endUp).normalize().mul(thickness / 2).add(endUp);
            case LEFT,RIGHT -> new Vector2f(this.end);
        };
        return spanHelper(this.end,this.endOrg,other.end,other.endOrg);
    }

    private Line spanHelper(Vector2f start,Vector2f org,Vector2f end,Vector2f orgEnd){
        Line clone = new Line(new Vector2f(org));

        float angle = (float) -Math.toDegrees(new Vector2f(end).sub(start).angle(new Vector2f(1,0)));
        clone.angle = angle;

        clone.length = (float) Math.sqrt(Math.pow(end.x - start.x,2) +
                Math.pow(end.y - start.y,2));

        clone.thickness = thickness;

        clone.mirror = mirror;

        clone.r = r;
        clone.g = g;
        clone.b = b;
        clone.a = a;

        clone.recalculate();

        return clone;
    }

    public Line cloneFromEnd(float angleD,float length,float thickness,Type mirror){
        return cloneFromEnd(angleD,length,thickness,mirror,0,0,0,1);
    }

    public Line cloneFromEnd(float angleD,float length,float thickness,Type mirror,float r,float g,float b,float a) {
        Line clone = new Line(new Vector2f(mirror == Type.CENTER?endOrg:end));
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
        clone.org.x += deltaX;
        clone.org.y += deltaY;
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

    @Override
    public Line setColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        dirty = true;
        return this;
    }

    public float getR() {
        return r;
    }

    private void recalculate(){
        float deltaX = Math.round(length * (float) Math.cos(angle) * 100000) / 100000.0f;
        float deltaY = Math.round(length * (float) Math.sin(angle) * 100000) / 100000.0f;
        if(mirror != Type.CENTER){
            start = new Vector2f(org);
        }else{
            Vector2f directional = new Vector2f((float) deltaY, (float) -deltaX).normalize().mul(0.5f);
            start = directional.mul(thickness).add(org);
        }
        end = new Vector2f((float) (start.x + deltaX), (float) (start.y + deltaY));
        endOrg = new Vector2f((float) (org.x + deltaX), (float) (org.y + deltaY));

        Vector2f directional = (mirror == Type.RIGHT ? new Vector2f((float) deltaY, (float) -deltaX) : new Vector2f((float) -deltaY, (float) deltaX)).normalize().mul(thickness);
        startUp = directional.add(start);
        endUp = new Vector2f(startUp).add(end).sub(start);

        dirty = true;
    }

    public Line setType(Type mirror) {
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

    public Vector2f getEndOrg() {
        return endOrg;
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

    public Line mirrored(){
        return new Line(new Vector2f(end),(float) Math.toDegrees(-angle),length,thickness,mirror,r,g,b,a);
    }

    public Vector2f getOrg() {
        return org;
    }

    public void setA(float a) {
        this.a = a;
        markDirty();
    }
}
