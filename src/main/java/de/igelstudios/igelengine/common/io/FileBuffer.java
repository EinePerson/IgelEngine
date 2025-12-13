package de.igelstudios.igelengine.common.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBuffer {
    public static final int APPEND_FILE_WRITER_FLAG = 0b1;

    private byte[] buffer;
    private final Path path;
    private int writerIndex;
    private int readerIndex;
    private boolean empty;
    private final int version;
    private final int flags;

    public FileBuffer(Path path,int version){
        this(path, version,1024,0);
    }

    public FileBuffer(Path path,int version,int initialCapacity,int flags) {
        this.path = path;
        buffer = new byte[initialCapacity];
        this.version = version;
        this.flags = flags;
    }

    public FileBuffer(Path path) {
        this(path,-1);
    }

    public FileBuffer(String path) {
        this(Paths.get(path));
    }

    public FileBuffer(String path, int version) {
        this(Paths.get(path), version);
    }

    private void writeCheck(int bytesToWrite) {
        if(bytesToWrite + writerIndex >= buffer.length){
            int newSize = buffer.length * 2;
            while(bytesToWrite + writerIndex >= newSize)newSize *= 2;
            byte[] data = new byte[newSize];
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

    /**
     * Reads the specified file to the beginning of the buffer
     * @throws IOException if an I/O error occurs reading from the stream
     */
    public void read() throws IOException {
        empty = !Files.exists(path);
        if(!empty){
            byte[] fileContent = Files.readAllBytes(path);
            writeCheck(fileContent.length);
            System.arraycopy(fileContent,0,buffer,0,fileContent.length);
            if((flags & APPEND_FILE_WRITER_FLAG) != 0)writerIndex = fileContent.length;
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public int version(){
        return version;
    }
}
