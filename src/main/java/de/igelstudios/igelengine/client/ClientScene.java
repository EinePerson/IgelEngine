package de.igelstudios.igelengine.client;

import de.igelstudios.igelengine.client.graphics.Camera;
import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.graphics.batch.ObjectBatch;
import de.igelstudios.igelengine.client.graphics.batch.BatchSupplier;
import de.igelstudios.igelengine.client.graphics.batch.TextBatch;
import de.igelstudios.igelengine.client.graphics.batch.TextSupplier;
import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.scene.Scene;
import de.igelstudios.igelengine.common.scene.SceneObject;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class ClientScene extends Scene implements BatchSupplier<SceneObject> {
    private ObjectBatch batch;
    private Camera cam;
    private TextSupplier supplier;
    private Renderer renderer;

    public ClientScene(){
        cam = new Camera(new Vector2f());
        //batch = new ObjectBatch(80 * 45);
        //supplier = new TextSupplier(this);
        //supplier.render();
        renderer = new Renderer(this);
        renderer.render();
    }
    public Camera getCam() {
        return cam;
    }

    @Override
    public void addObject(SceneObject obj) {
        renderer.add(obj);
        super.addObject(obj);
    }

    public void add(Text text){
        renderer.add(text);
    }

    public void render(){
        //batch.render(this);
        //supplier.render();
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

    public TextSupplier getSupplier() {
        return supplier;
    }
}
