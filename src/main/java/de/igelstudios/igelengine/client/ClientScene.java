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

    public ClientScene(){
        cam = new Camera(new Vector2f());
        new Renderer(this);
    }
    public Camera getCam() {
        return cam;
    }

    @Override
    public void addObject(SceneObject obj) {
        super.addObject(obj);
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

    @Override
    public int getIndicesSize(int i) {
        return 0;
    }

    @Override
    public int getIndicesSize() {
        return 0;
    }

    @Override
    public int getVertexCount() {
        return getSize() * 3;
    }
}
