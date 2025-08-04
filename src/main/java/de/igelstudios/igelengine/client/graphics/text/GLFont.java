package de.igelstudios.igelengine.client.graphics.text;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.igelstudios.igelengine.client.graphics.texture.TexturePool;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;

public class GLFont {
    private Map<Integer,Char> chars;
    private int tex;

    public GLFont(String name){
        chars = new Gson().fromJson(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("fonts/" + name + ".json"))),new TypeToken<Map<Integer, Char>>(){}.getType());
        tex = TexturePool.get("fonts/" + name + ".png").getID();
    }

    public Char get(int c){
        return chars.getOrDefault(c,new Char(0,0,0,0));
    }

    public int getTex() {
        return tex;
    }
}
