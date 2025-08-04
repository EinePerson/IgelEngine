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
        if(!config.contains("lang"))config.write("lang",EngineSettings.parser("info.json").read().getDefaultLang());
        Text.init((String) config.get("lang"));
        instance = this;
    }

    public static Config getConfig() {
        return instance.config;
    }
}
