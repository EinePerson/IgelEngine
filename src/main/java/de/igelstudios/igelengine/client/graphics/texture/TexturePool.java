package de.igelstudios.igelengine.client.graphics.texture;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This keeps track of all textures currently in use and returns new textures for their respective path
 */
public class TexturePool {
    private static final Map<Integer, Texture> textures = new HashMap<>();
    private static final Map<String,Integer> texIDs = new HashMap<>();

    /**
     * Creates a usable texture
     * @param path the path in the resources folder
     * @return the Texture to use
     */
    public static Texture get(String path){
        if(!texIDs.containsKey(path)){
            int i;
            if(textures.isEmpty())i = 0;
            else i = Collections.max(textures.keySet(),Integer::compareTo) + 1;
            textures.put(i,new Texture(path,i));
            texIDs.put(path,i);
        }
        return textures.get(texIDs.get(path));
    }

    public static Texture getI(String path){
        if(!texIDs.containsKey(path)){
            int i;
            if(textures.isEmpty())i = 0;
            else i = Collections.max(textures.keySet(),Integer::compareTo) + 1;
            textures.put(i,new Texture(path,false));
            texIDs.put(path,i);
        }
        return textures.get(texIDs.get(path));
    }

    public static int texCount(){
        return textures.size();
    }

    public static Collection<Texture> get(){
        return textures.values();
    }

    public static int getID(String name){
        return  getID(get(name));
    }

    public static int getID(Texture tex) {
        return texIDs.get(tex.getPath());
    }

    public static void bind(String path){
        textures.get(texIDs.get(path)).bind();
    }
    public static void unbind(String path){
        textures.get(texIDs.get(path)).unbind();
    }

    public static Map<Integer, Texture> getTextures() {
        return textures;
    }
}
