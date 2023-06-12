package de.igelstudios.igelengine.client;

import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.graphics.Camera;
import de.igelstudios.igelengine.common.scene.Scene;
import org.joml.Vector2f;

public class ClientScene extends Scene {
    private Camera cam;

    public ClientScene(){
        cam = new Camera(new Vector2f());
    }
    public Camera getCam() {
        return cam;
    }
}
