package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.lang.Text;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * creates a new Button, when it is clicked all listeners added via {@link #addListener(ButtonClickEvent)} are called
 */
public class Button implements Clickable{
    private Vector2f pos;
    private Vector2f size;
    private List<ButtonClickEvent> listeners;
    private boolean hasLabel = false;
    protected Text label = null;
    private Map<GUI,Boolean> hidden = new HashMap<>();

    /**
     * creates a new button spanning from pos to pos + size
     * @param pos the starting position of the button
     * @param size the size of the button,
     */
    public Button(Vector2f pos,Vector2f size){
        this.pos = pos;
        this.size = size;
        listeners = new ArrayList<>();
    }

    /**
     * creates a new Button, the size of the button is the exact size of the label
     * @param pos the position where the button shall be located
     * @param label the text to display and wrap this button around
     */
    public Button(Vector2f pos,Text label){
        this.pos = pos;
        size = new Vector2f(label.getFullVisualLength(),label.getHeight());
        hasLabel = true;
        this.label = label;
        listeners = new ArrayList<>();
    }

    /**
     * removes the button from the GUI thus hiding the (optional) label and making it unClickable
     * @param gui the GUI to remove the button from
     */
    public void hideOn(GUI gui){
        if(hidden.containsKey(gui) && hidden.get(gui))return;

        hidden.put(gui, true);
        gui.removeButton(this);
    }

    /**
     * Readds this button to the specified GUI, if it was previously removed using {@link #hideOn(GUI)}
     * @param gui the GUI to readd the button to
     */
    public void showOn(GUI gui){
        if(!hidden.containsKey(gui) || !hidden.get(gui))return;

        hidden.put(gui, false);
        gui.addButton(this);
    }

    /**
     * Creates a new Button with the exact measurements of the Text label
     * @param label the label to wrap around
     */
    public Button(Text label){
        this(label.getPos(),label);
    }

    /**
     * adds a listener to be invoked when the button is pressed
     * @param e the method to be called
     * @see ButtonClickEvent
     */
    public void addListener(ButtonClickEvent e){
        listeners.add(e);
    }

    @Override
    public void invoke(MouseButton button){
        listeners.forEach(buttonClickEvent -> buttonClickEvent.clicked(button));
    }

    @Override
    public Vector2f getPos() {
        return pos;
    }

    @Override
    public Vector2f getSize() {
        return size;
    }

    public boolean hasLabel() {
        return hasLabel;
    }

    public Text getLabel() {
        return label;
    }

    @Override
    public String toString() {
        if(hasLabel)return "Button: " + label.getContent();
        else return super.toString();
    }
}
