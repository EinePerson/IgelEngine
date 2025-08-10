package de.igelstudios.igelengine.common;

import de.igelstudios.igelengine.common.util.Tickable;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Main Engine class, it runs until it is told to stop, {@link #tick()} 20 times per second and {@link #second()} once a second as well as {@link #loop()} as often as the computer can handle
 */
public abstract class Engine {
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
        //double loops = 1000000000d / maxLoops;
        double delta = 0d;
        double loopDelta = 0d;
        long org = System.nanoTime();
        long sTimer = 0;
        started();
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
            //loop();
            /*if(loopDelta >= loops){
                loopDelta = 0;
                loop();
            }*/
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
     * called every iteration of the loop (as often as possible)
     */
    public void loop(){

    }

    /**
     * Overridable methode for subclasses to get notified when the Engine stops
     */
    public void stopSub(){

    }

    /**
     * this may be overridden to be called whenever the game loop starts
     */
    protected void started(){

    }

    /**
     * Signals to stop the Engine
     */
    public final void stop(){
        running = false;
    }

    /**
     * adds an Object to get {@link #tick()} 20 times per second
     * @param tickable the class to tick
     * @see #removeTickable(Tickable)
     * @see Tickable
     */
    public void addTickable(Tickable tickable){
        tickables.add(tickable);
    }

    /**
     * removes an object from the Ticking list
     * @param tickable the object
     * @see #addTickable(Tickable)
     * @see Tickable
     */
    public void removeTickable(Tickable tickable){
        tickables.remove(tickable);
    }
}
