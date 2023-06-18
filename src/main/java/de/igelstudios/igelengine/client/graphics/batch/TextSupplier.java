package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.ClientScene;
import de.igelstudios.igelengine.client.lang.Text;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class TextSupplier implements BatchSupplier<Text>{
    private TextBatch batch;
    private List<Text> texts;
    private ClientScene scene;

    public TextSupplier(ClientScene scene){
        batch = new TextBatch(30);
        texts = new ArrayList<>();
        this.scene = scene;
    }

    public void add(Text text){
        batch.add(texts.size(),text);
        texts.add(text);
    }

    @Override
    public List<Text> getT() {
        return texts;
    }

    @Override
    public int getSize() {
        int i = 0;
        for (Text text : texts) {
            i += text.getContent().length();
        }
        return i;
    }

    @Override
    public int getSize(int i) {
        int j = 0;
        for (int k = 0; k < texts.size() && k <= i; k++) {
            j += texts.get(k).getContent().length();
        }
        return j;
    }

    @Override
    public Matrix4f getProjMat() {
        return scene.getProjMat();
    }

    @Override
    public Matrix4f getViewMat() {
        return scene.getViewMat();
    }

    public void render(){
        batch.render(this);
    }
}
