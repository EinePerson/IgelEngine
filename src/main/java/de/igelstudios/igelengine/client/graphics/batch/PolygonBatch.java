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
    public boolean dirtyCheck(List<Polygon> objs,BatchSupplier<Polygon> supplier) {
        boolean dirty = false;
        for (int i = 0; i < objs.size(); i++) {
            if(objs.get(i).toRemove()){
                clear(i,objs);

                dirty = true;
                continue;
            }
            if(objs.get(i).isDirty(id)){
                add(i,objs.get(i),supplier);
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
        int indOffset = 0;
        for (int j = 0;j < i && j < objs.size(); j++) {
            indOffset += (objs.get(j).getLength() - 2);
        }

        int fullIndOffset = 0;
        for (int j = i; j < objs.size(); j++) {
            fullIndOffset += (objs.get(j).getLength() - 2);
        }

        indOffset *= 3;
        fullIndOffset *= 3;
        fullIndOffset += indOffset;

        int k = 0;
        for (int j = 0; j < objs.size() && j < i; j++) {
            k += objs.get(j).formerLength();
        }
        k *= totalInBits;
        int l = k + totalInBits * objs.get(i).formerLength();
        for (int j = k; j < l; j++) {
            vertices[j] = 0;
        }

        System.arraycopy(vertices,l,vertices,k,vertices.length - l);

        /*for (int j = i; j < objs.size(); j++) {
            add(j,objs.get(j));
        }*/

        int offset = (objs.get(i).getLength() - 2) * 3;
        for(int j = indOffset + (objs.get(i).getLength() - 2) * 3;j < fullIndOffset;j++){
            indices[j - offset] = indices[j] - objs.get(i).getLength();
        }

        for (int j = fullIndOffset - (objs.get(i).getLength() - 2) * 3; j < fullIndOffset; j++) {
            indices[j] = 0;
        }

        objs.remove(i);
        objs.get(i).unMarkDirty(id);
    }
}
