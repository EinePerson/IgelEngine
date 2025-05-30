package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.graphics.batch.TextBatch;
import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.scene.Scene;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class TextField {
    private String content = "";
    private Vector2f pos;
    private Vector2f size;
    private final Text text;

    public TextField(Vector2f pos,Vector2f size){
        this(pos,new Vector2f(0,0),size);
    }

    public TextField(Vector2f pos,Vector2f textOffset,Vector2f size){
        this.pos = pos;
        this.size = size;
        text = Text.literal("");
        Renderer.get().render(text,textOffset.x + pos.x,textOffset.y + pos.y);
    }

    public void setTextCol(int r,int g,int b){
        text.setColor(r,g,b);
    }

    public void add(char c){
        content += c;
        text.add(c);
    }

    public void hide(){
        text.setLifeTime(0);
    }

    public void show(){
        text.setLifeTime(-1);
    }

    public int getLength(){
        return content.length();
    }

    public void remove(){
        content = content.substring(0,content.length() -1);
        text.remove();
    }

    public Vector2f getSize() {
        return size;
    }

    public Vector2f getPos() {
        return pos;
    }

    public Text getText() {
        return text;
    }

    public String getContent() {
        return content;
    }
}
