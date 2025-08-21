package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.common.util.Numbers;

/**
 * An interface that specifies that all Objects have a color value (r,g,b,NOT a) associated with them
 * @see AlphaColoredObject
 */
public interface ColoredObject {
    ColoredObject setColor(float r,float g,float b);
    default ColoredObject setColor(int hex){
        return setColor(((hex >> 16) & 0xFF) / 255.0f,((hex >> 8) & 0xFF) / 255.0f,(hex & 0xFF) / 255.0f);
    }

    default ColoredObject setInterpolatedColor(float r1, float g1, float b1, float r2, float g2, float b2,float weight){
        return setColor(Numbers.interpolate(r1,r2,weight),Numbers.interpolate(g1,g2,weight),Numbers.interpolate(b1,b2,weight));
    }

    default ColoredObject setInterpolatedColored(int hex1,int hex2,float weight){
        return setInterpolatedColor(((hex1 >> 16) & 0xFF) / 255.0f,((hex1 >> 8) & 0xFF) / 255.0f,(hex1 & 0xFF) / 255.0f,
                ((hex2 >> 16) & 0xFF) / 255.0f,((hex2 >> 8) & 0xFF) / 255.0f,(hex2 & 0xFF) / 255.0f,weight);
    }

    float getR();
    float getG();
    float getB();
}
