package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.client.ClientScene;
import de.igelstudios.igelengine.client.graphics.batch.BatchSupplier;
import de.igelstudios.igelengine.client.graphics.batch.ObjectBatch;
import de.igelstudios.igelengine.client.graphics.batch.TextBatch;
import de.igelstudios.igelengine.client.lang.GraphChar;
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

    /**
     * Displays a text
     * @param text the text to be displayed
     * @param x the x coordinate
     * @param y the y coordinate
     * if this is used in a gui {@link de.igelstudios.igelengine.client.gui.GUI#render(Text, float, float)} should be used
     */
    public void render(Text text,float x,float y){
        if(!text.life())text.setLifeTime(-1);
        render(text,x,y,text.getLifeTime());
    }

    /**
     * Displays a text
     * @param text the text to be displayed
     * @param x the x coordinate
     * @param y the y coordinate
     * @param lifetime the amount of ticks this should be displayed(default/infinite is -1)
     * if this is used in a gui {@link de.igelstudios.igelengine.client.gui.GUI#render(Text, float, float,int)} should be used
     */
    public void render(Text text,float x,float y,int lifetime){
        text.setPos(new Vector2f(x,y)).setLifeTime(lifetime);
        text.update();
        text.getChars().forEach(graphChar -> {
            textBatch.add(textSupplier.texts.size(),graphChar);
            textSupplier.texts.add(graphChar);
        });
    }

    public void render(GraphChar graphChar){
        textBatch.add(textSupplier.texts.size(),graphChar);
        textSupplier.texts.add(graphChar);
    }

    public void render(SceneObject obj,float x,float y){
        obj.removed();
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

    public class TextSupplier implements BatchSupplier<GraphChar>{
        private List<GraphChar> texts;

        public TextSupplier(){
            texts = new ArrayList<>();
        }

        @Override
        public List<GraphChar> getT() {
            return texts;
        }

        @Override
        public int getSize() {
            return texts.size();
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
