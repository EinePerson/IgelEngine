package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.graphics.Polygon;
import de.igelstudios.igelengine.client.lang.Text;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class CheckBox implements Clickable{
    private Vector2f pos;
    private Vector2f size;
    private Text label;
    private List<CheckBoxChangeEvent> listeners;
    private boolean state;
    private final Polygon outline;
    private final Polygon inside;

    public CheckBox(Vector2f pos,Text label){
        this(pos,label,false);
    }

    public CheckBox(Vector2f pos,Text label,boolean defaultState) {
        this.pos = pos;
        size = new Vector2f(label.getFullVisualLength() + 1,label.getHeight());
        this.label = label.setPos(pos.x + 1, pos.y);
        this.listeners = new ArrayList<>();
        state = false;

        outline = new Polygon(new Vector2f(pos.x + 0.25f,pos.y + 0.25f),new Vector2f(pos.x + 0.75f,pos.y + 0.25f), new Vector2f(pos.x + 0.75f,pos.y + 0.75f), new Vector2f(pos.x + 0.25f,pos.y + 0.75f));
        inside = new Polygon(new Vector2f(pos.x + 0.4f,pos.y + 0.4f),new Vector2f(pos.x + 0.6f,pos.y + 0.4f), new Vector2f(pos.x + 0.6f,pos.y + 0.6f), new Vector2f(pos.x + 0.4f,pos.y + 0.6f));
        state = defaultState;
        updateState();
    }

    public void addListener(CheckBoxChangeEvent listener) {
        listeners.add(listener);
    }

    public void removeListener(CheckBoxChangeEvent listener) {
        listeners.remove(listener);
    }

    @Override
    public Vector2f getPos() {
        return pos;
    }

    @Override
    public Vector2f getSize() {
        return size;
    }

    private void updateState(){
        inside.setColor(state?0x000000:0xFFFFFF);
    }

    @Override
    public void invoke(MouseButton button){
        if(button == MouseButton.LMB){
            state = !state;
            updateState();
            listeners.forEach(listeners -> listeners.stateChanged(state));
        }
    }

    public Text getLabel() {
        return label;
    }

    public Polygon getInside() {
        return inside;
    }

    public Polygon getOutline() {
        return outline;
    }
}
