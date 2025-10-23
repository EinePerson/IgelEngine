package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.graphics.RenderObject;

public interface BatchContent extends RenderObject {
    int getLength();
    int formerLength();
}
