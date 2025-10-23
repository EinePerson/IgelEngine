package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.graphics.RenderDataSupplier;
import org.joml.Matrix4f;

import java.util.List;

public interface BatchSupplier<T> extends RenderDataSupplier {

    /**
     * The objects that are Supplied;
     * @return every Object to be supplied
     */
    List<T> getT();

    int getSize();

    int getSize(int i);

    int getIndicesSize(int i);

    int getIndicesSize();

    int getVertexCount();

    void clear();

    void add(T t);
}
