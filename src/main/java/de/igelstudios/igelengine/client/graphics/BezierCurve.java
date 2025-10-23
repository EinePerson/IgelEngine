package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.graphics.batch.BatchContent;
import de.igelstudios.igelengine.common.util.Numbers;
import org.joml.Vector2f;

import java.util.Arrays;

public class BezierCurve implements BatchContent,AlphaColoredObject {
    private boolean[] dirty;
    private Vector2f start,controlPoint,end;
    private float r,g,b,a;
    private boolean toRemove;

    /**
     *
     * @param start
     * @param controlPoint
     * @param end
     * @param r
     * @param g
     * @param b
     * @param a
     */
    public BezierCurve(Vector2f start,Vector2f controlPoint,Vector2f end,float r,float g,float b,float a) {
        dirty = new boolean[ClientEngine.getWindowCount()];
        this.start = new Vector2f(start);
        this.controlPoint = new Vector2f(controlPoint);
        this.end = new Vector2f(end);

        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Constructs a new Bezier spanning from start to end spanning in a quadratic way to an upper-left corner
     * @param start the start value
     * @param end the start value
     * @param r the red value
     * @param g the green value
     * @param b the blue value
     * @param a the alpha value
     */
    public BezierCurve(Vector2f start,Vector2f end,float r,float g,float b,float a) {
        this(true,start,end,r,g,b,a);
    }

    /**
     * Constructs a new Bezier spanning from start to end spanning in a quadratic way to an upper-left corner if start is true,else the lower left one
     * @param start the start value
     * @param end the start value
     * @param r the red value
     * @param g the green value
     * @param b the blue value
     * @param a the alpha value
     */
    public BezierCurve(boolean side,Vector2f start,Vector2f end,float r,float g,float b,float a){
        this(start,new Vector2f(side?start.x:end.x,side? end.y : start.y),end,r,g,b,a);
    }

    /**
     * Constructs a new Bezier spanning from start to end spanning in a quadratic way to an upper-left corner if start is true,else the lower left one
     * @param start the start value
     * @param end the start value
     * @param r the red value
     * @param g the green value
     * @param b the blue value
     * @param a the alpha value
     */
    public BezierCurve(float controlPointDifferences,Vector2f start,Vector2f end,float r,float g,float b,float a){
        this(start,new Vector2f(Numbers.interpolate(start.x,end.x,controlPointDifferences),Numbers.interpolate(start.y,end.y,1 - controlPointDifferences)),end,r,g,b,a);
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public int formerLength() {
        return 1;
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
    public void markDirty() {
        Arrays.fill(dirty,true);
    }

    public boolean toRemove() {
        return toRemove;
    }

    public void remove(){
        toRemove = true;
    }

    @Override
    public AlphaColoredObject setRGBA(float r,float g,float b,float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        markDirty();

        return this;
    }

    @Override
    public float getA() {
        return a;
    }

    @Override
    public ColoredObject setColor(float r,float g,float b) {
        this.r = r;
        this.g = g;
        this.b = b;

        markDirty();

        return this;
    }

    @Override
    public float getR() {
        return r;
    }

    @Override
    public float getG() {
        return g;
    }

    @Override
    public float getB() {
        return b;
    }

    public Vector2f getStart() {
        return start;
    }

    public Vector2f getControlPoint() {
        return controlPoint;
    }

    public Vector2f getEnd() {
        return end;
    }

    public void removed(){
        toRemove = false;
    }

    public float getThickness(){
        return 1;
    }
}
