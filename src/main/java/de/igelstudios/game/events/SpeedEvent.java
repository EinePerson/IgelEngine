package de.igelstudios.game.events;

public class SpeedEvent extends Event{

    @Override
    public int getTime() {
        return 1000;
    }

    @Override
    public String getName() {
        return "SpeedEvent";
    }

    @Override
    public void execute() {

    }


}
