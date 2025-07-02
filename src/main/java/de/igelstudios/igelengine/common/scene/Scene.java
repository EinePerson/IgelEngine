package de.igelstudios.igelengine.common.scene;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as a way to keep track of every present object that is currently shown.
 * Due to restructuring, the class should not be used extensively anymore
 */
public class Scene {
    protected List<SceneObject> objects;

    public Scene(){
        objects = new ArrayList<>();
    }

    public List<SceneObject> getObjects() {
        return objects;
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
}
