package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.graphics.shader.Shader;
import de.igelstudios.igelengine.client.graphics.texture.Texture;
import de.igelstudios.igelengine.common.scene.SceneObject;

import java.util.List;

public class ObjectBatch extends Batch<SceneObject>{

    public ObjectBatch(int size) {
        super(size, new Shader("default"),true,2,3);
    }

    @Override
    public boolean dirtyCheck(List<SceneObject> objects) {
        boolean dirty = false;
        for (int i = 0; i < objects.size(); i++) {
            if(objects.get(i).isDirty()){
                add(i,objects.get(i));
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


            vertices[j] = obj.getPos().x + k;
            vertices[j + 1] = obj.getPos().y + l;
            vertices[j + 2] = ((float) obj.getTex());
            vertices[j + 3] = (Texture.TEX_COORDS[m].x + obj.getUv().x)  / Texture.SPRITE_PER_TEXTURE;
            vertices[j + 4] = (Texture.TEX_COORDS[m].y + obj.getUv().y)  / Texture.SPRITE_PER_TEXTURE;

            j += 5;
        }
        obj.unMarkDirty();
    }
}
