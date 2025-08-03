package de.igelstudios.igelengine.client.graphics;

import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.ClientScene;
import de.igelstudios.igelengine.client.graphics.batch.*;
import de.igelstudios.igelengine.client.lang.GraphChar;
import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.scene.SceneObject;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the main Render class which dispatches draw calls and keeps track of everything that should be displayed
 */
public class Renderer {
    private static List<Renderer> clientRenderers = new ArrayList<>();

    /**
     * Obtains the global renderer to add objects for rendering, only available in single windowed mode
     * @return the global renderer
     * @see #get(int)
     */
    public static Renderer get() {
        ClientEngine.singleWindowCheck();
        return clientRenderers.getFirst();
    }

    /**
     * Obtains the main renderer of the specified window
     * @param window the window
     * @return the renderer of the specified object
     * @see #get()
     */
    public static Renderer get(int window) {
        return clientRenderers.get(window);
    }

    private TextBatch textBatch;
    private ObjectBatch objectBatch;
    private LineBatch lineBatch;
    private PolygonBatch polygonBatch;
    private BatchSupplier<GraphChar> textSupplier;
    private BatchSupplier<SceneObject> objectSupplier;
    private BatchSupplier<Line> lineSupplier;
    private BatchSupplier<Polygon> polygonSupplier;

    private Camera camera;
    private int id;

    public static void add(Renderer renderer) {
        clientRenderers.add(renderer);
    }

    /*public Renderer(Camera camera,int id,BatchSupplier<GraphChar> textSupplier,BatchSupplier<SceneObject> objectSupplier,BatchSupplier<Line> lineSupplier,BatchSupplier<Polygon> polygonSupplier) {
        this.id = id;
        textBatch = new TextBatch(80 * 45);
        textBatch.setId(id);
        objectBatch = new ObjectBatch(80 * 45);
        objectBatch.setId(id);
        lineBatch = new LineBatch(80 * 45);
        lineBatch.setId(id);
        polygonBatch = new PolygonBatch(80 * 45);
        polygonBatch.setId(id);
        this.textSupplier = textSupplier;
        this.objectSupplier = objectSupplier;
        this.lineSupplier = lineSupplier;
        this.polygonSupplier = polygonSupplier;

        this.camera = camera;
        render();
    }*/

    public Renderer(Camera camera, int id) {
        this.id = id;
        textBatch = new TextBatch(80 * 45);
        textBatch.setId(id);
        objectBatch = new ObjectBatch(80 * 45);
        objectBatch.setId(id);
        lineBatch = new LineBatch(80 * 45);
        lineBatch.setId(id);
        polygonBatch = new PolygonBatch(80 * 45);
        polygonBatch.setId(id);
        textSupplier = new TextSupplier();
        objectSupplier = new ObjectSupplier();
        lineSupplier = new LineSupplier();
        polygonSupplier = new PolygonSupplier();

        this.camera = camera;
        render();
    }

    /**
     * Displays a polygon
     *
     * @param polygon the polygon
     */
    public void render(Polygon polygon) {
        ClientEngine.enforceRenderThread(() -> {
            polygon.unMarkDirty();
            polygonBatch.add(polygonSupplier.getSize(), polygon,polygonSupplier);
            polygonSupplier.add(polygon);
        }, id);

    }

    /**
     * Displays a text
     *
     * @param text the text to be displayed
     * @param x    the x coordinate
     * @param y    the y coordinate
     *             if this is used in a gui {@link de.igelstudios.igelengine.client.gui.GUI#render(Text, float, float)} should be used
     */
    public void render(Text text, float x, float y) {
        if (!text.life()) text.setLifeTime(-1);
        render(text, x, y, text.getLifeTime());
    }

    /**
     * Displays a text
     *
     * @param text     the text to be displayed
     * @param x        the x coordinate
     * @param y        the y coordinate
     * @param lifetime the amount of ticks this should be displayed(default/infinite is -1)
     *                 if this is used in a gui {@link de.igelstudios.igelengine.client.gui.GUI#render(Text, float, float, int)} should be used
     */
    public void render(Text text, float x, float y, int lifetime) {
        text.setPos(new Vector2f(x, y));
        render(text,lifetime);
    }

    /**
     * Renders a non-decaying text at the position specified in the text object
     * @param text the text to render
     */
    public void render(Text text){
        render(text,-1);
    }

    /**
     * displays a text at the position specified in the Text object
     * @param text the text to display
     * @param lifetime the lifetime of the text
     */
    public void render(Text text,int lifetime){
        text.setWindowId(id);
        text.setLifeTime(lifetime);
        ClientEngine.enforceRenderThread(() -> {
            text.update();
            text.getChars().forEach(graphChar -> {
                textBatch.add(textSupplier.getSize(), graphChar,textSupplier);
                textSupplier.add(graphChar);
            });
        }, id);
    }

    /**
     * Displays a simple Line
     *
     * @param line the line to display
     */
    public void render(Line line) {
        ClientEngine.enforceRenderThread(() -> {
            line.removed();
            lineBatch.add(lineSupplier.getSize(), line,lineSupplier);
            lineSupplier.add(line);
        }, id);
    }

    public void render(GraphChar graphChar) {
        ClientEngine.enforceRenderThread(() -> {
            textBatch.add(textSupplier.getSize(), graphChar,textSupplier);
            textSupplier.add(graphChar);
        }, id);
    }

    /**
     * Displays the object at the given coordinates
     *
     * @param obj the object
     * @param x   the x Position
     * @param y   the y Position
     */
    public void render(SceneObject obj, float x, float y) {
        obj.setPos(new Vector2f(x, y));
        render(obj);
    }

