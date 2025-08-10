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

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Client side implementation of the Scene, this adds the possibility to move around with an instance of {@link Camera}
 * it for some reason also contains the initialisation of a Render, this may be changed in the future
 */
public class ClientScene extends Scene {
    protected Camera camera;
    private List<Integer> windowIDs;

    public ClientScene(){
        camera = new Camera();
        windowIDs = new ArrayList<>();
    }

    public void onAddToRenderer(int id){
        if(windowIDs.contains(id))return;
        windowIDs.add(id);
        objects.forEach(obj -> Renderer.get(id).render(obj));
        lines.forEach(line -> Renderer.get(id).render(line));
        texts.forEach(text -> Renderer.get(id).render(text));
        polygons.forEach(polygon -> Renderer.get(id).render(polygon));
    }

    //TODO make it so that objects can be removed on only one screen
    public void onRemoveFromRenderer(int id){
        clearLines();
        clearObjects();
        clearPolygons();
        clearTexts();
    }

    @Override
    public void addObject(SceneObject obj) {
        super.addObject(obj);
        for(Integer windowID : windowIDs){
            Renderer.get(windowID).render(obj);
        }
    }

    @Override
    public void addLine(Line line) {
        super.addLine(line);
        for(Integer windowID : windowIDs){
            Renderer.get(windowID).render(line);
        }
    }

    @Override
    public void addPolygon(Polygon polygon) {
        super.addPolygon(polygon);
        for(Integer windowID : windowIDs){
            Renderer.get(windowID).render(polygon);
        }
    }

    @Override
    public void addText(Text text) {
        super.addText(text);
        for(Integer windowID : windowIDs){
            Renderer.get(windowID).render(text);
        }
    }

    @Override
    public void removeLine(Line line) {
        super.removeLine(line);
        line.remove();
    }

    @Override
    public void removePolygon(Polygon polygon) {
        super.removePolygon(polygon);
        polygon.remove();
    }

    @Override
    public void removeText(Text text) {
        super.removeText(text);
        text.setLifeTime(0);
    }

    @Override
    public void removeObject(SceneObject obj) {
        super.removeObject(obj);
        obj.remove();
    }

    @Override
    public void clearLines() {
        lines.forEach(Line::remove);
        super.clearLines();
    }

    @Override
    public void clearObjects() {
        objects.forEach(SceneObject::remove);
        super.clearObjects();
    }

    @Override
    public void clearPolygons() {
        polygons.forEach(Polygon::remove);
        super.clearPolygons();
    }

    @Override
    public void clearTexts() {
        texts.forEach(txt -> txt.setLifeTime(0));
        super.clearTexts();
    }
}
