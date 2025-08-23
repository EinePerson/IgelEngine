package de.igelstudios.igelengine.common.io;


public interface Serializable extends SerializerKnown{
    int serializedByteCount();
    void write(byte[] data,int index);
    void setFromBytes(byte[] data,int index);
}
