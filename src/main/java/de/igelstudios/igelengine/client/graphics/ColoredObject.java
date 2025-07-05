package de.igelstudios.igelengine.client.graphics;

/**
 * An interface that specifies that all Objects have a color value (r,g,b,NOT a) associated with them
 * @see AlphaColoredObject
 */
public interface ColoredObject {
    ColoredObject setColor(float r,float g,float b);

    float getR();
    float getG();
    float getB();
}
