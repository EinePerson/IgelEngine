package de.igelstudios.igelengine.common;

import de.igelstudios.igelengine.common.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public abstract class Engine {
    public static final int maxLoops = 60;//this is basically max fps
    private final List<Tickable> tickables;
    private boolean running;

    public Engine(){
        tickables = new ArrayList<>();
    }
    public void start(){
        running = true;
        run();
    }

    public final void run() {
        running = true;
        double ticks = 1000000000d / 20d;
        double loops = 1000000000d / maxLoops;
        double delta = 0d;
        double loopDelta = 0d;
        long org = System.nanoTime();
        long sTimer = 0;
        while (running && shouldRun()){
            long t = System.nanoTime();
            loopDelta += (t - org);
            delta += (t - org) / ticks;
            sTimer += t - org;
            org = t;
            if(delta >= 1){
                --delta;
                tick();
                tickables.forEach(Tickable::tick);
            }

            if(sTimer >= 1000000000){
                sTimer -= 1000000000;
                second();
            }
            if(loopDelta >= loops){
                loopDelta = 0;
                loop();
            }
        }
        stopSub();
        System.exit(0);
    }


    /**
     * Called every second
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
     */

    public abstract void tick();

    /**
     * called {@link #maxLoops} times per second
     */
    public void loop(){

    }

    public void stopSub(){

    }

    public final void stop(){
        running = false;
    }

    public void addTickable(Tickable tickable){
        tickables.add(tickable);
    }

    public void removeTickable(Tickable tickable){
        tickables.remove(tickable);
    }
}
