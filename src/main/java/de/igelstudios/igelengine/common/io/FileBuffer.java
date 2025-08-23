package de.igelstudios.igelengine.common.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBuffer {
    private byte[] buffer;
    private final Path path;
    private int writerIndex;
    private int readerIndex;
    private boolean empty;

    public FileBuffer(Path path) {
        this.path = path;
        buffer = new byte[1024];
    }

    public FileBuffer(String path) {
        this(Paths.get(path));
    }

    private void writeCheck(int bytesToWrite) {
        if(bytesToWrite + writerIndex >= buffer.length){
            byte[] data = new byte[buffer.length * 2];
            System.arraycopy(buffer, 0, data, 0, buffer.length);
            buffer = data;
        }
    }

    private void readCheck(int bytesToRead) {
        if(bytesToRead + readerIndex > buffer.length){
            throw new IllegalArgumentException("Exceeded file buffer length while reading; Tried to read an additional " + (bytesToRead + readerIndex - buffer.length) + " bytes from the file with a length of " + buffer.length);
        }
    }

    public void writeInt(int value) {
        writeCheck(4);
        buffer[writerIndex++] = (byte) ((value >> 24) & 0xFF);
        buffer[writerIndex++] = (byte) ((value >> 16) & 0xFF);
        buffer[writerIndex++] = (byte) ((value >> 8) & 0xFF);
        buffer[writerIndex++] = (byte) ((value) & 0xFF);
    }

    public int readInt(){
        readCheck(4);
        int value = (buffer[readerIndex++] & 0xFF) << 24;
        value |= (buffer[readerIndex++] & 0xFF) << 16;
        value |= (buffer[readerIndex++] & 0xFF) << 8;
        value |= (buffer[readerIndex++] & 0xFF);
        return value;
    }

    public void writeLong(long value) {
        writeCheck(8);
        buffer[writerIndex++] = (byte) ((value >> 56) & 0xFF);
        buffer[writerIndex++] = (byte) ((value >> 48) & 0xFF);
        buffer[writerIndex++] = (byte) ((value >> 40) & 0xFF);
        buffer[writerIndex++] = (byte) ((value >> 32) & 0xFF);
        buffer[writerIndex++] = (byte) ((value >> 24) & 0xFF);
        buffer[writerIndex++] = (byte) ((value >> 16) & 0xFF);
        buffer[writerIndex++] = (byte) ((value >> 8) & 0xFF);
        buffer[writerIndex++] = (byte) ((value) & 0xFF);
    }

    public long readLong(){
        readCheck(8);
        long value = (long) (buffer[readerIndex++] & 0xFF) << 56;
        value |= (long) (buffer[readerIndex++] & 0xFF) << 48;
        value |= (long) (buffer[readerIndex++] & 0xFF) << 40;
        value |= (long) (buffer[readerIndex++] & 0xFF) << 32;
        value |= (long) (buffer[readerIndex++] & 0xFF) << 24;
        value |= (buffer[readerIndex++] & 0xFF) << 16;
        value |= (buffer[readerIndex++] & 0xFF) << 8;
        value |= (buffer[readerIndex++] & 0xFF);

        readerIndex += 8;
        return value;
    }

    public void writeFloat(float value) {
        writeCheck(4);
        writeInt(Float.floatToIntBits(value));
    }

    public float readFloat(){
        readCheck(4);
        return Float.intBitsToFloat(readInt());
    }

    public void writeString(String value) {
        byte[] bytes = value.getBytes();
        writeCheck(4 + bytes.length);
        writeInt(bytes.length);
        System.arraycopy(bytes, 0, buffer, writerIndex, bytes.length);
        writerIndex += bytes.length;
    }

    public String readString(){
        int length = readInt();
        byte[] bytes = new byte[length];
        System.arraycopy(buffer, readerIndex, bytes, 0, length);
        readerIndex += length;
        return new String(bytes);
    }

    public void writeStringList(List<String> value) {
        writeCheck(4);
        writeInt(value.size());
        value.forEach(this::writeString);
    }

    public List<String> readStringList() {
        writeCheck(4);
        int length = readInt();
        ArrayList<String> result = new ArrayList<>(length);
        for(int i = 0; i < length; i++){
            result.add(readString());
        }
        return result;
    }

    public void write() throws IOException {
        byte[] croppedBuffer = new byte[writerIndex];
        System.arraycopy(buffer, 0, croppedBuffer, 0, writerIndex);
        Files.write(path,croppedBuffer);
    }

    public void read() throws IOException {
        empty = !Files.exists(path);
        if(!empty) buffer = Files.readAllBytes(path);
    }

    public boolean isEmpty() {
        return empty;
    }
}
