package de.igelstudios.igelengine.client.gui;

@FunctionalInterface
public interface CheckBoxChangeEvent {
    void stateChanged(boolean isSelected);
}
