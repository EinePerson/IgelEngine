package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.graphics.Line;
import de.igelstudios.igelengine.client.graphics.shader.Shader;

import java.util.List;

public class LineBatch extends Batch<Line>{

    public LineBatch(int size) {
        super(size, new Shader("line").noTexture(),true,2,4);
    }

    @Override
    public boolean dirtyCheck(List<Line> objects) {
        boolean dirty = false;
        for (int i = 0; i < objects.size(); i++) {
            if(objects.get(i).toRemove()){
                clear(i,objects);
                //objects.get(i).removed();
                objects.remove(i);
                dirty = true;
                continue;
            }
            if(objects.get(i).isDirty()){
                add(i,objects.get(i));
                dirty = true;
            }
        }
        return dirty;
    }

    @Override
    protected void addP(int j, Line obj) {
        vertices[j] = obj.getStart().x;
        vertices[j + 1] = obj.getStart().y;
        vertices[j + 2] = obj.getR();
        vertices[j + 3] = obj.getG();
        vertices[j + 4] = obj.getB();
        vertices[j + 5] = obj.getA();

        j += 6;

        vertices[j] = obj.getEnd().x;
        vertices[j + 1] = obj.getEnd().y;
        vertices[j + 2] = obj.getR();
        vertices[j + 3] = obj.getG();
        vertices[j + 4] = obj.getB();
        vertices[j + 5] = obj.getA();

        j += 6;

        vertices[j] = obj.getEndUp().x;
        vertices[j + 1] = obj.getEndUp().y;
        vertices[j + 2] = obj.getR();
        vertices[j + 3] = obj.getG();
        vertices[j + 4] = obj.getB();
        vertices[j + 5] = obj.getA();

        j += 6;

        vertices[j] = obj.getStartUp().x;
        vertices[j + 1] = obj.getStartUp().y;
        vertices[j + 2] = obj.getR();
        vertices[j + 3] = obj.getG();
        vertices[j + 4] = obj.getB();
        vertices[j + 5] = obj.getA();

        obj.unMarkDirty();
    }
}
