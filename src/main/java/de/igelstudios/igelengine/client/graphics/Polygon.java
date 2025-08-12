package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.graphics.batch.BatchContent;
import org.joml.Vector2f;

import java.util.Arrays;

/**
 * This represents a polygon spanning all the coords with a uniform color
 * @see #fromLines(Line...)
 */
public class Polygon implements BatchContent,AlphaColoredObject {
    private Vector2f[] coords;
    private float r,g,b,a;
    private boolean[] dirty;
    private boolean remove;

    /**
     * creates a new Polygon, colors from 0 to 1
     * @param coords all corners of the Polygon
     * @param r the red value
     * @param g the green value
     * @param b the blue value
     * @param a the alpha value
     * @see Polygon#Polygon(Polygon)  Polygon
     * @see Polygon#Polygon(Polygon)  Polygon
     */
    public Polygon(Vector2f[] coords,float r,float g,float b,float a) {
        this.coords = coords;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        dirty = new boolean[ClientEngine.getWindowCount()];
        markDirty();
    }

    /**
     * Copy constructor
     * @param polygon the Polygon to copy
     * @see #Polygon(Vector2f[], float, float, float, float)  Polygon
     * @see #Polygon(Vector2f...)  Polygon
     */
    public Polygon(Polygon polygon){
        coords = new  Vector2f[polygon.coords.length];
        System.arraycopy(polygon.coords, 0, coords, 0, polygon.coords.length);
        r = polygon.r;
        g = polygon.g;
        b = polygon.b;
        a = polygon.a;

        dirty = new boolean[ClientEngine.getWindowCount()];
        markDirty();
    }

    /**
     * a polygon that spans the given corners with display color black
     * @param coords the corners to span
     */
    public Polygon(Vector2f ... coords){
        this(coords,0f,0f,0f,1.0f);
    }

    public Polygon moveBy(float deltaX, float deltaY){
        Vector2f delta = new Vector2f(deltaX, deltaY);

        for(Vector2f coord : coords){
            coord.add(delta);
        }

        markDirty();
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
    public Polygon setRGBA(float r,float g,float b,float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        markDirty();
        return this;
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

    @Override
    public boolean isDirty(int windowID) {
        return dirty[windowID];
    }

    @Override
    public void unMarkDirty(int windowID) {
        dirty[windowID] = false;
    }

     @Override
    public void markDirty(){
        Arrays.fill(dirty,true);
    }

    public Vector2f[] getCoords() {
        return coords;
    }

    /**
     * sets the colors, from 0 to 1
     * @param r the amount of red
     * @param g the amount of green
     * @param b the amount of blue
     * @return the Polygon for chained modification calls
     * @see #setRGBA(float, float, float, float)
     */
    @Override
    public ColoredObject setColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;

        markDirty();
        return this;
    }

    /**
     * sets the rgb values from a single integer
     * @param color the new color as single integer
     * @return this
     */
    public ColoredObject setColor(int color){
        this.r = ((color >> 16) & 0xFF) / 255.0f;
        this.g = ((color >> 8) & 0xFF) / 255.0f;
        this.b = (color & 0xFF) / 255.0f;

        markDirty();
        return this;
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

    /**
     * @return the amount of corners
     */
    @Override
    public int getLength() {
        return coords.length;
    }

    @Override
    public int formerLength() {
        return coords.length;
    }

    /**
     * This creates a Polygon which spans the ends of the given lines
     * @param lines the lines
     * @return the newly created polygon
     */
    public static Polygon fromLines(Line ... lines){
        Vector2f[] coords = new Vector2f[lines.length];
        //if(!(lines[0].getStart().equals(lines[lines.length - 1].getEnd()) || lines[0].getEnd().equals(lines[lines.length - 1].getStart())))throw new IllegalArgumentException("Lines in a polygon must connect");
        coords[0] = new Vector2f(lines[0].getEnd());
        for (int i = 1; i < lines.length; i++) {
            coords[i] = new Vector2f(lines[i].getEndOrg());
            //if(coords[i - 1].equals(lines[i].getStart())) coords[i] = new Vector2f(lines[i].getEnd());
            //else if(coords[i - 1].equals(lines[i].getEnd())) coords[i] = new Vector2f(lines[i].getStart());
            //else throw new IllegalArgumentException("Lines in a polygon must connect");
        }

        return new Polygon(coords);
    }

    /**
     * creates a new polygon and moves it by the given coordinates
     * @param x the x Offset
     * @param y the y Offset
     * @return the moved polygon
     */
    public Polygon getAtDelta(float x,float y){
        Polygon copy = new Polygon(this);
        for (Vector2f coord : copy.coords) {
            coord.x += x;
            coord.y += y;
        }
        return copy;
    }

    public void removed() {
        remove = false;
    }

    public void remove(){
        remove = true;
    }

    public boolean toRemove(){
        return remove;
    }
}
