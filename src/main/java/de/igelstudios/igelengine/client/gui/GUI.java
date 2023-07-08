package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.lang.Text;

import java.util.ArrayList;
import java.util.List;

public class GUI {
    //static GUI instance;
    private List<Button> buttons;
    private List<TextField> textFields;
    private List<Text> texts;

    public GUI(){
        buttons = new ArrayList<>();
        textFields = new ArrayList<>();

        //instance = this;
    }

    public void render(Text text,float x,float y){
        Renderer.get().render(text,x,y);
        texts.add(text);
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
}
