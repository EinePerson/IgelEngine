package de.igelstudios.igelengine.client.gui;

import java.util.ArrayList;
import java.util.List;

public class GUI {
    //static GUI instance;
    private List<Button> buttons;
    private List<TextField> textFields;

    public GUI(){
        buttons = new ArrayList<>();
        textFields = new ArrayList<>();

        //instance = this;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public List<TextField> getTextFields() {
        return textFields;
    }
}
