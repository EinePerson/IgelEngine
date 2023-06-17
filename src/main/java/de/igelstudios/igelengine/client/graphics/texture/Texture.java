package de.igelstudios.igelengine.client.graphics.texture;

import de.igelstudios.ClientMain;
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
    public static final int SPRITE_PER_TEXTURE = 16;
    public static final Vector2f[] TEX_COORDS = {
            new Vector2f(1, 0),
            new Vector2f(1, 1),
            new Vector2f(0, 1),
            new Vector2f(0, 0),
    };
    private static final Map<Integer, Texture> textures = new HashMap<>();
    private static final Map<String,Integer> texIDs = new HashMap<>();
    private String path;
    private int tex;

    private Texture(String path){
        this.path = path;

        tex = GL40.glGenTextures();
        GL40.glBindTexture(GL11.GL_TEXTURE_2D, tex);
        GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        TextureInfo info = new TextureInfo();
        ByteBuffer img = read(path,info);
        if (img != null){
            if(info.ip2.get(0) == 3) GL40.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, info.ip0.get(0), info.ip1.get(0), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, img);
            else if(info.ip2.get(0) == 4)GL40.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, info.ip0.get(0), info.ip1.get(0), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, img);
            stbi_image_free(img);
        }
        else ClientMain.LOGGER.error("Could not load image:" + this.path);
    }

    public static Texture get(String path){
        if(!texIDs.containsKey(path)){
            int i;
            if(textures.isEmpty())i = 0;
            else i = Collections.max(textures.keySet(),Integer::compareTo) + 1;
            textures.put(i,new Texture(path));
            texIDs.put(path,i);
        }
        return textures.get(texIDs.get(path));
    }

    public static int texCount(){
        return textures.size();
    }

    public static Collection<Texture> get(){
        return textures.values();
    }

    public static ByteBuffer read(String name,TextureInfo info){
        try (InputStream stream = Texture.class.getClassLoader().getResourceAsStream(name)){
            byte[] bytes = Objects.requireNonNull(stream).readAllBytes();
            ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.rewind();
            ByteBuffer img = STBImage.stbi_load_from_memory(byteBuffer, info.ip0, info.ip1, info.ip2, 4);
            if(img == null)throw new RuntimeException("Could not load image" + stbi_failure_reason());
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
        return texIDs.get(path);
    }

    public static void bind(String path){
        textures.get(texIDs.get(path)).bind();
    }
    public static void unbind(String path){
        textures.get(texIDs.get(path)).unbind();
    }

    public record TextureInfo(IntBuffer ip0, IntBuffer ip1, IntBuffer ip2){

        public TextureInfo(){
            this(BufferUtils.createIntBuffer(1),BufferUtils.createIntBuffer(1),BufferUtils.createIntBuffer(1));
        }
    }
}
