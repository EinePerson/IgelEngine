package de.igelstudios.igelengine.common;

import de.igelstudios.igelengine.common.scene.Scene;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public abstract class Engine {
    protected static final int TPS = 20;

    private boolean running;

    public final void run() {
        running = true;
        double ticks = 1000000000d / TPS;
        double delta = 0d;
        long org = System.nanoTime();
        long sTimer = 0;
        while (running && shouldRun()){
            long t = System.nanoTime();
            delta += (t - org) / ticks;
            if(delta >= 1){
                long deltat = t - org;
                sTimer += deltat;
                org = t;
                --delta;
                tick(deltat);
            }

            if(sTimer >= 1000000000){
                sTimer -= 1000000000;
                second();
            }
            loop();
        }
    }


    /**
     * Called about every second
     */
    public void second(){

    }


    /**
     * an overridable method for a child class to say if the engine should stop
     * @return weather the engine should continue to run
     */
    public boolean shouldRun(){
        return true;
    }

    /**
     * Called Every Time a tick occurs
     * @param deltat the time passed since the last tick
     */

    public abstract void tick(long deltat);

    /**
     * called every itteration of the loop
     */
    public void loop(){

    }

    public final void stop(){
        running = false;
    }
}
