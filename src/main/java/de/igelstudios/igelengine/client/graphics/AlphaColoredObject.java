package de.igelstudios.igelengine.client.graphics;

/**
 * An interface that specifies that all objects have a color value associated with them (r,g,b,a)
 * @see ColoredObject
 */
public interface AlphaColoredObject extends ColoredObject{
    AlphaColoredObject setRGBA(float r, float g, float b, float a);

    float getA();
}
