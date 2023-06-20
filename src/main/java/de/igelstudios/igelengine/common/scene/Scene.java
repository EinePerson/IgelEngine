package de.igelstudios.igelengine.common.scene;

import java.util.ArrayList;
import java.util.List;

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
}
