package de.igelstudios.igelengine.common.startup;

public interface EngineInitializer {
    void registerKeys(KeyInitializer paramKeyInitializer);

    void onInitialize();

    void onEnd();
}
