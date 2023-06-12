package de.igelstudios.game;

import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.keys.KeyHandler;
import de.igelstudios.igelengine.client.keys.KeyListener;

public class Test implements KeyListener {

    @KeyHandler("left")
    public void left(boolean pressed){
        ClientMain.getInstance().getEngine().getScene().getCam();
    }
}
