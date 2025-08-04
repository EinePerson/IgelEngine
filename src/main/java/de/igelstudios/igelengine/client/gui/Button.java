package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.lang.Text;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * creates a new Button, when it is clicked all listeners added via {@link #addListener(ButtonClickEvent)} are called
 */
public class Button {
    private Vector2f pos;
    private Vector2f size;
    private List<ButtonClickEvent> listeners;
    private boolean hasLabel = false;
    private Text label = null;

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
     * adds a listener to be invoked when the button is pressed
     * @param e the method to be called
     * @see ButtonClickEvent
     */
    public void addListener(ButtonClickEvent e){
        listeners.add(e);
    }

    void invoke(MouseButton button){
        listeners.forEach(buttonClickEvent -> buttonClickEvent.clicked(button));
    }

    public Vector2f getPos() {
        return pos;
    }

    public Vector2f getSize() {
        return size;
    }

    public boolean hasLabel() {
        return hasLabel;
    }

    public Text getLabel() {
        return label;
    }
}
