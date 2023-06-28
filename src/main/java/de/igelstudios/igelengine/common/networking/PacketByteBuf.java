package de.igelstudios.igelengine.common.networking;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketByteBuf extends ByteBuf {
    public static PacketByteBuf create(){
        return new PacketByteBuf(Unpooled.buffer());
    }

    private final ByteBuf supper;
    public PacketByteBuf(ByteBuf supper){
        this.supper = supper;
    }

    @Override
    public int capacity() {
        return supper.capacity();
    }

    @Override
    public ByteBuf capacity(int newCapacity) {
        return supper.capacity(newCapacity);
    }

    @Override
    public int maxCapacity() {
        return supper.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return supper.alloc();
    }

    @Override
    public ByteOrder order() {
        return supper.order();
    }

    @Override
    public ByteBuf order(ByteOrder endianness) {
        return supper.order(endianness);
    }

    @Override
    public ByteBuf unwrap() {
        return supper.unwrap();
    }

    @Override
    public boolean isDirect() {
        return supper.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return supper.isReadOnly();
    }

    @Override
    public ByteBuf asReadOnly() {
        return supper.asReadOnly();
    }

    @Override
    public int readerIndex() {
        return supper.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int readerIndex) {
        return supper.readerIndex(readerIndex);
    }

    @Override
    public int writerIndex() {
        return supper.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int writerIndex) {
        return supper.writerIndex(writerIndex);
    }

    @Override
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        return supper.setIndex(readerIndex,writerIndex);
    }

    @Override
    public int readableBytes() {
        return supper.readableBytes();
    }

    @Override
    public int writableBytes() {
        return supper.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return supper.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return supper.isReadable();
    }

    @Override
    public boolean isReadable(int size) {
        return supper.isReadable(size);
    }

    @Override
    public boolean isWritable() {
        return supper.isWritable();
    }

    @Override
    public boolean isWritable(int size) {
        return supper.isWritable();
    }

    @Override
    public ByteBuf clear() {
        return supper.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return supper.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return supper.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return supper.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return supper.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return supper.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return supper.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(int minWritableBytes) {
        return supper.ensureWritable(minWritableBytes);
    }

    @Override
    public int ensureWritable(int minWritableBytes, boolean force) {
        return supper.ensureWritable(minWritableBytes,force);
    }

    @Override
    public boolean getBoolean(int index) {
        return supper.getBoolean(index);
    }

    @Override
    public byte getByte(int index) {
        return supper.getByte(index);
    }

    @Override
    public short getUnsignedByte(int index) {
        return supper.getUnsignedByte(index);
    }

    @Override
    public short getShort(int index) {
        return supper.getShort(index);
    }

    @Override
    public short getShortLE(int index) {
        return supper.getShortLE(index);
    }

    @Override
    public int getUnsignedShort(int index) {
        return supper.getUnsignedShort(index);
    }

    @Override
    public int getUnsignedShortLE(int index) {
        return supper.getUnsignedShortLE(index);
    }

    @Override
    public int getMedium(int index) {
        return supper.getMedium(index);
    }

    @Override
    public int getMediumLE(int index) {
        return supper.getMediumLE(index);
    }

    @Override
    public int getUnsignedMedium(int index) {
        return supper.getUnsignedMedium(index);
    }

    @Override
    public int getUnsignedMediumLE(int index) {
        return supper.getUnsignedMediumLE(index);
    }

    @Override
    public int getInt(int index) {
        return supper.getInt(index);
    }

    @Override
    public int getIntLE(int index) {
        return supper.getIntLE(index);
    }

    @Override
    public long getUnsignedInt(int index) {
        return supper.getUnsignedInt(index);
    }

    @Override
    public long getUnsignedIntLE(int index) {
        return supper.getUnsignedIntLE(index);
    }

    @Override
    public long getLong(int index) {
        return supper.getLong(index);
    }

    @Override
    public long getLongLE(int index) {
        return supper.getLongLE(index);
    }

    @Override
    public char getChar(int index) {
        return supper.getChar(index);
    }

    @Override
    public float getFloat(int index) {
        return supper.getFloat(index);
    }

    @Override
    public double getDouble(int index) {
        return supper.getDouble(index);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst) {
        return supper.getBytes(index,dst);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int length) {
        return supper.getBytes(index,dst,length);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        return supper.getBytes(index,dst,dstIndex,length);
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst) {
        return supper.getBytes(index,dst);
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        return supper.getBytes(index,dst,dstIndex,length);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        return supper.getBytes(index,dst);
    }

    @Override
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        return supper.getBytes(index,out,length);
    }

    @Override
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        return supper.getBytes(index,out,length);
    }

    @Override
    public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        return supper.getBytes(index,out,position,length);
    }

    @Override
    public CharSequence getCharSequence(int index, int length, Charset charset) {
        return supper.getCharSequence(index,length,charset);
    }

    @Override
    public ByteBuf setBoolean(int index, boolean value) {
        return supper.setBoolean(index,value);
    }

    @Override
    public ByteBuf setByte(int index, int value) {
        return supper.setByte(index,value);
    }

    @Override
    public ByteBuf setShort(int index, int value) {
        return supper.setShort(index,value);
    }

    @Override
    public ByteBuf setShortLE(int index, int value) {
        return supper.setShortLE(index,value);
    }

    @Override
    public ByteBuf setMedium(int index, int value) {
        return supper.setMedium(index,value);
    }

    @Override
    public ByteBuf setMediumLE(int index, int value) {
        return supper.setMediumLE(index,value);
    }

    @Override
    public ByteBuf setInt(int index, int value) {
        return supper.setInt(index,value);
    }

    @Override
    public ByteBuf setIntLE(int index, int value) {
        return supper.setIntLE(index,value);
    }

    @Override
    public ByteBuf setLong(int index, long value) {
        return supper.setLong(index,value);
    }

    @Override
    public ByteBuf setLongLE(int index, long value) {
        return supper.setLong(index,value);
    }

    @Override
    public ByteBuf setChar(int index, int value) {
        return supper.setChar(index,value);
    }

    @Override
    public ByteBuf setFloat(int index, float value) {
        return supper.setFloat(index,value);
    }

    @Override
    public ByteBuf setDouble(int index, double value) {
        return supper.setDouble(index,value);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src) {
        return supper.setBytes(index,src);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        return supper.setBytes(index,src,length);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        return supper.setBytes(index,src,srcIndex,length);
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src) {
        return supper.setBytes(index,src);
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        return supper.setBytes(index,src,srcIndex,length);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuffer src) {
        return supper.setBytes(index,src);
    }

    @Override
    public int setBytes(int index, InputStream in, int length) throws IOException {
        return supper.setBytes(index,in,length);
    }

    @Override
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        return supper.setBytes(index,in,length);
    }

    @Override
    public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
        return supper.setBytes(index,in,position,length);
    }

    @Override
    public ByteBuf setZero(int index, int length) {
        return supper.setZero(index,length);
    }

    @Override
    public int setCharSequence(int index, CharSequence sequence, Charset charset) {
        return supper.setCharSequence(index,sequence,charset);
    }

    @Override
    public boolean readBoolean() {
        return supper.readBoolean();
    }

    @Override
    public byte readByte() {
        return supper.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return supper.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return supper.readShort();
    }

    @Override
    public short readShortLE() {
        return supper.readShortLE();
    }

    @Override
    public int readUnsignedShort() {
        return supper.readUnsignedShort();
    }

    @Override
    public int readUnsignedShortLE() {
        return supper.readUnsignedShortLE();
    }

    @Override
    public int readMedium() {
        return supper.readMedium();
    }

    @Override
    public int readMediumLE() {
        return supper.readMediumLE();
    }

    @Override
    public int readUnsignedMedium() {
        return supper.readUnsignedMedium();
    }

    @Override
    public int readUnsignedMediumLE() {
        return supper.readUnsignedMediumLE();
    }

    @Override
    public int readInt() {
        return supper.readInt();
    }

    @Override
    public int readIntLE() {
        return supper.readIntLE();
    }

    @Override
    public long readUnsignedInt() {
        return supper.readUnsignedInt();
    }

    @Override
    public long readUnsignedIntLE() {
        return supper.readUnsignedIntLE();
    }

    @Override
    public long readLong() {
        return supper.readLong();
    }

    @Override
    public long readLongLE() {
        return supper.readLongLE();
    }

    @Override
    public char readChar() {
        return supper.readChar();
    }

    @Override
    public float readFloat() {
        return supper.readFloat();
    }

    @Override
    public double readDouble() {
        return supper.readDouble();
    }

    @Override
    public ByteBuf readBytes(int length) {
        return supper.readBytes(length);
    }

    @Override
    public ByteBuf readSlice(int length) {
        return supper.readSlice(length);
    }

    @Override
    public ByteBuf readRetainedSlice(int length) {
        return supper.readRetainedSlice(length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst) {
        return supper.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int length) {
        return supper.readBytes(dst,length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        return supper.readBytes(dst,dstIndex,length);
    }

    @Override
    public ByteBuf readBytes(byte[] dst) {
        return supper.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        return supper.readBytes(dst,dstIndex,length);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer dst) {
        return supper.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        return supper.readBytes(out,length);
    }

    @Override
    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        return supper.readBytes(out,length);
    }

    @Override
    public CharSequence readCharSequence(int length, Charset charset) {
        return supper.readCharSequence(length,charset);
    }

    @Override
    public int readBytes(FileChannel out, long position, int length) throws IOException {
        return supper.readBytes(out,position,length);
    }

    @Override
    public ByteBuf skipBytes(int length) {
        return supper.skipBytes(length);
    }

    @Override
    public ByteBuf writeBoolean(boolean value) {
        return supper.writeBoolean(value);
    }

    @Override
    public ByteBuf writeByte(int value) {
        return supper.writeByte(value);
    }

    @Override
    public ByteBuf writeShort(int value) {
        return supper.writeShort(value);
    }

    @Override
    public ByteBuf writeShortLE(int value) {
        return supper.writeShortLE(value);
    }

    @Override
    public ByteBuf writeMedium(int value) {
        return supper.writeMedium(value);
    }

    @Override
    public ByteBuf writeMediumLE(int value) {
        return supper.writeMediumLE(value);
    }

    @Override
    public ByteBuf writeInt(int value) {
        return supper.writeInt(value);
    }

    @Override
    public ByteBuf writeIntLE(int value) {
        return supper.writeIntLE(value);
    }

    @Override
    public ByteBuf writeLong(long value) {
        return supper.writeLong(value);
    }

    @Override
    public ByteBuf writeLongLE(long value) {
        return supper.writeLongLE(value);
    }

    @Override
    public ByteBuf writeChar(int value) {
        return supper.writeLongLE(value);
    }

    @Override
    public ByteBuf writeFloat(float value) {
        return supper.writeFloat(value);
    }

    @Override
    public ByteBuf writeDouble(double value) {
        return supper.writeDouble(value);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src) {
        return supper.writeBytes(src);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int length) {
        return supper.writeBytes(src,length);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        return supper.writeBytes(src,srcIndex,length);
    }

    @Override
    public ByteBuf writeBytes(byte[] src) {
        return supper.writeBytes(src);
    }

    @Override
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        return supper.writeBytes(src,srcIndex,length);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer src) {
        return supper.writeBytes(src);
    }

    @Override
    public int writeBytes(InputStream in, int length) throws IOException {
        return supper.writeBytes(in,length);
    }

    @Override
    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
        return supper.writeBytes(in,length);
    }

    @Override
    public int writeBytes(FileChannel in, long position, int length) throws IOException {
        return supper.writeBytes(in,position,length);
    }

    @Override
    public ByteBuf writeZero(int length) {
        return supper.writeZero(length);
    }

    @Override
    public int writeCharSequence(CharSequence sequence, Charset charset) {
        return supper.writeCharSequence(sequence,charset);
    }

    @Override
    public int indexOf(int fromIndex, int toIndex, byte value) {
        return supper.indexOf(fromIndex, toIndex, value);
    }

    @Override
    public int bytesBefore(byte value) {
        return supper.bytesBefore(value);
    }

    @Override
    public int bytesBefore(int length, byte value) {
        return supper.bytesBefore(length,value);
    }

    @Override
    public int bytesBefore(int index, int length, byte value) {
        return supper.bytesBefore(index,length,value);
    }

    @Override
    public int forEachByte(ByteProcessor processor) {
        return supper.forEachByte(processor);
    }

    @Override
    public int forEachByte(int index, int length, ByteProcessor processor) {
        return supper.forEachByte(index,length,processor);
    }

    @Override
    public int forEachByteDesc(ByteProcessor processor) {
        return supper.forEachByteDesc(processor);
    }

    @Override
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        return supper.forEachByteDesc(index,length,processor);
    }

    @Override
    public ByteBuf copy() {
        return supper.copy();
    }

    @Override
    public ByteBuf copy(int index, int length) {
        return supper.copy(index, length);
    }

    @Override
    public ByteBuf slice() {
        return supper.slice();
    }

    @Override
    public ByteBuf retainedSlice() {
        return supper.retainedSlice();
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return supper.slice(index,length);
    }

    @Override
    public ByteBuf retainedSlice(int index, int length) {
        return supper.retainedSlice(index,length);
    }

    @Override
    public ByteBuf duplicate() {
        return supper.duplicate();
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return supper.retainedDuplicate();
    }

    @Override
    public int nioBufferCount() {
        return supper.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return supper.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int index, int length) {
        return supper.nioBuffer(index,length);
    }

    @Override
    public ByteBuffer internalNioBuffer(int index, int length) {
        return supper.internalNioBuffer(index,length);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return supper.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        return supper.nioBuffers(index, length);
    }

    @Override
    public boolean hasArray() {
        return supper.hasArray();
    }

    @Override
    public byte[] array() {
        return supper.array();
    }

    @Override
    public int arrayOffset() {
        return supper.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return supper.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return supper.memoryAddress();
    }

    @Override
    public String toString(Charset charset) {
        return supper.toString();
    }

    @Override
    public String toString(int index, int length, Charset charset) {
        return supper.toString(index,length,charset);
    }

    @Override
    public int hashCode() {
        return supper.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return supper.equals(obj);
    }

    @Override
    public int compareTo(ByteBuf buffer) {
        return supper.compareTo(buffer);
    }

    @Override
    public String toString() {
        return supper.toString();
    }

    @Override
    public ByteBuf retain(int increment) {
        return supper.retain(increment);
    }

    @Override
    public int refCnt() {
        return supper.refCnt();
    }

    @Override
    public ByteBuf retain() {
        return supper.retain();
    }

    @Override
    public ByteBuf touch() {
        return supper.touch();
    }

    @Override
    public ByteBuf touch(Object hint) {
        return supper.touch(hint);
    }

    @Override
    public boolean release() {
        return supper.release();
    }

    @Override
    public boolean release(int decrement) {
        return supper.release(decrement);
    }

    public void writeString(String s){
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        this.writeShort(bytes.length);
        this.writeBytes(bytes);
    }

    public String readString(){
        int i = this.readShort();
        byte[] bytes = new byte[i];
        for (int j = 0; j < i; j++) {
            bytes[j] = this.readByte();
        }
        return new String(bytes,StandardCharsets.UTF_8);
    }

    public void writeId(String id){
        this.writeString(id);
    }

    public void writeUUID(UUID uuid){
        supper.writeLong(uuid.getMostSignificantBits());
        supper.writeLong(uuid.getLeastSignificantBits());
    }

    public UUID readUUID(){
        return new UUID(supper.readLong(),supper.readLong());
    }

    public void writeVec2f(Vector2f vec){
        supper.writeFloat(vec.x);
        supper.writeFloat(vec.y);
    }

    public Vector2f readVec2f(){
        Vector2f vec = new Vector2f();
        vec.x = supper.readFloat();
        vec.y = supper.readFloat();
        return vec;
    }

    public void writeVec3f(Vector3f vec){
        supper.writeFloat(vec.x);
        supper.writeFloat(vec.y);
        supper.writeFloat(vec.z);
    }

    public Vector3f readVec3f(){
        Vector3f vec = new Vector3f();
        vec.x = supper.readFloat();
        vec.y = supper.readFloat();
        vec.z = supper.readFloat();
        return vec;
    }

    public void writeEnum(Enum<?> e){
        writeByte(e.ordinal() + Byte.MIN_VALUE);
    }

    public <T extends Enum<T>> T readEnum(Class<T> enumClass) {
        return enumClass.getEnumConstants()[readByte() - Byte.MIN_VALUE];
    }

    public String readId(){
        return this.readString();
    }
}
