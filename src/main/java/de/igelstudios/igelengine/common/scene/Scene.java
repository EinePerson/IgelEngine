package de.igelstudios.igelengine.common.scene;

import de.igelstudios.igelengine.client.graphics.Line;
import de.igelstudios.igelengine.client.graphics.Polygon;
import de.igelstudios.igelengine.client.lang.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as a way to keep track of every present object that is currently shown.
 * Due to restructuring, the class should not be used extensively anymore
 */
public class Scene {
    protected List<SceneObject> objects;
    protected List<Line> lines;
    protected List<Text> texts;
    protected List<Polygon> polygons;

    public Scene(){
        objects = new ArrayList<>();
        lines = new ArrayList<>();
        texts = new ArrayList<>();
        polygons = new ArrayList<>();
    }

    public void addObject(SceneObject obj){
        objects.add(obj);
    }

    public void removeObject(SceneObject obj){
        objects.remove(obj);
    }

    public void clearObjects(){
        objects.clear();
    }

    public void addLine(Line line){
        lines.add(line);
    }

    public void removeLine(Line line){
        lines.remove(line);
    }

    public void clearLines(){
        lines.clear();
    }
    public void addText(Text text){
        texts.add(text);
    }

    public void removeText(Text text){
        texts.remove(text);
    }

    public void clearTexts(){
        texts.clear();
    }

    public void addPolygon(Polygon polygon){
        polygons.add(polygon);
    }

    public void removePolygon(Polygon polygon){
        polygons.remove(polygon);
    }

    public void clearPolygons(){
        polygons.clear();
    }

    public void clear() {
        clearObjects();
        clearLines();
        clearTexts();
        clearPolygons();
    }
}
