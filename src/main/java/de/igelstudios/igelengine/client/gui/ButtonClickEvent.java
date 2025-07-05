package de.igelstudios.igelengine.client.gui;

/**
 * Handler for {@link Button} to receive notifications when it is clicked
 */
@FunctionalInterface
public interface ButtonClickEvent {
    void clicked(MouseButton button);
}
