package de.igelstudios.igelengine.client.gui;

import org.lwjgl.glfw.GLFW;

public enum MouseButton {
    LMB(GLFW.GLFW_MOUSE_BUTTON_1),
    MMB(GLFW.GLFW_MOUSE_BUTTON_3),
    RMB(GLFW.GLFW_MOUSE_BUTTON_2);
    private final int id;
    MouseButton(int id){
        this.id = id;
    }
}
