package de.igelstudios.igelengine.client.graphics;

public interface AlphaColoredObject extends ColoredObject{
    AlphaColoredObject setRGBA(float r, float g, float b, float a);

    float getA();
}
