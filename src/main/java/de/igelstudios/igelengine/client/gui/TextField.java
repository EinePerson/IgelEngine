package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.graphics.Polygon;
import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.lang.Text;
import org.joml.Vector2f;

/**
 * A text field is a clickable area where a text can be written to, beware that only the text is shown and no background
 */
public class TextField {
    private String content = "";
    private Vector2f pos;
    private Vector2f size;
    private final Text text;

    private boolean hasBackground = false;
    private Polygon backGround = null;
    private int backColor = 0;
    private int selBackBackground = 0;

    private boolean hasLabel = false;
    private Text label = null;


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

    /**
     * Adds a background to this text field, this color changes when the text field is selected
     * @param color the color this normally has in RGBA format
     * @param selectedColor the color this has when selected in RGBA format
     * @return this for chained modification calls
     */
    public TextField addBackground(int color,int selectedColor){
        backGround = new Polygon(new Vector2f(pos),new Vector2f(pos).add(new Vector2f(size.x,0)),new Vector2f(pos).add(new Vector2f(size)),new Vector2f(pos).add(new Vector2f(0,size.y)));
        this.backColor = color;
        this.selBackBackground = selectedColor;
        backGround.setRGBA(((color >> 24) & 0xFF) / 255.0f,((color >> 16) & 0xFF) / 255.0f,((color >> 8) & 0xFF) / 255.0f,(color & 0xFF) / 255.0f);
        hasBackground = true;

        return this;
    }

    /**
     * Adds a label to be displayed directly left of the Text field
     * @param text the text to be displayed, no modifications except for position are done in here so the color and similar things habe to be done manually
     * @return this for chained modification calls
     */
    public TextField addLabel(Text text){
        label = text;

        hasLabel = true;

        return this;
    }

    /**
     * Checks weather this has a background
     * @return weather the text field has a background
     */
    public boolean hasBackground(){
        return hasBackground;
    }

    public Polygon getBackGround() {
        return backGround;
    }

    public int getBackGroundColor() {
        return backColor;
    }

    public int getSelectedBackgroundColor() {
        return selBackBackground;
    }

    public boolean hasLabel() {
        return hasLabel;
    }

    public Text getLabel() {
        return label;
    }
}
