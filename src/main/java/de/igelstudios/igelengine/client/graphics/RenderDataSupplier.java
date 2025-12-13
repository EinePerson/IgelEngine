package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.client.graphics.batch.Batch;
import org.joml.Matrix4f;

public interface RenderDataSupplier {
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

    Camera getCamera();
}
