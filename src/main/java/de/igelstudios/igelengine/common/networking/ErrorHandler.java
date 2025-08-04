package de.igelstudios.igelengine.common.networking;

/**
 * This is a simple hook that should handle networking errors that occur
 */
public interface ErrorHandler {

    /**
     * The method called when an error occurred
     * @param cause the reason for the exception
     */
    void handle(Throwable cause);
}
