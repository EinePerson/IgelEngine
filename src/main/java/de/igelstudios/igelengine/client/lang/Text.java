package de.igelstudios.igelengine.client.lang;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.graphics.Renderer;
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
    private float r,g,b,a;
    private Vector2f pos;
    private float scale;
    private GLFont font;
    private int lifeTime = -1;

    private boolean changed;
    private List<GraphChar> chars;
    private List<Text> childTexts;
    private boolean charsDirty = true;
    private List<GraphChar> fullCharList;

    private Text(String content){
        this.content = content;
        font = ClientEngine.getDefaultFont();
        r = 0;
        g = 0;
        b = 0;
        a = 0;
        scale = 0.0078125f;
        chars = new ArrayList<>();
        pos = new Vector2f(0.0f);
        childTexts = new ArrayList<>();
        fullCharList = new ArrayList<>();
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
        childTexts.forEach(text -> text.setLifeTime(lifeTime));
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
        charsDirty = true;

        return this;
    }

    public Text setColor(float r, float g, float b,float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        chars.forEach(graphChar -> {
            graphChar.setColor(r,g,b,a);
            graphChar.markDirty();
        });
        charsDirty = true;

        return this;
    }

    public Text setA(float a) {
        this.a = a;

        chars.forEach(graphChar -> {
            graphChar.setColor(r,g,b,a);
            graphChar.markDirty();
        });
        charsDirty = true;

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

    public float getA() {
        return a;
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

        GraphChar graphChar = new GraphChar(c,new Vector2f(pos.x + getVisualLength(),pos.y),lifeTime,scale,r,g,b,font);
        chars.add(graphChar);
        Renderer.get().render(graphChar);
        changed = true;
    }

    public float getVisualLength(){
        float i = 0;
        for (GraphChar chat : chars) {
            i += chat.getFont().get(chat.getChat()).getWith() * scale;
        }
        return i;
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
        if(checkDirty()){
            fullCharList = getFullCharList();
        }
        return fullCharList;
    }

    public Text update(){
        char[] charArr = content.toCharArray();
        float j = 0;
        if(chars.isEmpty()){
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

    private boolean checkDirty(){
        boolean dirty = charsDirty;
        for (Text childText : childTexts) {
            if(childText.checkDirty())dirty = true;
        }

        charsDirty = false;
        update();
        return dirty;
    }

    private List<GraphChar> getFullCharList(){
        fullCharList.clear();

        fullCharList.addAll(chars);
        float x = getVisualLength();
        for (Text childText : childTexts) {
            childText.setPos(new Vector2f(pos.x + x,pos.y));
            x += childText.getFullVisualLength();
            fullCharList.addAll(childText.getFullCharList());
        }

        return fullCharList;
    }

    public float getFullVisualLength(){
        float length = getVisualLength();

        for (Text childText : childTexts) {
            length += childText.getFullVisualLength();
        }

        return length;
    }

    public Text append(Text text){
        childTexts.add(text);
        charsDirty = true;

        return this;
    }
}
