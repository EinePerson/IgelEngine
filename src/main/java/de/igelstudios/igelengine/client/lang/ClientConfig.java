package de.igelstudios.igelengine.client.lang;

import de.igelstudios.igelengine.common.io.Config;
import de.igelstudios.igelengine.common.io.EngineSettings;

public class ClientConfig {
    private static ClientConfig instance;
    private Config config;

    public ClientConfig(){
        config = new Config("client");
        Text.init((String) config.get("lang", EngineSettings.parser("info.json").read().getDefaultLang()));
        instance = this;
    }

    public static Config getConfig() {
        return instance.config;
    }
}
