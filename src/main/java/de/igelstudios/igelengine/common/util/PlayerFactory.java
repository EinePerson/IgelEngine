package de.igelstudios.igelengine.common.util;

import de.igelstudios.igelengine.common.networking.client.ClientNet;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;


/**
 * Used to create a {@link ClientNet} as Player (From networking perspective)
 */
public class PlayerFactory {
    private static Class<? extends ClientNet> clazz;
    private static boolean inConstructor;

    /**
     * This should be used to set the class that represents a player from a networking perspective
     * @param clazz the Class, can be obtained by CLASSNAME.class
     * @param incConstructor used to indicate weather the UUID can be set in the constructor or not;
     */
    public static void setPlayerClass(Class<? extends ClientNet> clazz,boolean incConstructor){
        PlayerFactory.clazz = clazz;
        PlayerFactory.inConstructor = incConstructor;
    }

    /**
     * Gets the player Corresponding to the UUID
     * @param uuid the UUID
     * @return the player as an instance of the class set in {@link #setPlayerClass(Class, boolean)}
     */
    public static ClientNet get(UUID uuid) {
        if(clazz == null)throw new RuntimeException("The class of the PlayerFactory has to be set before networking can be used");
        if (inConstructor){
            try {
                return clazz.getConstructor(UUID.class).newInstance(uuid);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                ClientNet net = clazz.getConstructor().newInstance();
                net.setUUID(uuid);
                return net;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
