package de.igelstudios.igelengine.client;

import de.igelstudios.igelengine.client.graphics.Renderer;
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

//Test

/**
 * This class contains a window where Graphics can be drawn to
 */
public class Window{
    //private static Window instance;
    public static final String TITLE = "Test";
    private String title;
    private int width, height;
    private final int orgWidth,orgHeight;

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    private final Thread localThread;
    private final long window;
    private boolean resized;
    private long audioDevice;
    private long audioContext;
    private List<String> audioDevices;
    private int id;

    private static volatile int selectedWindowID = -1;

    /**
     * creates a window with the desired parameters
     * @param width the width of the window, a width of 1 results in the width of the monitor
     * @param height the height of the window, a height of 1 results in the height of the monitor
     * @param title the title the window should hold
     * @param windowed weather the window should be in windowed or fullscreen mode
     * @see Window#Window(String,int)  Window
     */
    public Window(int width, int height,String title ,boolean windowed,int id) {
        this.title = title;
        this.id = id;
        localThread = Thread.currentThread();

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
        this.orgWidth = width;
        this.orgHeight = height;

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        //glfwWindowHint(GLFW_DECORATED,GLFW_FALSE);
        if(windowed)glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);

        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) throw new IllegalStateException("Could not create window");

        //if(windowed) glfwMaximizeWindow(window);
        //else
        glfwSetWindowPos(window,0,0);

        glfwRequestWindowAttention(window);
        try(MemoryStack stack = MemoryStack.stackPush()) {
            Texture.TextureInfo info = new Texture.TextureInfo();
            ByteBuffer img = Texture.read("Icon.png",info);
            GLFWImage glfwImg = GLFWImage.malloc();
            glfwImg.set(info.ip0().get(),info.ip1().get(),img);
            glfwSetWindowIcon(window, GLFWImage.calloc(1,stack).put(0,glfwImg));
        }
        //glfwSetWindowSizeLimits(window, width, height, width, height);
        glfwSetFramebufferSizeCallback(window, (this::resize));
        glfwSetWindowFocusCallback(window,this::focus);

        updateSoundDevices();
        //createAudio(audioDevices.get(0));

        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE,GL_ONE_MINUS_SRC_ALPHA);

        //instance = this;
    }

    private void focus(long ptr,boolean b) {
        if(b) selectedWindowID = ClientEngine.findID(ptr);
    }

    public static int getSelectedWindowID() {
        return selectedWindowID;
    }

    /**
     * Creates a window with the specified title
     * @param title the title of the window
     * @see Window#Window (int, int, String, boolean) Window
     */
    public Window(String title,int id) {
        this(1,1,title,true,id);
    }

    public void pollEvents(){
        //if(this.id == selectedWindowID)
            glfwPollEvents();
    }

    /**
     * This creates an Audio context for the specified device
     * @param device the name of the device to be used
     */
    public void createAudio(String device){
        if(audioDevice != MemoryUtil.NULL)closeAudio();
        updateSoundDevices();
        //if(!device.contains("OpenAL Soft on "))device = "OpenAL Soft on " + device;
        if(!audioDevices.contains(device) && (!device.contains("OpenAL Soft on ") && !audioDevices.contains("OpenAL Soft on " + device)))throw new RuntimeException("The Audio Device " + device + " could not be found");

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

    /**
     * closes the window and audio system
     * @see #closeAudio()
     */
    public void close() {
        closeAudio();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public static void terminate(){
        glfwTerminate();
    }

    public void closeMonitor(){
        glfwDestroyWindow(window);
    }

    /**
     * closes only the Audio system
     * @see #close()
     */
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

    /**
     * resizes the window to the specified size
     * @param width the new width of the window
     * @param height the new height of the window
     */
    public void resize(long ptr,int width, int height) {
        ClientEngine.enforceRenderThread(() -> {
            resized = true;
            this.width = width;
            this.height = height;
            glViewport((int) Renderer.get(id).getCamera().getPos().x, (int) Renderer.get(id).getCamera().getPos().y, width, height);
        },ClientEngine.findID(ptr));
    }

    public long getWindow() {
        return window;
    }

    /**
     * @return a list of all names of all present audio devices
     */
    public List<String> getAudioDevices() {
        return audioDevices;
    }
}