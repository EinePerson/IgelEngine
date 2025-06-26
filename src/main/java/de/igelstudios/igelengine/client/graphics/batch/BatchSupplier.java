package de.igelstudios.igelengine.client.graphics.batch;

import org.joml.Matrix4f;

import java.util.List;

public interface BatchSupplier<T> {

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

    /**
     * If the Batch to which this is supplied has {@link Batch#movable} set to false this will never be used and should return null;
     * @return either a Projection Matrix or null
     */
    Matrix4f getProjMat();

    /**
     * If the Batch to which this is supplied has {@link Batch#movable} set to false this will never be used and should return null;
     * @return either a View Matrix or null
     */
    Matrix4f getViewMat();
}
