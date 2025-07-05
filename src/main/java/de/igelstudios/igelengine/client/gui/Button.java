package de.igelstudios.igelengine.client.gui;

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
}
