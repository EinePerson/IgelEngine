package de.igelstudios.igelengine.client.gui;

import org.joml.Vector2f;

interface Clickable {

    Vector2f getPos();

    Vector2f getSize();

    void invoke(MouseButton button);
}
