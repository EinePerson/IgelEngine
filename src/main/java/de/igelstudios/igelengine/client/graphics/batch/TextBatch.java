package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.graphics.shader.Shader;
import de.igelstudios.igelengine.client.graphics.text.Char;
import de.igelstudios.igelengine.client.graphics.texture.Texture;
import de.igelstudios.igelengine.client.graphics.texture.TexturePool;
import de.igelstudios.igelengine.client.lang.GraphChar;
import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.util.Tickable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextBatch extends Batch<GraphChar>implements Tickable {
    public TextBatch(int size) {
        super(size, new Shader("text"), true,false,2,3,4);
    }

    @Override
    public boolean dirtyCheck(List<GraphChar> objs,BatchSupplier<GraphChar> supplier) {
        boolean dirty = false;
        for (int i = 0; i < objs.size(); i++) {
            GraphChar obj = objs.get(i);
            if(!obj.life() || obj.toRemove()){
                clear(i,objs);
                objs.remove(i);
                dirty = true;
                continue;
            }
            if(obj.isDirty()){
                add(i,obj,supplier);
                dirty = true;
            }
        }
        return dirty;
    }


    @Override
    public void addP(int j, GraphChar obj) {
        Char chat = obj.getFont().get(obj.getChat());
        int k = j;
        for (int m = 0; m < 4; m++) {

            vertices[j] = obj.getPos().x + (Texture.TEX_COORDS[0][m].x * chat.getWith() * obj.getScale());
            vertices[j + 1] = obj.getPos().y + (Texture.TEX_COORDS[0][m].y * chat.getHeight() * obj.getScale());

            vertices[j + 2] = ((float) obj.getFont().getTex());
            vertices[j + 3] = chat.getTexCords()[(int) Texture.TEX_COORDS[0][m].x].x;
            vertices[j + 4] = chat.getTexCords()[(int) Texture.TEX_COORDS[0][m].y].y * 1.125f;

            vertices[j + 5] = obj.getR();
            vertices[j + 6] = obj.getG();
            vertices[j + 7] = obj.getB();
            vertices[j + 8] = obj.getA();

            j += 9;
        }
        obj.unMarkDirty();
    }

    @Override
    public void tick() {
        for (int i = 0; i < Renderer.get(id).getTextSupplier().getT().size(); i++) {
            Renderer.get(id).getTextSupplier().getT().get(i).decrement();
        }
        //Renderer.get().getTextSupplier().getT().forEach(GraphChar::decrement);
    }
}
