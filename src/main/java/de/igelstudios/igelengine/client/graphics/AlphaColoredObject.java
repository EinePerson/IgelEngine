package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.common.util.Numbers;

/**
 * An interface that specifies that all objects have a color value associated with them (r,g,b,a)
 * @see ColoredObject
 */
public interface AlphaColoredObject extends ColoredObject{
    AlphaColoredObject setRGBA(float r, float g, float b, float a);
    default AlphaColoredObject setRGBA(int hex){
        return setRGBA(((hex >> 24) & 0xFF) / 255.0f,((hex >> 16) & 0xFF) / 255.0f,((hex >> 8) & 0xFF) / 255.0f,(hex & 0xFF) / 255.0f);
    }

    default AlphaColoredObject setInterpolatedRGBA(float r1, float g1, float b1, float a1, float r2, float g2, float b2,float a2,float weight){
        return setRGBA(Numbers.interpolate(r1,r2,weight),Numbers.interpolate(g1,g2,weight),Numbers.interpolate(b1,b2,weight),Numbers.interpolate(a1,a2,weight));
    }

    default AlphaColoredObject setInterpolatedRGBA(int hex1,int hex2,float weight){
        return setInterpolatedRGBA(((hex1 >> 24) & 0xFF) / 255.0f,((hex1 >> 16) & 0xFF) / 255.0f,((hex1 >> 8) & 0xFF) / 255.0f,(hex1 & 0xFF) / 255.0f,
                ((hex2 >> 24) & 0xFF) / 255.0f,((hex2 >> 16) & 0xFF) / 255.0f,((hex2 >> 8) & 0xFF) / 255.0f,(hex2 & 0xFF) / 255.0f,weight);
    }

    float getA();

    default int getRGBA(){
        return ((int) (getR() * 255) << 24) | ((int) (getG() * 255) << 16) | ((int) (getB() * 255) << 8) | ((int) (getA() * 255));
    }
}
