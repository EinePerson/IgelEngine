package de.igelstudios.igelengine.common.entity;

import java.util.UUID;

public class Player extends Entity{
    private UUID id;

    public Player(UUID id){
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
