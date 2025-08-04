package de.igelstudios.igelengine.common.startup;

/**
 * This class is used to run setup and cleanup code on a server, it must be implemented by every class wishing to start the Engine
 * @see EngineInitializer
 */
public interface ServerInitializer {
    /**
     * This is like a standard main method
     */
    void onInitialize();

    /**
     * This method is called when the Engine terminates to run cleanup code
     */
    void onEnd();
}
