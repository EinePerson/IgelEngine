package de.igelstudios.igelengine.client;

import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.io.Config;
import de.igelstudios.igelengine.common.io.EngineSettings;

/**
 * This is a global config for settings the player may make, a {@link Config} may be obtained from {@link #getConfig()}
 */
public class ClientConfig {
    private static ClientConfig instance;
    private Config config;

    public ClientConfig(){
        config = new Config("client");
        Text.init((String) config.getOrDefault("lang", EngineSettings.parser("info.json").read().getDefaultLang()));
        instance = this;
    }

    public static Config getConfig() {
        return instance.config;
    }
}
