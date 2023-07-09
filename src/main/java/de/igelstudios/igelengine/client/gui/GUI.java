package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.scene.SceneObject;

import java.util.ArrayList;
import java.util.List;

public abstract class GUI {
    //static GUI instance;
    private List<Button> buttons;
    private List<TextField> textFields;
    private List<Text> texts;
    private List<SceneObject> objects;

    public GUI(){
        buttons = new ArrayList<>();
        textFields = new ArrayList<>();
        texts = new ArrayList<>();
        objects = new ArrayList<>();

        //instance = this;
    }

    public void render(Text text,float x,float y){
        Renderer.get().render(text,x,y);
        texts.add(text);
    }


    public void render(Text text,float x,float y,int lifetime){
        Renderer.get().render(text,x,y,lifetime);
        texts.add(text);
    }

    public void render(SceneObject object,float x,float y){
        Renderer.get().render(object,x,y);
        objects.add(object);
    }

    @Deprecated
    public List<Button> getButtons() {
        return buttons;
    }

    @Deprecated
    public List<TextField> getTextFields() {
        return textFields;
    }

    @Deprecated
    public List<Text> getTexts() {
        return texts;
    }

    List<SceneObject> getObjects() {
        return objects;
    }

    public void addButton(Button button){
        buttons.add(button);
    }

    public void addTextField(TextField textField){
        textFields.add(textField);
    }

    public void addObject(SceneObject object){
        objects.add(object);
    }
}
