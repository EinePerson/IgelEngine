package de.igelstudios.igelengine.client;

import de.igelstudios.ClientMain;
import de.igelstudios.ServerMain;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window{
    public static final String TITLE = "Test";
    private String title;
    private int width, height;
    private final long window;
    private boolean resized;

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
        if(windowed)glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);

        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) throw new IllegalStateException("Could not create window");

        if(windowed) glfwMaximizeWindow(window);
        else glfwSetWindowPos(window,0,0);

        glfwRequestWindowAttention(window);
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer i = stack.mallocInt(1);
            IntBuffer j = stack.mallocInt(1);
            IntBuffer k = stack.mallocInt(1);
            ByteBuffer img = stbi_load("src/main/resources/Icon.png",i,j,k,4);
            if(img == null)throw new RuntimeException("Could not Load Image");
            GLFWImage glfwImg = GLFWImage.malloc();
            glfwImg.set(i.get(),j.get(),img);
            glfwSetWindowIcon(window, GLFWImage.calloc(1,stack).put(0,glfwImg));
        }
        glfwSetWindowSizeLimits(window, width, height, width, height);
        glfwSetFramebufferSizeCallback(window, ((handle1, width1, height1) -> resize(width1,height1)));


        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);
        GL.createCapabilities();


    }

    public Window() {
        this(1,1,false);
    }

    public void pollEvents(){
        glfwPollEvents();
    }

    public void close() {
        glfwDestroyWindow(window);
        glfwTerminate();
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
}