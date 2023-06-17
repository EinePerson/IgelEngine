package de.igelstudios.igelengine.client.sound;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to play sounds
 * It is a utility class and thus has a private Constructor
 */
@Deprecated
public class SoundManager {
    private SoundManager(){

    }
    private static final Map<Integer,Clip> clips = new HashMap<>();

    /**
     * This plays a sound without stopping the other sounds
     * @param sound the {@link Sound} to be played
     * @return the id the specific sound has
     * @see #play(Sound, boolean)
     */
    public static int play(Sound sound){
        return play(sound,false);
    }

    /**
     * This plays a sound
     * @param sound the specific sound to be played
     * @param stopRunning weather all other sounds should stop playing
     * @return the id the specific sound has
     */
    public static int play(Sound sound,boolean stopRunning){
        try {
            Clip clip = AudioSystem.getClip();
            stop();
            clip.open(sound.getStream());
            clip.start();
            clip.setFramePosition(0);
            int i = clips.isEmpty() ? 0:Collections.max(clips.keySet()) + 1;
            clips.put(i,clip);
            return i;
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * this loops a sound until manually stopped
     * @param sound the specific sound to be played
     * @return the id the specific sound has
     * @see #playContinuous(Sound, int)
     */
    public static int playContinuous(Sound sound){
        return playContinuous(sound,Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * this loops a sound
     * @param sound the specific sound to be looped
     * @param loops how often the sound should be looped
     * @return the id the specific sound has
     * @see #playContinuous(Sound)
     */
    public static int playContinuous(Sound sound,int loops){
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(sound.getStream());
            clip.start();
            clip.loop(loops);
            int i = clips.isEmpty() ? 0:Collections.max(clips.keySet()) + 1;
            clips.put(i,clip);
            return i;
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * this stops every playing sound
     */
    public static void stop(){
        clips.keySet().forEach(SoundManager::stop);
    }

    /**
     * This stops playing a sound
     * @param i the id of the sound to be stopped
     */
    public static void stop(int i){
        clips.get(i).stop();
        clips.get(i).close();
    }
}
