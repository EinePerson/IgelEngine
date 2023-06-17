package de.igelstudios.igelengine.common;

public abstract class Engine {

    private boolean running;
    public void start(){
        running = true;
        run();
    }

    public final void run() {
        running = true;
        double ticks = 1000000000d / 20;
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
     * @param deltat the time passed since the last tick
     */

    public abstract void tick(long deltat);

    /**
     * called every iteration of the loop
     */
    public void loop(){

    }

    public final void stop(){
        running = false;
    }
}
