package de.igelstudios.igelengine.client.graphics.texture;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TexturePool {
    private final Map<Integer, Texture> textures;
    private final Map<String,Integer> texIDs;
    public TexturePool(){
        textures = new HashMap<>();
        texIDs = new HashMap<>();
    }

    public Texture get(String path){
        if(!texIDs.containsKey(path)){
            int i;
            if(textures.isEmpty())i = 0;
            else i = Collections.max(textures.keySet(),Integer::compareTo) + 1;
            textures.put(i,new Texture(path));
            texIDs.put(path,i);
        }
        return textures.get(texIDs.get(path));
    }

    public Texture getI(String path){
        if(!texIDs.containsKey(path)){
            int i;
            if(textures.isEmpty())i = 0;
            else i = Collections.max(textures.keySet(),Integer::compareTo) + 1;
            textures.put(i,new Texture(path,false));
            texIDs.put(path,i);
        }
        return textures.get(texIDs.get(path));
    }

    public int texCount(){
        return textures.size();
    }

    public Collection<Texture> get(){
        return textures.values();
    }

    public int getID(String name){
        return  getID(get(name));
    }

    public int getID(Texture tex) {
        return texIDs.get(tex.getPath());
    }

    public void bind(String path){
        textures.get(texIDs.get(path)).bind();
    }
    public void unbind(String path){
        textures.get(texIDs.get(path)).unbind();
    }

    public Map<Integer, Texture> getTextures() {
        return textures;
    }
}
