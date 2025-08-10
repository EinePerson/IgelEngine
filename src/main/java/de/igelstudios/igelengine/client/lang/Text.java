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

/**
 * This is the wrapper class used to render string to the screen
 * <br> instances may be created either via {@link #literal(String,Object...)} which copies the String as is or with a translation key in {@link #translatable(String,Object...)} which translates the key with the given value in the language or the key itself if no value is found
 * <br> a lifetime in 20th of seconds may be set after which the text will automatically disappear
 */
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
    private boolean completeDirty = true;

    private boolean[] windowAdded;

    private Text(String content,Object ... vals){
        this.content = String.format(content,vals);
        font = ClientEngine.getDefaultFont();
        r = 0;
        g = 0;
        b = 0;
        a = 1.0f;
        scale = 0.0078125f;
        chars = new ArrayList<>();
        pos = new Vector2f(0.0f);
        childTexts = new ArrayList<>();
        fullCharList = new ArrayList<>();

        windowAdded = new boolean[ClientEngine.getWindowCount()];

        update();
    }

    public void setWindowId(int windowId){
        windowAdded[windowId] = true;
    }

    public static void init(String lang){
        translatable = new Gson().fromJson(new InputStreamReader(Objects.requireNonNull(ClientMain.class.getClassLoader().getResourceAsStream("lang/" + lang + ".json"))),new TypeToken<Map<String, String>>(){}.getType());
        init = true;
    }

    public boolean life(){
        return  lifeTime != 0;
    }

    /**
     * sets the lifetime of a text in 20th of seconds, a value of -1 means the text is to be removed manually and not by code
     * @param lifeTime the lifetime in 20th of seconds
     * @return the object for chained modifications
     */
    public Text setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
        chars.forEach(graphChar -> graphChar.setLifeTime(lifeTime));
        childTexts.forEach(text -> text.setLifeTime(lifeTime));
        return this;
    }

    public void decrement(){
        if(lifeTime > 0)lifeTime--;
    }

    /**
     * Creates a Text object with the given text as text
     * @param content the text to show
     * @return a text object with the text
     * @see #translatable(String,Object...) 
     */
    public static Text literal(String content,Object ... vals){
        return new Text(content,vals);
    }

    /**
     * Creates a Text object where the key is translated in the language file or the key if no translation is set
     * @param key the key to lookup in the translation table
     * @return a translated Text object
     */
    public static Text translatable(String key,Object ... vals){
        if(!init)throw new IllegalStateException("Texts hava to be initialised before being utilised");
        String v = translatable.get(key);
        return v != null ? new Text(v,vals):new Text(key,vals);
    }

    /**
     * This set the font to use in the text
     * @param font the font object
     * @return the text for chained modification calls
     */
    public Text setFont(GLFont font){
        this.font = font;
        chars.forEach(graphChar -> graphChar.setFont(font));
        charsDirty = true;
        return this;
    }

    /**
     * This sets the new position of the Text object
     * @param pos the pos to move this text to
     * @return the Text for chained modification calls
     */
    public Text setPos(Vector2f pos) {
        this.pos = pos;
        float i = 0;
        for (GraphChar chat : chars) {
            chat.setPos(new Vector2f(pos.x + i,pos.y));
            i += (font.get(chat.getChat()).getWith() * scale);
        }

        charsDirty = true;
        return this;
    }

    public Text setPos(float x, float y) {
        setPos(new Vector2f(x,y));

        return this;
    }

    /**
     * This sets the scale of the text
     * @param scale the scale the text shall have
     * @return the Text for chained modification calls
     */
    public Text setScale(float scale) {
        this.scale = scale / 128;
        chars.forEach(graphChar -> graphChar.setScale(this.scale));

        charsDirty = true;
        updatePositions();
        return this;
    }

    public float getScale() {
        return scale;
    }

    public Vector2f getPos() {
        return pos;
    }

    /**
     * sets the color of the text, all values from 0 to 1
     * @param r the amount of red
     * @param g the amount of green
     * @param b the amount of blue
     * @return the Text for chained modification calls
     * @see #setColor(float, float, float, float)
     */
    public Text setColor(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;

        chars.forEach(graphChar -> {
            graphChar.setColor(r,g,b);
        });
        charsDirty = true;

        return this;
    }

    /**
     * sets the color of the text, all values from 0 to 1
     * @param r the amount of red
     * @param g the amount of green
     * @param b the amount of blue
     * @param a the alpha value
     * @return the Text for chained modification calls
     * @see #setColor(float, float, float)
     * @see #setA(float)
     */
    public Text setColor(float r, float g, float b,float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        chars.forEach(graphChar -> {
            graphChar.setColor(r,g,b,a);
        });
        charsDirty = true;

        return this;
    }

    /**
     * This sets only the Alpha value of the text
     * @param a the new Alpha value
     * @return the Text for chained modification calls
     */
    public Text setA(float a) {
        this.a = a;

        chars.forEach(graphChar -> {
            graphChar.setA(a);
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
        for(int i = 0; i < windowAdded.length; i++){
            if(windowAdded[i])Renderer.get(i).render(graphChar);
        }

        changed = true;
    }

    public float getHeight(){
        if(!chars.isEmpty())return chars.getFirst().getFont().get(chars.getFirst().getChat()).getHeight() * scale;
        return 0;
    }

    public float getVisualLength(){
        float j = 0;
        for (int i = 0; i < chars.size(); i++) {
            j += chars.get(i).getFont().get(chars.get(i).getChat()).getWith() * scale;
        }
        return j;
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

    public Text update(String newContent){
        if(newContent.length() == content.length()){
            content = newContent;
            changed = true;
            charsDirty = true;
            for(int i = 0; i < chars.size(); i++){
                chars.get(i).setChat(newContent.charAt(i));
            }

            return this;
        }
        content = newContent;
        changed = true;
        charsDirty = true;
        chars.forEach(GraphChar::remove);
        chars.clear();
        update();
        for(int i = 0; i < windowAdded.length; i++){
            if(windowAdded[i]){
                for(GraphChar chat : chars){
                    Renderer.get(i).render(chat);
                }
            }
        }


        return this;
    }

    private void updatePositions(){
        char[] charArr = content.toCharArray();
        float j = 0;

        for(int i = 0; i < chars.size(); i++){
            chars.get(i).setPos(new Vector2f(pos.x + j,pos.y));
            j += chars.get(i).getChar().getWith() * scale;
        }
    }

    public Text update(){
        char[] charArr = content.toCharArray();
        float j = 0;

        if(chars.isEmpty()){
            for(int i = 0; i < charArr.length; i++){
                chars.add(new GraphChar(charArr[i],new Vector2f(pos.x + j,pos.y),lifeTime,scale,r,g,b,font));
                j += chars.get(i).getChar().getWith() * scale;
            }
            return this;
        }

        if(charsDirty){
            for(int i = 0; i < chars.size(); i++){
                chars.get(i).setChat(charArr[i]);
                chars.get(i).setColor(r,g,b,a);
                chars.get(i).setFont(font);
                chars.get(i).setPos(new Vector2f(pos.x + j,pos.y));
                chars.get(i).setScale(scale);
                j += chars.get(i).getChar().getWith() * scale;
            }

            charsDirty = false;
            completeDirty = true;
        }else{
            j = chars.getLast().getPos().x - pos.x + chars.getLast().getChar().getWith() * scale;
        }

        if(changed){
            for(int i = chars.size(); i < charArr.length; i++){
                chars.add(i,new GraphChar(charArr[i],new Vector2f(pos.x + j,pos.y),lifeTime,scale,r,g,b,font));
                j += chars.get(i).getChar().getWith() * scale;
            }

            applied();
            completeDirty = true;
        }
        return this;
    }

    private boolean checkDirty(){
        if(charsDirty || changed) update();
        boolean dirty = completeDirty;

        for (Text childText : childTexts) {
            if(childText.checkDirty())dirty = true;
        }

        completeDirty = false;
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

    /**
     * returns the length the rendered text will have
     * @return the length in normalized screen sized
     */
    public float getFullVisualLength(){
        float length = getVisualLength();

        for (Text childText : childTexts) {
            length += childText.getFullVisualLength();
        }

        return length;
    }

    /**
     * adds a new Text as a child object, most modifications to the parent object will not affect the child one except for position modification
     * @param text the text to add as child
     * @return the Text for chained modification calls
     */
    public Text append(Text text){
        childTexts.add(text);
        charsDirty = true;

        return this;
    }
}
