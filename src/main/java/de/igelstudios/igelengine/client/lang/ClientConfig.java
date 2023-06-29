package de.igelstudios.igelengine.client.lang;

import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.io.Config;

public class ClientConfig {
    private static ClientConfig instance;
    private Config config;

    public ClientConfig(){
        config = new Config("client");
        Text.init((String) config.get("lang","en_us"));
        instance = this;
    }

    public static Config getConfig() {
        return instance.config;
    }
}
