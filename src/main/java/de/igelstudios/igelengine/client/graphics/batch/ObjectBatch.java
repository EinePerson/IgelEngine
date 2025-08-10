package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.graphics.shader.Shader;
import de.igelstudios.igelengine.client.graphics.texture.Texture;
import de.igelstudios.igelengine.client.graphics.texture.TexturePool;
import de.igelstudios.igelengine.common.scene.SceneObject;

import java.util.List;

public class ObjectBatch extends Batch<SceneObject>{

    public ObjectBatch(int size) {
        super(size, new Shader("default"),true,false,2,3);
    }

    @Override
    public boolean dirtyCheck(List<SceneObject> objects,BatchSupplier<SceneObject> supplier) {
        boolean dirty = false;
        for (int i = 0; i < objects.size(); i++) {
            if(objects.get(i).toRemove()){
                clear(i,objects);
                //objects.get(i).removed();
                //Renderer.get().getScene().removeObject(objects.get(i));
                objects.remove(i);
                dirty = true;
                continue;
            }
            if(objects.get(i).isDirty(id)){
                add(i,objects.get(i),supplier);
                dirty = true;
            }
        }
        return dirty;
    }


    @Override
    public void addP(int j, SceneObject obj) {
        float k = 1.0f;
        float l = 1.0f;
        for (int m = 0; m < 4; m++) {
            switch (m) {
                case 1 -> l = 0.0f;
                case 2 -> k = 0.0f;
                case 3 -> l = 1.0f;
            }


            vertices[j] = obj.getPos().x + (k * obj.getSize().x);
            vertices[j + 1] = obj.getPos().y + (l * obj.getSize().y);
            vertices[j + 2] = ((float) obj.getTex());
            vertices[j + 3] = (Texture.TEX_COORDS[obj.getRotation()][m].x * obj.getTexSize().x + obj.getUv().x)  / Texture.getSpritePerTextureX();
            vertices[j + 4] = (Texture.TEX_COORDS[obj.getRotation()][m].y * obj.getTexSize().y + obj.getUv().y)  / Texture.getSpritePerTextureY();

            j += 5;
        }
        obj.unMarkDirty(id);
    }
}
