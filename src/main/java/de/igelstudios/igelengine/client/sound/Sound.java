package de.igelstudios.igelengine.client.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Objects;

/**
 * This class is used to play sounds using {@link SoundManager}
 */
@Deprecated()
public class Sound {
    private AudioInputStream stream;

    /**
     * This creates a new Audio
     * @param name the name of the file in the resources folder
     */
    public Sound(String name){
        try {
            stream = AudioSystem.getAudioInputStream(Objects.requireNonNull(this.getClass().getClassLoader().getResource(name)));
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    AudioInputStream getStream() {
        return stream;
    }
}

