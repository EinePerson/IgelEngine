package de.igelstudios.igelengine.client;

import de.igelstudios.igelengine.client.graphics.texture.Texture;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.openal.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.EXTThreadLocalContext.alcSetThreadContext;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window{
    private static Window instance;
    public static final String TITLE = "Test";
    private String title;
    private int width, height;

    public static int getWidth(){
        return instance.width;
    }
    public static int getHeight(){
        return instance.height;
    }
    private final long window;
    private boolean resized;
    private long audioDevice;
    private long audioContext;
    private List<String> audioDevices;

    public Window(int width, int height, boolean windowed) {
        title = TITLE;

        GLFWErrorCallback.createPrint(System.err).set();
        /*GLFWErrorCallback.create(new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                ClientMain.LOGGER.error(error + ":" + description);
            }
        }).set();*/

        if (!glfwInit()) throw new IllegalStateException("Could not init GLFW");

        if(width == 1 || height == 1){
            width = glfwGetVideoMode(glfwGetPrimaryMonitor()).width();
            height = glfwGetVideoMode(glfwGetPrimaryMonitor()).height();
        }
        this.width = width;
        this.height = height;

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED,GLFW_FALSE);
        if(windowed)glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);

        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) throw new IllegalStateException("Could not create window");

        if(windowed) glfwMaximizeWindow(window);
        else glfwSetWindowPos(window,0,0);

        glfwRequestWindowAttention(window);
        try(MemoryStack stack = MemoryStack.stackPush()) {
            Texture.TextureInfo info = new Texture.TextureInfo();
            ByteBuffer img = Texture.read("Icon.png",info);
            GLFWImage glfwImg = GLFWImage.malloc();
            glfwImg.set(info.ip0().get(),info.ip1().get(),img);
            glfwSetWindowIcon(window, GLFWImage.calloc(1,stack).put(0,glfwImg));
        }
        glfwSetWindowSizeLimits(window, width, height, width, height);
        glfwSetFramebufferSizeCallback(window, ((handle1, width1, height1) -> resize(width1,height1)));

        updateSoundDevices();
        createAudio(audioDevices.get(0));

        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE,GL_ONE_MINUS_SRC_ALPHA);
        instance = this;
    }

    public Window() {
        this(1,1,false);
    }

    public void pollEvents(){
        glfwPollEvents();
    }

    public void createAudio(String device){
        if(audioDevice != MemoryUtil.NULL)closeAudio();
        updateSoundDevices();
        if(!device.contains("OpenAL Soft on "))device = "OpenAL Soft on " + device;
        if(!audioDevices.contains(device))throw new RuntimeException("The Audio Device " + device + " could not be found");

        audioDevice = ALC11.alcOpenDevice(device);
        if (audioDevice == NULL) throw new IllegalStateException("Failed to open an OpenAL device.");

        ALCCapabilities capabilities = ALC.createCapabilities(audioDevice);

        if (!capabilities.OpenALC11) throw new IllegalStateException("Device does not support OpenAl 1.1");

        audioContext = ALC11.alcCreateContext(audioDevice, (IntBuffer)null);
        checkALCError(audioDevice);

        if (!(capabilities.ALC_EXT_thread_local_context && alcSetThreadContext(audioContext))) {
            if (!ALC11.alcMakeContextCurrent(audioContext)) throw new IllegalStateException("Could not create OpenAl context");
        }
        checkALCError(audioDevice);

        AL.createCapabilities(capabilities, MemoryUtil::memCallocPointer);
    }

    private void checkALCError(long aNull) {
        int err = alcGetError(aNull);
        if (err != ALC_NO_ERROR) throw new RuntimeException(alcGetString(aNull, err));
    }

    public void updateSoundDevices(){
        audioDevices = ALUtil.getStringList(NULL, ALC11.ALC_ALL_DEVICES_SPECIFIER);
    }

    public void close() {
        closeAudio();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void closeAudio(){
        if(audioDevice != NULL) ALC11.alcCloseDevice(audioDevice);
        if(audioContext != NULL) ALC11.alcDestroyContext(audioContext);
        alcMakeContextCurrent(NULL);
        AL.setCurrentThread(null);
        AL.setCurrentProcess(null);
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void resize(int width, int height) {
        resized = true;
        this.width = width;
        this.height = height;
    }

    public long getWindow() {
        return window;
    }

    public List<String> getAudioDevices() {
        return audioDevices;
    }
}