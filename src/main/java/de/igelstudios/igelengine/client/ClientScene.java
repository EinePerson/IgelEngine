package de.igelstudios.igelengine.client;

import de.igelstudios.igelengine.client.graphics.Camera;
import de.igelstudios.igelengine.client.graphics.Line;
import de.igelstudios.igelengine.client.graphics.Polygon;
import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.graphics.batch.BatchSupplier;
import de.igelstudios.igelengine.client.graphics.batch.PolygonBatch;
import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.scene.Scene;
import de.igelstudios.igelengine.common.scene.SceneObject;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.List;

/**
 * This is the Client side implementation of the Scene, this adds the possibility to move around with an instance of {@link Camera}
 * it for some reason also contains the initialisation of a Render, this may be changed in the future
 */
public class ClientScene extends Scene {
    protected Renderer renderer;
    protected Camera camera;

    public ClientScene(int id){
        camera = new Camera(id);
        renderer = new Renderer(camera, id);
    }

    public Renderer getRenderer() {
        return renderer;
    }

    @Override
    public void addObject(SceneObject obj) {
        super.addObject(obj);
        renderer.render(obj);
    }

    @Override
    public void addLine(Line line) {
        super.addLine(line);
        renderer.render(line);
    }

    @Override
    public void addPolygon(Polygon polygon) {
        super.addPolygon(polygon);
        renderer.render(polygon);
    }

    @Override
    public void addText(Text text) {
        super.addText(text);
        renderer.render(text);
    }

    @Override
    public void removeLine(Line line) {
        super.removeLine(line);
        renderer.render(line);
    }

    @Override
    public void removePolygon(Polygon polygon) {
        super.removePolygon(polygon);
        renderer.render(polygon);
    }

    @Override
    public void removeText(Text text) {
        super.removeText(text);
        renderer.render(text);
    }

    @Override
    public void removeObject(SceneObject obj) {
        super.removeObject(obj);
        renderer.render(obj);
    }

    @Override
    public void clearLines() {
        super.clearLines();
        renderer.clearLine();
    }

    @Override
    public void clearObjects() {
        super.clearObjects();
        renderer.clearObjects();
    }

    @Override
    public void clearPolygons() {
        super.clearPolygons();
        renderer.clearPolygon();
    }

    @Override
    public void clearTexts() {
        super.clearTexts();
        renderer.clearText();
    }
}
