package de.igelstudios.igelengine.client.gui;

import de.igelstudios.igelengine.client.Window;
import de.igelstudios.igelengine.client.graphics.Line;
import de.igelstudios.igelengine.client.graphics.Polygon;
import de.igelstudios.igelengine.client.keys.*;
import de.igelstudios.igelengine.common.scene.SceneObject;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT;

/**
 * in this class, one can set the currently shown GUI (only 1) with {@link #setGUI(GUI)}
 */
public class GUIManager implements MouseClickListener/*, MouseMoveListener*/ {
    //private static List<GUIManager> instances = new ArrayList<>();
    //private double x,y;
    private final List<GUI> guis = new ArrayList<>();
    private boolean changed = false;
    private static GUIManager instance;

    private GUIManager(){
        instance = this;
    }

    @KeyHandler("LMB")
    public void lmb(boolean pressed,double x,double y){
        if(!pressed)return;
        GUI gui = guis.get(Window.getSelectedWindowID());
        changed = false;
        if(gui == null)return;
        for (Clickable button : gui.getClickables()) {
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
                changeTextField(i);
                set = true;
                break;
            }
        }
        if(!set)gui.selText = -1;
    }

    @KeyHandler("RMB")
    public void rmb(boolean pressed,double x,double y){
        if(!pressed)return;
        GUI gui = guis.get(Window.getSelectedWindowID());
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
        GUI gui = guis.get(Window.getSelectedWindowID());
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

    private void changeTextField(int newSelected){
        GUI gui = guis.get(Window.getSelectedWindowID());
        if(gui.selText != -1 && gui.getTextFields().get(gui.selText).hasBackground()) {
            int oldColor = gui.getTextFields().get(gui.selText).getBackGroundColor();
            gui.getTextFields().get(gui.selText).getBackGround().setRGBA(((oldColor >> 24) & 0xFF) / 255.0f, ((oldColor >> 16) & 0xFF) / 255.0f, ((oldColor >> 8) & 0xFF) / 255.0f, (oldColor & 0xFF) / 255.0f);
        }

        gui.selText = newSelected;

        if(gui.selText != -1 && gui.getTextFields().get(gui.selText).hasBackground()) {
            int oldColor = gui.getTextFields().get(gui.selText).getSelectedBackgroundColor();
            gui.getTextFields().get(gui.selText).getBackGround().setRGBA(((oldColor >> 24) & 0xFF) / 255.0f, ((oldColor >> 16) & 0xFF) / 255.0f, ((oldColor >> 8) & 0xFF) / 255.0f, (oldColor & 0xFF) / 255.0f);
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
    public static synchronized void setGUI(GUI gui){
        instance.guis.set(gui.windowId, gui);
    }

    public void addText(int c){
        GUI gui = guis.get(Window.getSelectedWindowID());
        if(gui == null)return;
        if(gui.selText != -1){
            if(c == 259 && gui.getTextFields().get(gui.selText).getLength() > 0)gui.getTextFields().get(gui.selText).remove();
            if(c >= 46 && c <= 122) gui.getTextFields().get(gui.selText).add((char) c);
        }
    }

    public boolean hasSelText(int windowID){
        return guis.get(windowID).selText != -1;
    }

    private synchronized void setGui(GUI gui) {
        if(gui == null)return;
        changed = true;
        if(guis.get(gui.windowId) != null) removeGUI(gui.windowId);
        gui.selText = -1;
        guis.set(gui.windowId, gui);
    }

    /**
     * Removes the current GUI
     */
    public static synchronized void removeGui(int id){
        if(instance.guis.get(id) != null)instance.removeGUI(id);
    }

    private void removeGUI(int windowID){
        GUI gui = guis.get(windowID);
        gui.getTextFields().forEach(textfield -> textfield.getText().setLifeTime(0));
        gui.getTexts().forEach(text -> text.setLifeTime(0));
        gui.getObjects().forEach(SceneObject::remove);
        gui.getPolygons().forEach(Polygon::remove);
        gui.getLines().forEach(Line::remove);
        guis.set(windowID,null);
    }

    public static boolean handle(int mods,int key,int id) {
        if(instance.guis.get(id) == null)return false;
        if (instance.hasSelText(Window.getSelectedWindowID())) {
            if(key == GLFW.GLFW_KEY_ESCAPE){

                instance.changeTextField(-1);
                return true;
            }

            if(key == GLFW.GLFW_KEY_ENTER && instance.hasSelText(Window.getSelectedWindowID()) && instance.guis.get(id).getTextFields().get(instance.guis.get(id).selText).hasNext()){
                instance.changeTextField(instance.guis.get(id).getTextFields().indexOf(instance.guis.get(id).getTextFields().get(instance.guis.get(id).selText).getNextField()));
            }

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

    public static void register(){
        if(instance == null){
            new GUIManager();
            HIDInput.registerMouseClickListener(instance);
            HIDInput.activateListener(instance);
        }
        instance.guis.add(null);
        //input.registerMoveListener(instance);
    }

    public static synchronized GUI getGui(int id) {
        return instance.guis.get(id);
    }
}
