package de.igelstudios.igelengine.client.graphics.batch;

public interface BatchContent {
    int getLength();
    int formerLength();

    boolean isDirty(int windowID);
    void unMarkDirty(int windowID);
    void markDirty();
}
