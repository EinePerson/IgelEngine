package de.igelstudios.game.events;

public abstract class Event {
    public abstract int getTime();
    public abstract String getName();
    public abstract void execute();
}
