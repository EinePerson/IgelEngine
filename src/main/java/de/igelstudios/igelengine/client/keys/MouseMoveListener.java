package de.igelstudios.igelengine.client.keys;

/**
 * this interface is for every Class that wants to get informed for every mouse move regardless if the mouse is being dragged
 */
public interface MouseMoveListener extends Listener{
    void mouseMove(double x,double y);
}
