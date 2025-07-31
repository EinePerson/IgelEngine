package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.graphics.Line;
import de.igelstudios.igelengine.client.graphics.Polygon;
import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.scene.SceneObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the GUI super class, all objects that want a GUI like display should inherit this and should be registered using {@link GUIManager#setGUI(GUI)}<br>
 * when the GUI is removed, the screen is cleared from all objects added with the render methods of this class
 */
public abstract class GUI {
    //static GUI instance;
    private List<Button> buttons;
    private List<TextField> textFields;
    private List<Text> texts;
    private List<SceneObject> objects;
    private List<Polygon> polygons;
    private List<Line> lines;
    private final int windowId;

    public GUI(int windowId){
        this.windowId = windowId;

        buttons = new ArrayList<>();
        textFields = new ArrayList<>();
        texts = new ArrayList<>();
        objects = new ArrayList<>();
        polygons = new ArrayList<>();
        lines = new ArrayList<>();

        //instance = this;
    }

    /**
     * Renders a text
     * @param text the text to render
     * @param x the x coordinate of the text
     * @param y the y coordinate of the text
     * @see #render(Text, float, float, int)
     */
    public void render(Text text,float x,float y){
        Renderer.get(windowId).render(text,x,y);
        texts.add(text);
    }

    /**
     * Renders a text
     * @param text the text to render
     * @param x the x coordinate of the text
     * @param y the y coordinate of the text
     * @param lifetime the lifetime of the text in 20th of seconds
     * @see #render(Text, float, float, int)
     */
    public void render(Text text,float x,float y,int lifetime){
        Renderer.get(windowId).render(text,x,y,lifetime);
        texts.add(text);
    }

    /**
     * Renders a SceneObject
     * @param object the text to render
     * @param x the x coordinate of the text
     * @param y the y coordinate of the text
     */
    public void render(SceneObject object,float x,float y){
        Renderer.get(windowId).render(object,x,y);
        objects.add(object);
    }

    /**
     * Displays a polygon
     * @param polygon the polygon to display
     */
    public void render(Polygon polygon){
        Renderer.get(windowId).render(polygon);
        polygons.add(polygon);
    }

    /**
     * Displays a line
     * @param line the line to display
     */
    public void render(Line line){
        Renderer.get(windowId).render(line);
        lines.add(line);
    }

    List<Button> getButtons() {
        return buttons;
    }

    List<TextField> getTextFields() {
        return textFields;
    }

    List<Text> getTexts() {
        return texts;
    }

    List<SceneObject> getObjects() {
        return objects;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public void addButton(Button button){
        buttons.add(button);
        if(button.hasLabel())render(button.getLabel(),button.getPos().x,button.getPos().y);
    }

    public void addTextField(TextField textField){
        textField.init(windowId);
        textFields.add(textField);
        if(textField.hasBackground()){
            render(textField.getBackGround());
        }
        if(textField.hasLabel()){
            render(textField.getLabel(),textField.getPos().x - textField.getLabel().getFullVisualLength() - 0.5f,textField.getPos().y);
        }
    }

    public List<Line> getLines() {
        return lines;
    }

    public void addObject(SceneObject object){
        objects.add(object);
    }
}
