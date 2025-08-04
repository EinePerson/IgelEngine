package de.igelstudios.igelengine.client.graphics.texture;

import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.ClientEngine;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL40;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private static int SPRITE_PER_TEXTURE_X = 1;
    private static int SPRITE_PER_TEXTURE_Y = 1;
    //public static final int SPRITE_PER_TEXTURE = 1;
    public static final Vector2f[][] TEX_COORDS = {
            new Vector2f[]{
                    new Vector2f(1, 0),
                    new Vector2f(1, 1),
                    new Vector2f(0, 1),
                    new Vector2f(0, 0),
            },
            new Vector2f[]{
                    new Vector2f(1, 1),
                    new Vector2f(0, 1),
                    new Vector2f(0, 0),
                    new Vector2f(1, 0),
            },
            new Vector2f[]{
                    new Vector2f(0, 1),
                    new Vector2f(0, 0),
                    new Vector2f(1, 0),
                    new Vector2f(1, 1),
            },
            new Vector2f[]{
                    new Vector2f(0, 0),
                    new Vector2f(1, 0),
                    new Vector2f(1, 1),
                    new Vector2f(0, 1),
            }
    };
    private String path;
    private int tex;

    public static int getSpritePerTextureX(){
        return SPRITE_PER_TEXTURE_X;
    }

    public static int getSpritePerTextureY(){
        return SPRITE_PER_TEXTURE_Y;
    }

    public static void setSpritePerTextureX(int textureX){
        SPRITE_PER_TEXTURE_X = textureX;
    }

    public static void setSpritePerTextureY(int textureY){
        SPRITE_PER_TEXTURE_Y = textureY;
    }

    Texture(String path,int tex){
        this.path = path;

        this.tex = tex;


        ClientEngine.queueForAllRenderThreads(() -> {
            GL40.glBindTexture(GL11.GL_TEXTURE_2D, tex);
            GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            TextureInfo info = new TextureInfo();
            ByteBuffer img = read(path, info);
            if (img != null) {
                if (info.ip2.get(0) == 3)
                    GL40.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, info.ip0.get(0), info.ip1.get(0), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, img);
                else if (info.ip2.get(0) == 4)
                    GL40.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, info.ip0.get(0), info.ip1.get(0), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, img);
                stbi_image_free(img);
            }
        });


        //else ClientMain.LOGGER.error("Could not load image:" + this.path);
    }

    Texture(String path,boolean b){
        this.path = path;

        ClientEngine.queueForRenderThread(() -> {
            tex = GL40.glGenTextures();
            GL40.glBindTexture(GL11.GL_TEXTURE_2D, tex);
            GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            TextureInfo info = new TextureInfo();
            ByteBuffer img = readI(path,info);
            if (img != null){
                if(info.ip2.get(0) == 3) GL40.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, info.ip0.get(0), info.ip1.get(0), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, img);
                else if(info.ip2.get(0) == 4)GL40.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, info.ip0.get(0), info.ip1.get(0), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, img);
                stbi_image_free(img);
            }
        },0);

        //else ClientMain.LOGGER.error("Could not load image:" + this.path);
    }

    public static ByteBuffer read(String name,TextureInfo info){
        try (InputStream stream = ClientMain.class.getClassLoader().getResourceAsStream(name)){
            byte[] bytes = Objects.requireNonNull(stream).readAllBytes();
            ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.rewind();
            ByteBuffer img = STBImage.stbi_load_from_memory(byteBuffer, info.ip0, info.ip1, info.ip2, 4);
            if(img == null)throw new RuntimeException("Could not load image " + name + " " + stbi_failure_reason());
            return img;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static ByteBuffer readI(String name,TextureInfo info){
        try (InputStream stream = Texture.class.getClassLoader().getResourceAsStream(name)){
            byte[] bytes = Objects.requireNonNull(stream).readAllBytes();
            ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.rewind();
            ByteBuffer img = STBImage.stbi_load_from_memory(byteBuffer, info.ip0, info.ip1, info.ip2, 4);
            if(img == null)throw new RuntimeException("Could not load image " + name + " " + stbi_failure_reason());
            return img;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void bind(){
        GL40.glBindTexture(GL11.GL_TEXTURE_2D,tex);
    }

    public void unbind(){
        GL40.glBindTexture(GL11.GL_TEXTURE_2D,0);
    }

    public String getPath() {
        return path;
    }

    public int getID() {
        return tex;
    }

    public record TextureInfo(IntBuffer ip0, IntBuffer ip1, IntBuffer ip2){

        public TextureInfo(){
            this(BufferUtils.createIntBuffer(1),BufferUtils.createIntBuffer(1),BufferUtils.createIntBuffer(1));
        }
    }
}
