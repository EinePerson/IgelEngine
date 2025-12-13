package de.igelstudios.igelengine.client.graphics.text;

import com.google.gson.Gson;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.Path2D;
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
    private static final int CHAR_COUNT = 256;
    private final String name;
    private final int fontSize;
    int w,h;
    private Map<Integer,Char> chars;
    private int tex;
    private String subDir;

    public GLFontGen(String name,String subDir){
        this.name = name;
        this.subDir = subDir.isEmpty()?"":subDir + "/";
        this.fontSize = 128;
        generate();
    }

    public GLFontGen(String name){
        this(name,"");
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
        w = 0;
        h = (int) (metrics.getHeight() /* 1.4f*/);
        int x = 0;
        int y = (int) (metrics.getHeight() /* 1.4f*/);

        chars = new HashMap<>();
        for (int i = 0; i < CHAR_COUNT; i++) {
            if(font.canDisplay(i)){
                Char chat = new Char(x,y,metrics.charWidth(i), (int) (metrics.getHeight() /* 1.4f*/));
                chars.put(i,chat);
                w = Math.max(x + metrics.charWidth(i),w);
                x += chat.getWith();
            }
        }
        h += (int) (metrics.getHeight() /* 1.4f*/);
        g2d.dispose();

        img = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        g2d.setPaint(Color.WHITE);
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < CHAR_COUNT; i++) {
            if(font.canDisplay(i)){
                Char chat = chars.get(i);
                chat.calcCords(w,h);
                //the old shit stopped working
                GlyphVector gv = font.createGlyphVector(g2d.getFontRenderContext(), String.valueOf((char) i));
                Path2D.Double combined = new Path2D.Double(Path2D.WIND_NON_ZERO);
                Shape full = gv.getOutline(chat.getX(), chat.getY()); // x,y baseline
                combined.append(full, false);
                g2d.fill(combined);
            }
        }
        g2d.dispose();

        try {
            String trimmed = new File(name).getName().replace(".ttf","").replace(".otf","");
            String png = trimmed + ".png";
            String json = trimmed + ".json";
            if(!new File(subDir + "src/main/resources/fonts/" + png).exists())new File(subDir + "src/main/resources/fonts/" + png).createNewFile();
            ImageIO.write(img,"png",new FileOutputStream(subDir + "src/main/resources/fonts/" + png));
            Files.writeString(Path.of(subDir + "src/main/resources/fonts/" + json),new Gson().toJson(chars));
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
