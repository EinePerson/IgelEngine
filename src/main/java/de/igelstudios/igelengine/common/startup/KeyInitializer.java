package de.igelstudios.igelengine.common.startup;

import de.igelstudios.igelengine.client.keys.*;

import java.util.*;

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

    public void add(MouseMoveListener listener) {
        this.moveListeners.add(listener);
    }

    public void add(MouseDragListener listener) {
        this.dragListeners.add(listener);
    }
    public void add(MouseClickListener listener) {this.clickListeners.add(listener);}

    public void register(HIDInput input) {
        Objects.requireNonNull(input);
        this.keys.forEach(input::registerKey);
        Objects.requireNonNull(input);
        this.keyListeners.forEach(input::registerKeyListener);
        Objects.requireNonNull(input);
        this.moveListeners.forEach(input::registerMoveListener);
        Objects.requireNonNull(input);
        this.dragListeners.forEach(input::registerDragListener);
        Objects.requireNonNull(input);
        this.clickListeners.forEach(input::registerMouseClickListener);
    }
}
