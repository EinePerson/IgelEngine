package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.ClientEngine;
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
    private List<Clickable> clickables;
    private List<CheckBox> checkBoxes;
    private List<Button> buttons;
    private List<TextField> textFields;
    private List<Text> texts;
    private List<SceneObject> objects;
    private List<Polygon> polygons;
    private List<Line> lines;
    protected final int windowId;
    int selText = -1;

    public GUI() {
        this(0,true);
    }

    public GUI(int windowId) {
        this(windowId,false);
    }

    private GUI(int windowId,boolean singleWindowCheck) {
        if(singleWindowCheck) ClientEngine.singleWindowCheck();
        this.windowId = windowId;

        checkBoxes = new ArrayList<>();
        buttons = new ArrayList<>();
        textFields = new ArrayList<>();
        texts = new ArrayList<>();
        objects = new ArrayList<>();
        polygons = new ArrayList<>();
        lines = new ArrayList<>();
        clickables = new ArrayList<>();

        //instance = this;
    }

    /**
     * Renders a text
     *
     * @param text the text to render
     * @param x    the x coordinate of the text
     * @param y    the y coordinate of the text
     * @see #render(Text,float,float,int)
     */
    public void render(Text text,float x,float y) {
        Renderer.get(windowId).render(text,x,y);
        texts.add(text);
    }

    /**
     * Renders a text
     *
     * @param text     the text to render
     * @param x        the x coordinate of the text
     * @param y        the y coordinate of the text
     * @param lifetime the lifetime of the text in 20th of seconds
     * @see #render(Text,float,float,int)
     */
    public void render(Text text,float x,float y,int lifetime) {
        Renderer.get(windowId).render(text,x,y,lifetime);
        texts.add(text);
    }

    /**
     * Renders a SceneObject
     *
     * @param object the text to render
     * @param x      the x coordinate of the text
     * @param y      the y coordinate of the text
     */
    public void render(SceneObject object,float x,float y) {
        Renderer.get(windowId).render(object,x,y);
        objects.add(object);
    }

    /**
     * Displays a polygon
     *
     * @param polygon the polygon to display
     */
    public void render(Polygon polygon) {
        Renderer.get(windowId).render(polygon);
        polygons.add(polygon);
    }

    /**
     * Displays a line
     *
     * @param line the line to display
     */
    public void render(Line line) {
        Renderer.get(windowId).render(line);
        lines.add(line);
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public List<TextField> getTextFields() {
        return textFields;
    }

    public List<Text> getTexts() {
        return texts;
    }

    public List<SceneObject> getObjects() {
        return objects;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public void addButton(Button button) {
        buttons.add(button);
        clickables.add(button);
        if(button.hasLabel()) render(button.getLabel(),button.getPos().x,button.getPos().y);
    }

    public void addCheckBox(CheckBox checkBox) {
        checkBoxes.add(checkBox);
        clickables.add(checkBox);
        render(checkBox.getLabel(),checkBox.getPos().x + 1,checkBox.getPos().y);
        render(checkBox.getOutline());
        render(checkBox.getInside());
    }

    public void addTextField(TextField textField) {
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

    public void addObject(SceneObject object) {
        objects.add(object);
    }

    public int getWindowId() {
        return windowId;
    }

    public void removeButton(Button button) {
        buttons.remove(button);
        clickables.remove(button);
        if(button.hasLabel()) button.getLabel().setLifeTime(0);
    }

    List<Clickable> getClickables() {
        return clickables;
    }
}
