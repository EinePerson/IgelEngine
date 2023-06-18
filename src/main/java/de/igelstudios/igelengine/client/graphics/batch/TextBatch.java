package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.graphics.shader.Shader;
import de.igelstudios.igelengine.client.graphics.text.Char;
import de.igelstudios.igelengine.client.graphics.texture.Texture;
import de.igelstudios.igelengine.client.lang.Text;

import java.util.List;

public class TextBatch extends Batch<Text> {
    public TextBatch(int size) {
        super(size, new Shader("text"), true,2,3,3);
    }

    @Override
    public boolean dirtyCheck(List<Text> objs) {
        return true;
    }

    @Override
    public void addP(int j, Text obj) {
        float x = obj.getPos().x;
        for (int i = 0; i < obj.getContent().length(); i++) {
            Char chat = obj.getFont().get(obj.getContent().charAt(i));
            float scale = obj.getScale() / 50;
            for (int m = 0; m < 4; m++) {

                vertices[j] = x + (Texture.TEX_COORDS[m].x * chat.getWith() * scale);
                vertices[j + 1] = obj.getPos().y + (Texture.TEX_COORDS[m].y * chat.getHeight() * scale);
                //vertices[j + 2] = ((float) Texture.get("fonts/Candaraz.png").getID());
                vertices[j + 2] = ((float) obj.getFont().getTex());
                vertices[j + 3] = chat.getTexCords()[(int) Texture.TEX_COORDS[m].x].x;
                vertices[j + 4] = chat.getTexCords()[(int) Texture.TEX_COORDS[m].y].y;

                vertices[j + 5] = obj.getR();
                vertices[j + 6] = obj.getG();
                vertices[j + 7] = obj.getB();

                j += 8;
            }
            System.out.println(j);
            x += chat.getWith() * scale;
        }
    }
}
