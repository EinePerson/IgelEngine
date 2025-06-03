package de.igelstudios.igelengine.client.lang;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.graphics.batch.BatchContent;
import de.igelstudios.igelengine.client.graphics.text.GLFont;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Text{
    private static Map<String,String> translatable;
    private String content;
    private static boolean init = false;
    private float r,g,b;
    private Vector2f pos;
    private float scale;
    private GLFont font;
    private int lifeTime = -1;

    private boolean changed;
    private List<GraphChar> chars;

    private Text(String content){
        this.content = content;
        font = ClientEngine.getDefaultFont();
        r = 0;
        g = 0;
        b = 0;
        scale = 0.0078125f;
        chars = new ArrayList<>();
        pos = new Vector2f(0.0f);
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
        chars.forEach(graphChar -> graphChar.setLifeTime(lifeTime));
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
        chars.forEach(graphChar -> graphChar.setFont(font));
        return this;
    }

    public Text setPos(Vector2f pos) {
        this.pos = pos;
        float i = 0;
        for (GraphChar chat : chars) {
            chat.setPos(new Vector2f(pos.x + i,pos.y));
            i += (font.get(chat.getChat()).getWith() * scale);
        }
        return this;
    }

    public Text setScale(float scale) {
        this.scale = scale / 128;
        chars.forEach(graphChar -> graphChar.setScale(this.scale));
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

        chars.forEach(graphChar -> {
            graphChar.setColor(r,g,b);
            graphChar.markDirty();
        });

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

    /*public void content(String c){
        content = c;
        changed = true;
    }*/
    public void add(char c){
        content += c;
        float i = 0;
        for (GraphChar chat : chars) {
            i += chat.getFont().get(chat.getChat()).getWith() * scale;
        }
        GraphChar graphChar = new GraphChar(c,new Vector2f(pos.x + i,pos.y),lifeTime,scale,r,g,b,font);
        chars.add(graphChar);
        Renderer.get().render(graphChar);
        changed = true;
    }

    public void remove(){
        content = content.substring(0,content.length() - 1);
        changed = true;
        chars.get(chars.size() - 1).remove();
        chars.remove(chars.size() - 1);
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

    public List<GraphChar> getChars() {
        return chars;
    }

    public Text update(){
        char[] charArr = content.toCharArray();
        float j = 0;
        if(chars.size() == 0){
            for (int i = 0; i < charArr.length; i++) {
                chars.add(new GraphChar(charArr[i],new Vector2f(pos.x + j,pos.y),lifeTime,scale,r,g,b,font));
                j += chars.get(i).getFont().get(chars.get(i).getChat()).getWith() * scale;
            }
            return this;
        }
        for (int i = 0; i < charArr.length; i++) {
            if(chars.size() > i && !(chars.get(i).getChat() == charArr[i])) chars.add(i,new GraphChar(charArr[i],new Vector2f(pos.x + j,pos.y),lifeTime,scale,r,g,b,font));
            j += chars.get(i).getFont().get(chars.get(i).getChat()).getWith() * scale;
        }
        return this;
    }
}
