package de.igelstudios.igelengine.client.al;

import de.igelstudios.ClientMain;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.libc.LibCStdlib;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SoundBuffer {
    private final static Map<String, SoundBuffer> sounds = new HashMap<>();

    private int audId;
    private SoundBuffer(String file){
        try(InputStream stream = Objects.requireNonNull(ClientMain.class.getClassLoader().getResourceAsStream(file))) {
            byte[] bytes = stream.readAllBytes();
            ByteBuffer bp = BufferUtils.createByteBuffer(bytes.length);
            bp.put(bytes);
            bp.rewind();
            IntBuffer ip = BufferUtils.createIntBuffer(1);
            IntBuffer ip2 = BufferUtils.createIntBuffer(1);
            ShortBuffer sp = STBVorbis.stb_vorbis_decode_memory(bp,ip,ip2);
            if(sp == null)throw new RuntimeException("Could not load audio: " + file);
            audId = AL11.alGenBuffers();
            AL11.alBufferData(audId,ip.get() == 1 ? AL11.AL_FORMAT_MONO16:AL11.AL_FORMAT_STEREO16,sp,ip2.get());

            LibCStdlib.free(sp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return audId;
    }

    public static SoundBuffer get(String file){
        if(!sounds.containsKey(file)) {
            sounds.put(file,new SoundBuffer(file));
        }
        return sounds.get(file);
    }
}
