package de.igelstudios.igelengine.client.graphics.text;

import com.google.gson.Gson;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class GLFontGen {
    private final String name;
    private final int fontSize;
    int w,h,l;
    private Map<Integer,Char> chars;
    private int tex;

    public GLFontGen(String name){
        this.name = name;
        this.fontSize = 128;
        generate();
    }

    public Char get(int i){
        return chars.getOrDefault(i,new Char(0,0,0,0));
    }

    private Font create(){
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(name));
            env.registerFont(font);
            return new Font(font.getName(),Font.PLAIN,fontSize);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generate(){
        Font font = create();

        BufferedImage img = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics();
        int estimatedWidth = (int)Math.sqrt(font.getNumGlyphs()) * font.getSize() + 1;
        w = 0;
        h = metrics.getHeight();
        l = metrics.getHeight();
        int x = 0;
        int y = (int) (metrics.getHeight() * 1.4f);

        chars = new HashMap<>();
        for (int i = 0; i < font.getNumGlyphs(); i++) {
            if(font.canDisplay(i)){
                Char chat = new Char(x,y,metrics.charWidth(i),metrics.getHeight());
                chars.put(i,chat);
                w = Math.max(x + metrics.charWidth(i),w);
                x += chat.getWith();
                if(x > estimatedWidth){
                    x = 0;
                    y += metrics.getHeight() * 1.4f;
                    h += metrics.getHeight() * 1.4f;
                }
            }
        }
        h += metrics.getHeight() * 1.4f;
        g2d.dispose();

        img = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < font.getNumGlyphs(); i++) {
            if(font.canDisplay(i)){
                Char chat = chars.get(i);
                chat.calcCords(w,h);
                g2d.drawString(String.valueOf((char) i),chat.getX(),chat.getY());
            }
        }
        g2d.dispose();

        try {
            String trimmed = new File(name).getName().replace(".ttf","");
            String png = trimmed + ".png";
            String json = trimmed + ".json";
            if(!new File("src/main/resources/fonts/" + png).exists())new File("src/main/resources/fonts/" + png).createNewFile();
            ImageIO.write(img,"png",new FileOutputStream("src/main/resources/fonts/" + png));
            Files.writeString(Path.of("src/main/resources/fonts/" + json),new Gson().toJson(chars));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void put(BufferedImage img){
        int[] pixels = new int[img.getHeight() * img.getWidth()];
        img.getRGB(0,0,img.getWidth(),img.getHeight(),pixels,0,img.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(img.getWidth() * img.getHeight() * 4);
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                int k = pixels[i * img.getWidth() + j];
                byte a = (byte)((k >> 24) & 0xFF);
                buffer.put(a);
                buffer.put(a);
                buffer.put(a);
                buffer.put(a);
            }
        }

        buffer.flip();

        tex = glGenTextures();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        buffer.clear();
    }
}
