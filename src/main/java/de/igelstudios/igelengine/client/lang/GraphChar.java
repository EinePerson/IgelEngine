package de.igelstudios.igelengine.client.lang;

import de.igelstudios.igelengine.client.graphics.batch.BatchContent;
import de.igelstudios.igelengine.client.graphics.text.GLFont;
import org.joml.Vector2f;

public class GraphChar implements BatchContent {
    private float r,g,b;
    private Vector2f pos;
    private float scale;
    private GLFont font;
    private int lifeTime;
    private boolean dirty;
    private  boolean remove;
    private final char chat;

    public GraphChar(char chat,Vector2f pos,int lifeTime,float scale,float r,float g,float b,GLFont font){
        this.chat = chat;
        this.pos = pos;
        this.lifeTime = lifeTime;
        this.scale = scale;
        this.r = r;
        this.g = g;
        this.b = b;
        this.font = font;
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public int formerLength() {
        return 1;
    }

    public void remove(){
        remove = true;
    }

    public void unMarkDirty(){
        dirty = false;
    }

    public void markDirty(){
        dirty = true;
    }

    public boolean hasChanged() {
        return dirty;
    }

    public Vector2f getPos() {
        return pos;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public GLFont getFont() {
        return font;
    }

    public char getChat() {
        return chat;
    }

    public float getScale() {
        return scale;
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

    public boolean life(){
        return lifeTime != 0;
    }

    void setColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    void setScale(float scale) {
        this.scale = scale;
    }

    void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    void setPos(Vector2f pos) {
        this.pos = pos;
    }

    void setFont(GLFont font) {
        this.font = font;
    }

    public void decrement(){
        if(lifeTime > 0)lifeTime--;
    }

    public boolean toRemove() {
        return remove;
    }

    @Override
    public String toString() {
        return "GraphChar{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", pos=" + pos +
                ", scale=" + scale +
                ", font=" + font +
                ", lifeTime=" + lifeTime +
                ", dirty=" + dirty +
                ", remove=" + remove +
                ", chat=" + chat +
                '}';
    }
}
