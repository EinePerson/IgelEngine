package de.igelstudios.igelengine.common.startup;

import de.igelstudios.igelengine.client.keys.*;

import java.util.*;

/**
 * Here keys and their respective listeners may be added so that the engine can register them internally
 * @see EngineInitializer#registerKeys(KeyInitializer)
 */
public class KeyInitializer {
    private Map<Integer, String> keys = new HashMap<>();

    private List<KeyListener> keyListeners = new ArrayList<>();

    private List<MouseMoveListener> moveListeners = new ArrayList<>();

    private List<MouseDragListener> dragListeners = new ArrayList<>();
    private List<MouseClickListener> clickListeners = new ArrayList<>();

    public void add(int key, String name) {
        this.keys.put(Integer.valueOf(key), name);
    }

    public void add(KeyListener listener) {
        this.keyListeners.add(listener);
    }

    @Deprecated
    public void add(MouseMoveListener listener) {
        this.moveListeners.add(listener);
    }

    @Deprecated
    public void add(MouseDragListener listener) {
        this.dragListeners.add(listener);
    }
    @Deprecated
    public void add(MouseClickListener listener) {this.clickListeners.add(listener);}

    public void register() {
        this.keys.forEach(HIDInput::registerKey);

    }

    public void registerListeners(){
        this.keyListeners.forEach(HIDInput::registerKeyListener);
        this.moveListeners.forEach(HIDInput::registerMoveListener);
        this.dragListeners.forEach(HIDInput::registerDragListener);
        this.clickListeners.forEach(HIDInput::registerMouseClickListener);
    }
}
