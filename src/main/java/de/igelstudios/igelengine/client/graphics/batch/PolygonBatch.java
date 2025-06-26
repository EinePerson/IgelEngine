package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.graphics.Polygon;
import de.igelstudios.igelengine.client.graphics.shader.Shader;
import org.joml.Vector2f;

import java.util.List;

public class PolygonBatch extends Batch<Polygon>{
    public PolygonBatch(int size) {
        super(size, new Shader("line").noTexture(), true, true,2,4);
    }

    @Override
    public boolean dirtyCheck(List<Polygon> objs) {
        boolean dirty = false;
        for (int i = 0; i < objs.size(); i++) {
            if(objs.get(i).isDirty()){
                objs.get(i).unMarkDirty();
                add(i,objs.get(i));
                dirty = true;
            }
        }
        return dirty;
    }

    @Override
    protected void addP(int j, Polygon obj) {
        for (Vector2f coord : obj.getCoords()) {
            vertices[j] = coord.x;
            vertices[j + 1] = coord.y;
            vertices[j + 2] = obj.getR();
            vertices[j + 3] = obj.getG();
            vertices[j + 4] = obj.getB();
            vertices[j + 5] = obj.getA();
            j += 6;
        }
    }

    @Override
    protected void loadIndicesP(int[] indices, int startID,int startVertices,Polygon obj) {
        for (int i = 2; i < obj.getCoords().length; i++) {
            indices[startID] = 0 + startVertices;
            indices[startID + 1] = i - 1 + startVertices;
            indices[startID + 2] = i + startVertices;
            startID += 3;
        }
    }
}
