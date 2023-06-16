package de.igelstudios.game.events;

import java.util.Random;

public class EventSystem {
    private int ticks;
    private int tickCount;
    private int activeEvent;
    private Event[] events;

    public EventSystem(Event[] events){
        this.events = events;
    }
    public void tick() {
        if(activeEvent == -1 && tickCount >= 2*ticks){
            int activeEvent = new Random().nextInt(events.length);
            tickCount = 0;
            ticks = events [activeEvent].getTime();
        }
        else if(activeEvent != -1 && tickCount <= ticks){
            events [activeEvent].execute();
            tickCount ++;
        }
        else {
            activeEvent = -1;
        }
    }
}
