package de.igelstudios.igelengine.client.graphics;

public interface RenderObject {
    boolean isDirty(int windowID);
    void unMarkDirty(int windowID);
    void markDirty();
}
