package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.client.graphics.batch.BatchContent;
import org.joml.Vector2f;

import java.text.DecimalFormat;

/**
 * This represents a simple line
 */
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

    /**
     * creates a line spanning from start to end
     * @param start the start point of the line
     * @param end the end point of the line
     * @param thickness the thickness the line should have
     * @see  #Line(Vector2f, float, float, float, Type)  Line
     * @see #Line(Vector2f, float, float, float, Type, float, float, float, float)  Line
     */
    public Line(Vector2f start, Vector2f end,float thickness) {
        this(start, (float) -Math.toDegrees(new Vector2f(end).sub(start).angle(new Vector2f(1,0))),(float) Math.sqrt(Math.pow(end.x - start.x,2) +
                Math.pow(end.y - start.y,2)),thickness,Type.CENTER,0,0,0,1);
    }

    /**
     * creates a new simple line in black
     * @param start the start point of the line
     * @param angleD the angle relative to the x-Axis/lower side of the screen in degrees
     * @param length the length the line should have
     * @param thickness the thickness the line should have
     * @param mirror in which direction relative to the virtual line given here the visuallity should be expanded
     * @see #Line(Vector2f, float, float, float, Type, float, float, float, float)  Line
     * @see #Line(Vector2f, Vector2f, float)  Line
     */
    public Line(Vector2f start,float angleD,float length,float thickness,Type mirror){
        this(start,angleD,length,thickness,mirror,0,0,0,1);
    }

    /**
     * creates a new simple line with the given color between 0 and 1
     * @param start the start point of the line
     * @param angleD the angle relative to the x-Axis/lower side of the screen in degrees
     * @param length the length the line should have
     * @param thickness the thickness the line should have
     * @param mirror in which direction relative to the virtual line given here the visuallity should be expanded
     * @param r the amount of red
     * @param g the amount of green
     * @param b the amount of blue
     * @param a the alpha value
     * @see #Line(Vector2f, float, float, float, Type, float, float, float, float)  Line
     * @see #Line(Vector2f, Vector2f, float)  Line
     */
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

    /**
     * Draws a line from this Lines start point in black
     * @param angleD the angle relative to the x-Axis/lower side of the screen in degrees
     * @param length the length of the line
     * @param thickness the thickness of the line
     * @param mirror in which direction relative to the virtual line given here the visuallity should be expanded
     * @return a new line from this lines start point
     * @see #cloneFromStart(float, float, float, Type, float, float, float, float)
     */
    public Line cloneFromStart(float angleD,float length,float thickness,Type mirror){
        return cloneFromStart(angleD,length,thickness,mirror,0,0,0,1);
    }

    /**
     * Draws a line from this Lines start point in black
     * @param angleD the angle relative to the x-Axis/lower side of the screen in degrees
     * @param length the length of the line
     * @param thickness the thickness of the line
     * @param mirror in which direction relative to the virtual line given here the visuallity should be expanded
     * @param r the amount of red
     * @param g the amount of green
     * @param b the amount of blue
     * @param a the alpha value
     * @return a new line from this lines start point
     * @see #cloneFromStart(float, float, float, Type)
     */
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

    /**
     * creates a line from this lines start to the other lines end
     * @param other the line to span to
     * @return the newly created line
     */
    public Line spanStartToEnd(Line other){
        return spanHelper(this.start,this.org,other.end,other.org);
    }

    /**
     * creates a line from this lines start to the other lines start
     * @param other the line to span to
     * @return the newly created line
     */
    public Line spanStartToStart(Line other){
        return spanHelper(this.start,this.org,other.start,other.org);
    }

    /**
     * creates a line from this lines end to the other lines start
     * @param other the line to span to
     * @return the newly created line
     */
    public Line spanEndToStart(Line other){
        Vector2f org = switch (mirror) {
            case CENTER -> new Vector2f(end).sub(endUp).normalize().mul(thickness / 2).add(endUp);
            case LEFT,RIGHT -> new Vector2f(this.end);
        };
        return spanHelper(this.end,this.endOrg,other.start,other.org);
    }

    /**
     * creates a line from this lines end to the other lines end
     * @param other the line to span to
     * @return the newly created line
     */
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

    /**
     * Draws a line from this Lines end point in black
     * @param angleD the angle relative to the x-Axis/lower side of the screen in degrees
     * @param length the length of the line
     * @param thickness the thickness of the line
     * @param mirror in which direction relative to the virtual line given here the visuallity should be expanded
     * @return a new line from this lines start point
     * @see #cloneFromEnd(float, float, float, Type, float, float, float, float)
     */
    public Line cloneFromEnd(float angleD,float length,float thickness,Type mirror){
        return cloneFromEnd(angleD,length,thickness,mirror,0,0,0,1);
    }

    /**
     * Draws a line from this Lines end point in black
     * @param angleD the angle relative to the x-Axis/lower side of the screen in degrees
     * @param length the length of the line
     * @param thickness the thickness of the line
     * @param mirror in which direction relative to the virtual line given here the visuallity should be expanded
     * @param r the amount of red
     * @param g the amount of green
     * @param b the amount of blue
     * @param a the alpha value
     * @return a new line from this lines start point
     * @see #cloneFromEnd(float, float, float, Type)
     */
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

    /**
     * creates a clone that is move by the given Vector
     * @param deltaPox the amount to move by
     * @return the newly created line
     * @see #cloneMoved(float, float)
     */
    public Line cloneMoved(Vector2f deltaPox){
        return cloneMoved(deltaPox.x,deltaPox.y);
    }

    /**
     * creates a clone that is move by the given Vector
     * @param deltaX the amount to move by in x direction
     * @param deltaY the amount to move by in y direction
     * @return the newly created line
     * @see #cloneMoved(Vector2f)
     */
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

    /**
     * sets the color of the line
     * @param r the amount of red
     * @param g the amount of green
     * @param b the amount of blue
     * @return the Line for chained modification calls
     * @see #setRGBA(float, float, float, float)
     */
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

    /**
     * redraws the line with the set type
     * @param mirror in which direction relative to the virtual line given here the visuallity should be expanded
     * @return the line for chained modification calls
     */
    public Line setType(Type mirror) {
        if(this.mirror == mirror)return this;
        this.mirror = mirror;

        recalculate();

        return this;
    }

    /**
     * redraws the line with the set thickness
     * @param thickness the thickness of the line
     * @return the line for chained modification calls
     */
    public Line setThickness(float thickness) {
        if(thickness == this.thickness)return this;
        this.thickness = thickness;

        endUp.sub(end).mul(thickness).add(end);
        startUp.sub(start).mul(thickness).add(start);

        dirty = true;
        return this;
    }

    /**
     * redraws the line with the set angle
     * @param angleD the angle relative to the x-Axis/lower side of the screen in degrees
     * @return the line for chained modification calls
     */
    public Line setIncline(float angleD) {
        if((float) Math.toRadians(angleD) == this.angle)return this;
        this.angle = (float) Math.toRadians(angleD);

        recalculate();

        return this;
    }

    /**
     * redraws the line with the set length
     * @param length the length of the line
     * @return the line for chained modification calls
     */
    public Line setLength(float length) {
        if(this.length == length)return this;

        endUp.sub(startUp).div(this.length).mul(length).add(startUp);
        end.sub(endUp).div(this.length).mul(length).add(endUp);

        this.length = length;
        dirty = true;
        return this;
    }

    /**
     * sets the colors, from 0 to 1
     * @param r the amount of red
     * @param g the amount of green
     * @param b the amount of blue
     * @param a the alpha value
     * @return the Polygon for chained modification calls
     * @see #setColor(float, float, float)
     */
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

    /**
     * mirrors the line
     * @return a new line going from end to start of this line
     */
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
