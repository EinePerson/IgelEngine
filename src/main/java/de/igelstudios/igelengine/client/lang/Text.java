package de.igelstudios.igelengine.client.lang;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.graphics.batch.BatchContent;
import de.igelstudios.igelengine.client.graphics.text.GLFont;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;

public final class Text implements BatchContent {
    private static Map<String,String> translatable;
    private String content;
    private static boolean init = false;
    private float r,g,b;
    private Vector2f pos;
    private float scale;
    private GLFont font;
    private int lifeTime = -1;

    private boolean changed;

    private Text(String content){
        this.content = content;
        font = ClientEngine.getDefaultFont();
        r = 0;
        g = 0;
        b = 0;
        scale = 1.0f;
    }

    public static void init(String lang){
        translatable = new Gson().fromJson(new InputStreamReader(Objects.requireNonNull(ClientMain.class.getClassLoader().getResourceAsStream("lang/" + lang + ".json"))),new TypeToken<Map<String, String>>(){}.getType());
        init = true;
    }

    public boolean life(){
        return  lifeTime != 0;
    }

    public Text setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
        return this;
    }

    public void decrement(){
        if(lifeTime > 0)lifeTime--;
    }

    public static Text literal(String content){
        return new Text(content);
    }

    public static Text translatable(String key){
        if(!init)throw new IllegalStateException("Texts hava to be initialised before being utilised");
        String v = translatable.get(key);
        return v != null ? new Text(v):new Text(key);
    }

    public Text setFont(GLFont font){
        this.font = font;
        return this;
    }

    public Text setPos(Vector2f pos) {
        this.pos = pos;
        return this;
    }

    public Text setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public float getScale() {
        return scale;
    }

    public Vector2f getPos() {
        return pos;
    }

    public Text setColor(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
        return this;
    }

    public float getB() {
        return b;
    }

    public float getG() {
        return g;
    }

    public float getR() {
        return r;
    }

    public GLFont getFont() {
        return font;
    }

    public Vector3f getColor(){
        return new Vector3f(r,g,b);
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Text:" + content;
    }

    @Override
    public int getLength() {
        return content.length();
    }

    public void content(String c){
        content = c;
        changed = true;
    }

    public void applied(){
        changed = false;
    }

    public boolean hasChanged() {
        return changed;
    }

    public int getLifeTime() {
        return lifeTime;
    }
}
