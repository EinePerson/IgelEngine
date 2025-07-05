package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.graphics.Line;
import de.igelstudios.igelengine.client.graphics.Polygon;
import de.igelstudios.igelengine.client.keys.*;
import de.igelstudios.igelengine.common.scene.SceneObject;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT;

/**
 * in this class, one can set the currently shown GUI (only 1) with {@link #setGUI(GUI)}
 */
public class GUIManager implements MouseClickListener/*, MouseMoveListener*/ {
    private static GUIManager instance;
    //private double x,y;
    private int selText;
    private GUI gui;
    private boolean changed = false;

    private GUIManager(){
        instance = this;
    }

    @KeyHandler("LMB")
    public void lmb(boolean pressed,double x,double y){
        changed = false;
        if(!pressed)return;
        if(gui == null)return;
        for (Button button : gui.getButtons()) {
            if(changed){
                changed = false;
                return;
            }
            if(button.getPos().x <= x && button.getPos().x + button.getSize().x > x && button.getPos().y <= y && button.getPos().y + button.getSize().y > y){
                button.invoke(MouseButton.LMB);
            }
        }
        if(gui == null)return;
        boolean set = false;
        for (int i = 0; i < gui.getTextFields().size(); i++) {
            if(changed){
                changed = false;
                return;
            }
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
    public void rmb(boolean pressed,double x,double y){
        if(!pressed)return;
        if(gui == null)return;
        for (Button button : gui.getButtons()) {
            if(changed){
                changed = false;
                return;
            }
            if(button.getPos().x <= x && button.getPos().x + button.getSize().x > x && button.getPos().y <= y && button.getPos().y + button.getSize().y > y){
                button.invoke(MouseButton.RMB);
            }
        }
    }

    @KeyHandler("MMB")
    public void mmb(boolean pressed,double x,double y){
        if(!pressed)return;
        if(gui == null)return;
        for (Button button : gui.getButtons()) {
            if(changed){
                changed = false;
                return;
            }
            if(button.getPos().x <= x && button.getPos().x + button.getSize().x > x && button.getPos().y <= y && button.getPos().y + button.getSize().y > y){
                button.invoke(MouseButton.MMB);
            }
        }
    }

    /*@Override
    public void mouseMove(double x, double y) {
        this.x = x;
        this.y = y;
    }*/

    /**
     * sets the current active GUI on the screen
     * @param gui the new GUI or null to remove the old GUI
     */
    public static void setGUI(GUI gui){
        instance.setGui(gui);
    }

    public void addText(int c){
        if(gui == null)return;
        if(selText != -1){
            if(c == 259 && gui.getTextFields().get(selText).getLength() > 0)gui.getTextFields().get(selText).remove();
            if(c >= 46 && c <= 122) gui.getTextFields().get(selText).add((char) c);
        }
    }

    public boolean hasSelText(){
        return selText != -1;
    }

    private void setGui(GUI gui) {
        selText = -1;
        changed = true;
        if(this.gui != null) removeGUI();
        if(gui == null)HIDInput.deactivateListener(this);
        else HIDInput.activateListener(this);
        this.gui = gui;
    }

    /**
     * Removes the current GUI
     */
    public static void removeGui(){
        if(instance.gui != null)instance.removeGUI();
    }

    private void removeGUI(){
        gui.getTextFields().forEach(textfield -> textfield.getText().setLifeTime(0));
        gui.getTexts().forEach(text -> text.setLifeTime(0));
        gui.getObjects().forEach(SceneObject::remove);
        gui.getPolygons().forEach(Polygon::remove);
        gui.getLines().forEach(Line::remove);
        this.gui = null;
    }

    public static boolean handle(int mods,int key) {
        if (instance.hasSelText()) {
            int keyS = key;
            if ((mods & GLFW_MOD_SHIFT) == 0) {
                keyS = Character.toLowerCase(key);
            } else if (key == 47) keyS = 95;
            else if (key == 46) keyS = 58;
            instance.addText(keyS);
            return true;
        }
        return false;
    }

    public static void register(HIDInput input){
        new GUIManager();
        input.registerMouseClickListener(instance);
        //input.registerMoveListener(instance);
    }

    public static GUI getGui() {
        return instance.gui;
    }
}
