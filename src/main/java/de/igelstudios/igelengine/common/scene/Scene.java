package de.igelstudios.igelengine.common.scene;

import de.igelstudios.igelengine.common.entity.Entity;

public class Scene {
    protected Entity[][] entities;

    public Scene(){
        entities = new Entity[16][16];
    }
}