    /**
     * Displays the object at the coordinates set in the object
     * @param obj the object to display
     */
    public void render(SceneObject obj){
        ClientEngine.enforceRenderThread(() -> {
            obj.removed();
            objectSupplier.add(obj);
        }, id);
    }

    public void render(){
        render(true);
    }

    public synchronized void render(boolean mainRenderer) {
        polygonBatch.render(polygonSupplier,mainRenderer);
        objectBatch.render(objectSupplier,mainRenderer);
        lineBatch.render(lineSupplier,mainRenderer);
        textBatch.render(textSupplier,mainRenderer);
    }

    /**
     * Clears the entire screen from all its objects
     */
    public void clear() {
        objectBatch.clearBatch();
        textBatch.clearBatch();
        objectSupplier.clear();
        textSupplier.clear();
        lineSupplier.clear();
        lineBatch.clearBatch();
        polygonSupplier.clear();
        polygonBatch.clearBatch();
    }

    public void clearObjects(){
        objectBatch.clearBatch();
        objectSupplier.clear();
    }

    public void clearText(){
        textBatch.clearBatch();
        textSupplier.clear();
    }

    public void clearLine(){
        lineBatch.clearBatch();
        lineSupplier.clear();
    }

    public void clearPolygon(){
        polygonBatch.clearBatch();
        polygonSupplier.clear();
    }

    public Camera getCamera() {
        return camera;
    }

    public class TextSupplier implements BatchSupplier<GraphChar> {
        private List<GraphChar> texts;

        public TextSupplier(List<GraphChar> texts) {
            this.texts = texts;
        }

        public TextSupplier() {
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
        public int getIndicesSize(int i) {
            return 0;
        }

        @Override
        public int getIndicesSize() {
            return 0;
        }

        @Override
        public int getVertexCount() {
            return getSize() * 6;
        }

        @Override
        public Matrix4f getProjMat() {
            return Renderer.this.camera.getProjMat();
        }

        @Override
        public Matrix4f getViewMat() {
            return Renderer.this.camera.getViewMat();
        }

        @Override
        public void clear() {
            texts.clear();
        }

        @Override
        public void add(GraphChar graphChar) {
            texts.add(graphChar);
        }
    }

    public class ObjectSupplier implements BatchSupplier<SceneObject> {
        private List<SceneObject> objs;

        public ObjectSupplier(List<SceneObject> objs) {
            this.objs = objs;
        }

        public ObjectSupplier() {
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
        public int getIndicesSize(int i) {
            return 0;
        }

        @Override
        public int getIndicesSize() {
            return 0;
        }

        @Override
        public int getVertexCount() {
            return getSize() * 6;
        }

        @Override
        public Matrix4f getProjMat() {
            return Renderer.this.camera.getProjMat();
        }

        @Override
        public Matrix4f getViewMat() {
            return Renderer.this.camera.getViewMat();
        }

        @Override
        public void clear() {
            objs.clear();
        }

        @Override
        public void add(SceneObject sceneObject) {
            objs.add(sceneObject);
        }
    }

    public class LineSupplier implements BatchSupplier<Line> {
        private List<Line> lines;
        //private List<SceneObject> objs;

        public LineSupplier(List<Line> lines) {
            this.lines = lines;
        }

        public LineSupplier() {
            lines = new ArrayList<>();
            //objs = new ArrayList<>();
        }

        @Override
        public List<Line> getT() {
            return lines;
        }

        @Override
        public int getSize() {
            return lines.size();
        }

        @Override
        public int getSize(int i) {
            return i;
        }

        @Override
        public int getIndicesSize(int i) {
            return 0;
        }

        @Override
        public int getIndicesSize() {
            return 0;
        }

        @Override
        public int getVertexCount() {
            return getSize() * 6;
        }

        @Override
        public Matrix4f getProjMat() {
            return Renderer.this.camera.getProjMat();
        }

        @Override
        public Matrix4f getViewMat() {
            return Renderer.this.camera.getViewMat();
        }

        @Override
        public void clear() {
            lines.clear();
        }

        @Override
        public void add(Line line) {
            lines.add(line);
        }
    }

    public class PolygonSupplier implements BatchSupplier<Polygon> {
        private List<Polygon> lines;

        public PolygonSupplier(List<Polygon> lines) {
            this.lines = lines;
        }

        public PolygonSupplier() {
            lines = new ArrayList<>();
        }

        @Override
        public List<Polygon> getT() {
            return lines;
        }

        @Override
        public int getSize() {
            int size = 0;
            for (Polygon line : lines) {
                size += line.getLength();
            }
            return size;
        }

        @Override
        public int getSize(int i) {
            int size = 0;
            for (int j = 0; j < i; j++) {
                size += lines.get(j).getLength();
            }
            return size;
        }

        @Override
        public int getIndicesSize(int i) {
            int size = 0;
            for (int j = 0; j < i; j++) {
                size += lines.get(j).getLength() - 2;
            }
            return size * 3;
        }

        @Override
        public int getIndicesSize() {
            int size = 0;
            for (Polygon line : lines) {
                size += line.getLength() - 2;
            }
            return size * 3;
        }

        @Override
        public int getVertexCount() {
            return getIndicesSize() * 3;
        }

        @Override
        public Matrix4f getProjMat() {
            return Renderer.this.camera.getProjMat();
        }

        @Override
        public Matrix4f getViewMat() {
            return Renderer.this.camera.getViewMat();
        }

        @Override
        public void clear() {
            lines.clear();
        }

        @Override
        public void add(Polygon polygon) {
            lines.add(polygon);
        }
    }

    public TextBatch getTextBatch() {
        return textBatch;
    }

    public BatchSupplier<GraphChar> getTextSupplier() {
        return textSupplier;
    }
}
