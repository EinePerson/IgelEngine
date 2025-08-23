package de.igelstudios.igelengine.common.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * A util class to convert numbers between different formats
 */
public class Numbers {
    private Numbers(){

    }

    public static byte[] toBytes(int i){
        return ByteBuffer.allocate(4).putInt(i).array();
    }

    public static byte[] toBytes(float f){
        return ByteBuffer.allocate(4).putFloat(f).array();
    }

    public static int toInt(byte[] b){
        if(b.length > 4)throw new IllegalArgumentException("An Integer only consist of 4 bytes");
        return ByteBuffer.wrap(b).getInt();
    }

    public static float toFloat(byte[] b){
        if(b.length > 4)throw new IllegalArgumentException("An Float only consist of 4 bytes");
        return ByteBuffer.wrap(b).getFloat();
    }

    public static float interpolate(float start,float end,float weight){
        if(weight < 0 || weight > 1) throw new IllegalArgumentException("The weight must be between 0 and 1");
        return (end - start) * weight + start;
    }
}
