package de.igelstudios.igelengine.common.networking.client;

import java.util.UUID;

/**
 * This is the super class of every class that is some form of player, it has to be accosiated with a UUID for identification and it may be constructed in a PlayerFactory
 * @see de.igelstudios.igelengine.common.util.PlayerFactory
 * @see Client
 * @see de.igelstudios.igelengine.common.networking.server.Server
 */
public interface ClientNet {

    public UUID getUUID();

    public void setUUID(UUID uuid);
}
