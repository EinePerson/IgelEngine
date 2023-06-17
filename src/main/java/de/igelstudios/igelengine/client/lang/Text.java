package de.igelstudios.igelengine.client.lang;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;

public final class Text {
    private static Map<String,String> translatable;
    private final String content;
    private static boolean init = false;
    private Text(String content){
        this.content = content;
    }

    public static void init(String lang){
        translatable = new Gson().fromJson(new InputStreamReader(Objects.requireNonNull(Text.class.getClassLoader().getResourceAsStream("lang/" + lang + ".json"))),new TypeToken<Map<String, String>>(){}.getType());
        init = true;
    }

    public static Text literal(String content){
        return new Text(content);
    }

    public static Text translatable(String key){
        if(!init)throw new IllegalStateException("Texts hava to be initialised before being utilised");
        String v = translatable.get(key);
        return v != null ? new Text(v):new Text(key);
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Text:" + content;
    }
}
