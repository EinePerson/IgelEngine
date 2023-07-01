package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.graphics.shader.Shader;
import de.igelstudios.igelengine.client.graphics.text.Char;
import de.igelstudios.igelengine.client.graphics.texture.Texture;
import de.igelstudios.igelengine.client.graphics.texture.TexturePool;
import de.igelstudios.igelengine.client.lang.Text;

import java.util.List;

public class TextBatch extends Batch<Text> {
    public static final TexturePool pool = new TexturePool();
    public TextBatch(int size) {
        super(size, new Shader("text"), true,2,3,3);
    }

    @Override
    public boolean dirtyCheck(List<Text> objs) {
        boolean dirty = false;
        for (int i = 0; i < objs.size(); i++) {
            Text obj = objs.get(i);
            obj.decrement();
            if(!obj.life()){
                clear(i,objs);
                objs.remove(i);
                dirty = true;
            }
            if(obj.hasChanged()){
                clear(i,objs);
                add(i,obj);
                dirty = true;
                obj.applied();
            }
        }
        return dirty;
    }

    @Override
    protected TexturePool getTexs() {
        return pool;
    }

    @Override
    public void addP(int j, Text obj) {
        float x = obj.getPos().x;
        for (int i = 0; i < obj.getContent().length(); i++) {
            Char chat = obj.getFont().get(obj.getContent().charAt(i));
            float scale = obj.getScale() / 128;
            for (int m = 0; m < 4; m++) {

                vertices[j] = x + (Texture.TEX_COORDS[m].x * chat.getWith() * scale);
                vertices[j + 1] = obj.getPos().y + (Texture.TEX_COORDS[m].y * chat.getHeight() * scale);
                vertices[j + 2] = ((float) obj.getFont().getTex());
                vertices[j + 3] = chat.getTexCords()[(int) Texture.TEX_COORDS[m].x].x;
                vertices[j + 4] = chat.getTexCords()[(int) Texture.TEX_COORDS[m].y].y * 1.125f;

                vertices[j + 5] = obj.getR();
                vertices[j + 6] = obj.getG();
                vertices[j + 7] = obj.getB();

                j += 8;
            }
            x += chat.getWith() * scale;
        }
    }
}
