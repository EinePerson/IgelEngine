package de.igelstudios.igelengine.client.graphics;

/**
 * An interface that specifies that all Objects have a color value (r,g,b,NOT a) associated with them
 * @see AlphaColoredObject
 */
public interface ColoredObject {
    ColoredObject setColor(float r,float g,float b);
    default ColoredObject setColor(int hex){
        return setColor(((hex >> 16) & 0xFF) / 255.0f,((hex >> 8) & 0xFF) / 255.0f,(hex & 0xFF) / 255.0f);
    }

    float getR();
    float getG();
    float getB();
}
