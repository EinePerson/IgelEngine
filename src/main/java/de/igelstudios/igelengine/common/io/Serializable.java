package de.igelstudios.igelengine.common.io;


public interface Serializable extends SerializerKnown{
    int serializedByteCount(int version);
    void write(FileBuffer buffer);
    void setFromBytes(FileBuffer buffer);
}
