package de.igelstudios.igelengine.common.startup;

/**
 * This interface is to be implemented by all Main classes the functions are called for the Game to initialize its base state
 * @see KeyInitializer
 * @see ServerInitializer
 */
public interface EngineInitializer {
    /**
     * This should be used to register all Keys that may be used as well as their listeners
     * @param paramKeyInitializer in this class the Keys/Listeners may be added
     */
    void registerKeys(KeyInitializer paramKeyInitializer);

    /**
     * This is like a standard main method
     */
    void onInitialize();

    /**
     * This method is called when the Engine terminates to run cleanup code
     */
    void onEnd();
}
