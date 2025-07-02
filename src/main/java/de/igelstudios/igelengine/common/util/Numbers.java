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

    public static int toInt(byte[] b){
        if(b.length > 4)throw new IllegalArgumentException("An Integer only consist of 4 bytes");
        return ByteBuffer.wrap(b).getInt();
    }
}
