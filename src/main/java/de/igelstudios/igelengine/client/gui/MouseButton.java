package de.igelstudios.igelengine.client.gui;

import org.lwjgl.glfw.GLFW;

/**
 * Here are the mouse buttons that may be used and are defined by the Engine itself<br>
 * if other mouse buttons are desired they habe to be added manually like other keyboard keys
 */
public enum MouseButton {
    LMB(GLFW.GLFW_MOUSE_BUTTON_1),
    MMB(GLFW.GLFW_MOUSE_BUTTON_3),
    RMB(GLFW.GLFW_MOUSE_BUTTON_2);
    private final int id;
    MouseButton(int id){
        this.id = id;
    }
}
