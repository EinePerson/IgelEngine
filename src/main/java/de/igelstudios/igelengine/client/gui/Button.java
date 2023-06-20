package de.igelstudios.igelengine.client.gui;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Button {
    private Vector2f pos;
    private Vector2f size;
    private List<ButtonClickEvent> listeners;
    public Button(Vector2f pos,Vector2f size){
        this.pos = pos;
        this.size = size;
        listeners = new ArrayList<>();
    }


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
