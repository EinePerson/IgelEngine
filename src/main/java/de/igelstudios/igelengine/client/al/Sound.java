package de.igelstudios.igelengine.client.al;

import org.joml.Vector3f;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

/**
 * This represents a sound that is played
 */
public class Sound {
    private int srcId;
    private int loop;
    private SoundBuffer buf;

    /**
     * Makes a new Playable Sound
     * @param loop weather and how often this should loop upon playing,-1 is forever, 0 and 1 means once everything else is the number of loops
     * @param relative weather the Position is relative to the Listener
     * @param pos the Position of the Sound
     * @param buf the Sound buffer Obtainable with {@link SoundBuffer#get(String)}
     */
    public Sound(int loop, boolean relative, Vector3f pos,SoundBuffer buf){
        srcId = AL11.alGenSources();
        if(loop == -1)AL11.alSourcei(srcId, AL10.AL_LOOPING,AL10.AL_TRUE);
        if(relative)AL11.alSourcei(srcId,AL10.AL_SOURCE_RELATIVE,AL10.AL_TRUE);
        AL11.alSource3f(srcId,AL10.AL_POSITION,pos.x,pos.y,pos.z);
        AL11.alSourcei(srcId,AL10.AL_BUFFER,buf.getId());
        this.loop = loop;
        this.buf = buf;
    }

    /**
     * plays the sound for {@link #loop} times
     */
    public void play(){
        if(loop > 1) for (int i = 0; i < loop; i++) {
            AL11.alSourceQueueBuffers(srcId,buf.getId());
        }
        AL11.alSourcePlay(srcId);
    }

    public void stop(){
        AL11.alSourceStop(srcId);
    }
    public boolean isPlaying() {
        return AL11.alGetSourcei(srcId, AL11.AL_SOURCE_STATE) == AL11.AL_PLAYING;
    }

    public void pause() {
        AL11.alSourcePause(srcId);
    }

    public void delete(){
        stop();
        AL11.alDeleteSources(srcId);
    }
}
