package de.igelstudios.igelengine.client;

import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.graphics.Batch;
import de.igelstudios.igelengine.client.graphics.Camera;
import de.igelstudios.igelengine.common.scene.Scene;
import de.igelstudios.igelengine.common.scene.SceneObject;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class ClientScene extends Scene {
    private Batch batch;

    private Camera cam;

    public ClientScene(){
        cam = new Camera(new Vector2f());
        batch = new Batch(80 * 45);
    }
    public Camera getCam() {
        return cam;
    }

    @Override
    public void addObject(SceneObject obj) {
        if(batch.getSize() > objects.size()) {
            batch.add(super.objects.size(), obj);
            super.addObject(obj);
        }
    }

    public void render(){
        batch.render(this);
    }
}
