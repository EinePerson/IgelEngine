package de.igelstudios.igelengine.client.lang;

import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.graphics.batch.BatchContent;
import de.igelstudios.igelengine.client.graphics.text.Char;
import de.igelstudios.igelengine.client.graphics.text.GLFont;
import org.joml.Vector2f;

import java.util.Arrays;

/**
 * This is the Graphical implementation of {@link Text}<br>
 * DO NOT USE THIS ON YOUR OWN
 */
public class GraphChar implements BatchContent {
    private float r,g,b,a;
    private Vector2f pos;
    private float scale;
    private GLFont font;
    private int lifeTime;
    private boolean[] dirty = new boolean[ClientEngine.getWindowCount()];
    private  boolean remove;
    private char chat;

    public GraphChar(char chat,Vector2f pos,int lifeTime,float scale,float r,float g,float b,GLFont font){
        this.chat = chat;
        this.pos = pos;
        this.lifeTime = lifeTime;
        this.scale = scale;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1.0f;
        this.font = font;
    }

    void setChat(char chat) {
        this.chat = chat;

        markDirty();
    }

    public Char getChar(){
        return font.get(chat);
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

    @Override
    public void unMarkDirty(int windowID){
        dirty[windowID] = false;
    }

    @Override
    public void markDirty(){
        Arrays.fill(dirty,true);
    }

    @Override
    public boolean isDirty(int windowID) {
        return dirty[windowID];
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

        markDirty();
    }

    void setColor(float r, float g, float b,float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        markDirty();
    }

    void setA(float a) {
        this.a = a;

        markDirty();
    }

    void setScale(float scale) {
        this.scale = scale;
        markDirty();
    }

    void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    void setPos(Vector2f pos) {
        this.pos = pos;

        markDirty();
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

    public float getA() {
        return a;
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
