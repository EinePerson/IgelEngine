package de.igelstudios.igelengine.client.graphics;

/**
 * An interface that specifies that all objects have a color value associated with them (r,g,b,a)
 * @see ColoredObject
 */
public interface AlphaColoredObject extends ColoredObject{
    AlphaColoredObject setRGBA(float r, float g, float b, float a);
    default AlphaColoredObject setRGBA(int hex){
        return setRGBA(((hex >> 24) & 0xFF) / 255.0f,((hex >> 16) & 0xFF) / 255.0f,((hex >> 8) & 0xFF) / 255.0f,(hex & 0xFF) / 255.0f);
    }

    float getA();
}
