package de.igelstudios.igelengine.client.graphics.texture;

import de.igelstudios.ClientMain;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL40;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {
    private String path;
    private int tex;

    public Texture(String cutPath){
        this.path = "src/main/resources/" + cutPath;

        tex = GL40.glGenTextures();
        GL40.glBindTexture(GL11.GL_TEXTURE_2D, tex);
        GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL40.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        IntBuffer ip0 = BufferUtils.createIntBuffer(1);
        IntBuffer ip1 = BufferUtils.createIntBuffer(1);
        IntBuffer ip2 = BufferUtils.createIntBuffer(1);
        ByteBuffer img = stbi_load(path, ip0, ip1, ip2, 0);
        if (img != null){
            if(ip2.get(0) == 3) GL40.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, ip0.get(0), ip1.get(0), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, img);
            else if(ip2.get(0) == 4)GL40.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, ip0.get(0), ip1.get(0), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, img);
            stbi_image_free(img);
        }
        else ClientMain.LOGGER.error("Could not load image:" + path);
    }

    public void bind(){
        GL40.glBindTexture(GL11.GL_TEXTURE_2D,tex);
    }

    public void unbind(){
        GL40.glBindTexture(GL11.GL_TEXTURE_2D,0);
    }
}
