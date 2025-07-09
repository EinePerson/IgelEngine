package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.graphics.batch.TextBatch;
import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.scene.Scene;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * A text field is a clickable area where a text can be written to, beware that only the text is shown and no background
 */
public class TextField {
    private String content = "";
    private Vector2f pos;
    private Vector2f size;
    private final Text text;

    /**
     * creates a new Text field
     * @param pos the position the field should start at
     * @param size the overall size of the field, thus pos + size = the end position
     * @see TextField#TextField(Vector2f, Vector2f, Vector2f) TextField
     */
    public TextField(Vector2f pos,Vector2f size){
        this(pos,new Vector2f(0,0),size);
    }

    /**
     * creates a new Text field
     * @param pos the position the field should start at
     * @param size the overall size of the field, thus pos + size = the end position
     * @param initialText the initial text to be displayed
     * @see TextField#TextField(Vector2f, Vector2f, Vector2f) TextField
      * @see TextField#TextField(Vector2f, Vector2f)  TextField
      * @see TextField#TextField(Vector2f, Vector2f, Vector2f, String) TextField
     */
    public TextField(Vector2f pos,Vector2f size,String initialText){
        this(pos,new Vector2f(0,0),size,initialText);
    }

    /**
     * creates a new Text field
     * @param pos the position the field should start at
     * @param textOffset the offset from the pos to where the text shall be shown
     * @param size the overall size of the field, thus pos + size = the end position
     * @see TextField#TextField(Vector2f, Vector2f)  TextField
     * @see TextField#TextField(Vector2f, Vector2f, Vector2f, String) TextField
     * @see TextField#TextField(Vector2f, Vector2f, String) TextField
     *
     */
    public TextField(Vector2f pos,Vector2f textOffset,Vector2f size){
        this(pos,textOffset,size,"");
    }

    /**
     * creates a new Text field
     * @param pos the position the field should start at
     * @param textOffset the offset from the pos to where the text shall be shown
     * @param size the overall size of the field, thus pos + size = the end position
     * @param initialText the initial text to be displayed
     * @see TextField#TextField(Vector2f, Vector2f) TextField
     * @see TextField#TextField(Vector2f, Vector2f, Vector2f) TextField
     * @see TextField#TextField(Vector2f, Vector2f, String) TextField
     */
    public TextField(Vector2f pos,Vector2f textOffset,Vector2f size,String initialText){
        this.pos = pos;
        this.size = size;
        text = Text.literal(initialText);
        Renderer.get().render(text,textOffset.x + pos.x,textOffset.y + pos.y);
    }

    /**
     * the color of the text that is displayed when typing, from 0 to 1
     * @param r the amount of red
     * @param g the amount of green
     * @param b the amount of blue
     */
    public void setTextCol(float r,float g,float b){
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

    /**
     * This returns what is currently written in the field
     * @return the content
     */
    public String getContent() {
        return content;
    }
}
