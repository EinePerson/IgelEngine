package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.keys.KeyHandler;
import de.igelstudios.igelengine.client.keys.KeyListener;
import de.igelstudios.igelengine.client.keys.MouseMoveListener;

public class GUIManager implements KeyListener, MouseMoveListener {
    private static GUIManager instance;
    private double x,y;
    private int selText;
    private GUI gui;

    private GUIManager(){
        instance = this;
    }

    @KeyHandler("LMB")
    public void lmb(boolean pressed){
        if(!pressed)return;
        if(gui == null)return;
        gui.getButtons().forEach(button -> {
            if(button.getPos().x <= x && button.getPos().x + button.getSize().x > x && button.getPos().y <= y && button.getPos().y + button.getSize().y > y){
                button.invoke(MouseButton.LMB);
            }
        });
        boolean set = false;
        for (int i = 0; i < gui.getTextFields().size(); i++) {
            TextField field = gui.getTextFields().get(i);
            if(field.getPos().x <= x && field.getPos().x + field.getSize().x > x && field.getPos().y <= y && field.getPos().y + field.getSize().y > y){
                selText = i;
                set = true;
                break;
            }
        }
        if(!set)selText = -1;
    }

    @KeyHandler("RMB")
    public void rmb(boolean pressed){
        if(!pressed)return;
        if(gui == null)return;
        gui.getButtons().forEach(button -> {
            if(button.getPos().x <= x && button.getPos().x + button.getSize().x > x && button.getPos().y <= y && button.getPos().y + button.getSize().y > y){
                button.invoke(MouseButton.RMB);
            }
        });
    }

    @KeyHandler("MMB")
    public void mmb(boolean pressed){
        if(!pressed)return;
        if(gui == null)return;
        gui.getButtons().forEach(button -> {
            if(button.getPos().x <= x && button.getPos().x + button.getSize().x > x && button.getPos().y <= y && button.getPos().y + button.getSize().y > y){
                button.invoke(MouseButton.MMB);
            }
        });
    }

    @Override
    public void mouseMove(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static GUIManager getInstance() {
        if(instance == null)instance = new GUIManager();
        return instance;
    }

    public void addText(char c){
        if(gui == null)return;
        if(selText != -1){
            if(c == 259)gui.getTextFields().get(selText).remove();
            if(c != 340 && c != 341 && c != 343) gui.getTextFields().get(selText).add(c);
        }
    }

    public boolean hasSelText(){
        return selText != -1;
    }

    public void setGui(GUI gui) {
        if(gui != null) removeGUI();
        this.gui = gui;
    }

    public void removeGUI(){
        gui.getTextFields().forEach(textfield -> textfield.getText().setLifeTime(0));
        gui.getTexts().forEach(text -> text.setLifeTime(0));
        this.gui = null;
    }
}
