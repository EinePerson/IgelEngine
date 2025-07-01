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
            if(objs.get(i).toRemove()){
                clear(i,objs);
                objs.remove(i);

                dirty = true;
                continue;
            }
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
            indices[startID] = startVertices;
            indices[startID + 1] = i - 1 + startVertices;
            indices[startID + 2] = i + startVertices;
            startID += 3;
        }
    }

    @Override
    public void clearP(int i, List<Polygon> objs) {
        int offset = 0;
        for (int j = 0; j < objs.size() && j < i; j++) {
            offset += objs.get(j).getLength() * 6;
        }

        int afterOffset = offset + objs.get(i).getLength() * 6;

        System.arraycopy(vertices,afterOffset,vertices,offset,vertices.length - afterOffset);

        int indOffset = 0;
        for (int j = 0; j < objs.size() && j < i; j++) {
            indOffset += (objs.get(j).getLength() - 2);
        }

        int fullOffset = indOffset;

        for (int j = i; j < objs.size(); j++) {
            fullOffset += (objs.get(j).getLength() - 2);
        }

        indOffset *= 3;
        fullOffset *= 3;

        int afterIndOffset = indOffset + (objs.get(i).getLength() - 2) * 3;

        System.arraycopy(indices,indOffset,indices,afterIndOffset,indices.length - afterIndOffset);

        for(int j = fullOffset;j < fullOffset + (objs.get(i).getLength() - 2) * 3;j++){
            indices[j] = 0;
        }
    }
}
