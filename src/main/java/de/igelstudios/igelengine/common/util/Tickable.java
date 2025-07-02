package de.igelstudios.igelengine.common.util;

/**
 * This interface is used to get an update every tick of the engine(20 times per Second) to execute steadily running Game logic
 */
public interface Tickable {
    void tick();
}
