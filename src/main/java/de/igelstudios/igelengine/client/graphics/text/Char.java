package de.igelstudios.igelengine.client.graphics.text;

import org.joml.Vector2f;

import java.util.Arrays;

public class Char {
    private int x,y,z,w;
    private Vector2f[] texCords;

    public Char(int x,int y,int z,int w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void calcCords(int fW,int fH){
        texCords = new Vector2f[2];
        float x0 = (float)x / (float)fW;
        float x1 = (float)(x + z) / (float)fW;
        float y0 = (float)(y - w) / (float)fH;
        float y1 = (float)(this.y) / (float)fH;

        texCords[0] = new Vector2f(x0, y1);
        texCords[1] = new Vector2f(x1, y0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWith(){
        return z;
    }

    public int getHeight() {
        return w;
    }

    public Vector2f[] getTexCords() {
        return texCords;
    }

    @Override
    public String toString() {
        return "Char{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                ", texCords=" + Arrays.toString(texCords) +
                '}';
    }
}
