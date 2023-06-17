package de.igelstudios.game.config;

import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.io.Config;

public class ClientConfig {
    private Config config;

    public ClientConfig(){
        config = new Config("client");
        Text.init((String) config.get("lang","en_us"));
    }
}
