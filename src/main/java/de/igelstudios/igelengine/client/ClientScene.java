package de.igelstudios.igelengine.client;

import de.igelstudios.igelengine.client.graphics.Camera;
import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.graphics.batch.BatchSupplier;
import de.igelstudios.igelengine.common.scene.Scene;
import de.igelstudios.igelengine.common.scene.SceneObject;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.List;

public class ClientScene extends Scene implements BatchSupplier<SceneObject> {
    private Camera cam;
    private Renderer renderer;

    public ClientScene(){
        cam = new Camera(new Vector2f());;
        renderer = new Renderer(this);
        renderer.render();
    }
    public Camera getCam() {
        return cam;
    }

    @Override
    public void addObject(SceneObject obj) {
        renderer.render(obj,obj.getPos().x,obj.getPos().y);
        super.addObject(obj);
    }

    public void render(){
        renderer.render();
    }

    @Override
    public Matrix4f getProjMat() {
        return cam.getProjMat();
    }

    @Override
    public Matrix4f getViewMat() {
        return cam.getViewMat();
    }

    @Override
    public List<SceneObject> getT() {
        return super.getObjects();
    }

    @Override
    public int getSize() {
        return objects.size();
    }

    @Override
    public int getSize(int i) {
        return i;
    }

    public Renderer getRenderer() {
        return renderer;
    }
}
