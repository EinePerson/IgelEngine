package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.client.ClientScene;
import de.igelstudios.igelengine.client.graphics.batch.BatchSupplier;
import de.igelstudios.igelengine.client.graphics.batch.ObjectBatch;
import de.igelstudios.igelengine.client.graphics.batch.TextBatch;
import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.scene.SceneObject;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private static Renderer renderer;

    public static Renderer get() {
        return renderer;
    }

    private TextBatch textBatch;
    private ObjectBatch objectBatch;
    private ClientScene scene;
    private TextSupplier textSupplier;
    private ObjectSupplier objectSupplier;

    public Renderer(ClientScene scene){
        textBatch = new TextBatch(80 * 45);
        objectBatch = new ObjectBatch(80 * 45);
        this.scene = scene;
        textSupplier = new TextSupplier();
        objectSupplier = new ObjectSupplier();
        renderer = this;
        render();
    }

    public void render(Text text,float x,float y){
        if(!text.life())text.setLifeTime(-1);
        render(text,x,y,text.getLifeTime());
    }

    public void render(Text text,float x,float y,int lifetime){
        text.setPos(new Vector2f(x,y)).setLifeTime(lifetime).setColor(1.0f,0.0f,0.0f);
        textBatch.add(textSupplier.texts.size(),text);
        textSupplier.texts.add(text);
    }

    public void render(SceneObject obj,float x,float y){
        obj.setPos(new Vector2f(x,y));
        objectBatch.add(scene.getObjects().size(),obj);
        objectSupplier.objs.add(obj);
        scene.addObject(obj);
    }

    public void render(){
        objectBatch.render(objectSupplier);
        textBatch.render(textSupplier);
    }

    public ClientScene getScene() {
        return scene;
    }

    public class TextSupplier implements BatchSupplier<Text>{
        private List<Text> texts;

        public TextSupplier(){
            texts = new ArrayList<>();
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
            for (int k = 0; k < texts.size() && k < i; k++) {
                j += texts.get(k).getContent().length();
            }
            return j;
        }

        @Override
        public Matrix4f getProjMat() {
            return Renderer.this.scene.getProjMat();
        }

        @Override
        public Matrix4f getViewMat() {
            return Renderer.this.scene.getViewMat();
        }
    }

    public class ObjectSupplier implements BatchSupplier<SceneObject>{
        private List<SceneObject> objs;

        public ObjectSupplier(){
            objs = new ArrayList<>();
        }

        @Override
        public List<SceneObject> getT() {
            return objs;
        }

        @Override
        public int getSize() {
            return objs.size();
        }

        @Override
        public int getSize(int i) {
            return i;
        }

        @Override
        public Matrix4f getProjMat() {
            return Renderer.this.scene.getProjMat();
        }

        @Override
        public Matrix4f getViewMat() {
            return Renderer.this.scene.getViewMat();
        }
    }

    public TextBatch getTextBatch() {
        return textBatch;
    }

    public TextSupplier getTextSupplier() {
        return textSupplier;
    }
}
