package de.igelstudios.igelengine.client;

import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.graphics.text.GLFont;
import de.igelstudios.igelengine.client.keys.HIDInput;
import de.igelstudios.igelengine.common.Engine;
import de.igelstudios.igelengine.common.startup.EngineInitializer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * This class has to be initialised before anything else should be done on the client side
 */
public class ClientEngine extends Engine {

    private Window window;
    private HIDInput input;
    private ClientScene scene;
    private Thread renderThread;
    private Thread mainThread;
    private String title;
    private static boolean renderTaskQueue = false;
    private static final ConcurrentLinkedQueue<Runnable> renderTasks1 = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Runnable> renderTasks2 = new ConcurrentLinkedQueue<>();

    private static GLFont defaultFont = null;

    /**
     * Because everything that has to do with Open GL(Rendering) has to run on the same thread, you can add a task here so that it may be executed on the next rendering
     * @param task the task to be executed on the renderer thread
     */
    public static void queueForRenderThread(Runnable task){
        if(renderTaskQueue) renderTasks1.add(task);
        else renderTasks2.add(task);
    }

    public static GLFont getDefaultFont() {
        if(defaultFont == null) defaultFont = new GLFont("Cantarell-VF");
        return defaultFont;
    }
    private EngineInitializer initializer;

    int fps = 0;

    public ClientEngine(EngineInitializer initializer,String title){
        this.initializer = initializer;
        this.title = title;

        renderThread = new Render();
        //window = new Window(title);

        //renderThread = new Render();
        //input = new HIDInput(initializer);
        //input.registerGLFWListeners(window.getWindow());
        //GL11.glClearColor(1.0f,1.0f,1.0f,1.0f);
        //scene = new ClientScene();
        //addTickable(Renderer.get().getTextBatch());
        //ClientEngine.this.window = new Window(ClientEngine.this.title);
        //ClientEngine.this.input.registerGLFWListeners(ClientEngine.this.window.getWindow());
        //GL11.glClearColor(1.0f,1.0f,1.0f,1.0f);

        //renderTasks.forEach(Runnable::run);
        //renderTasks.clear();
        //addTickable(Renderer.get().getTextBatch());
        //((Render) renderThread).init();
    }

    @Override
    public boolean shouldRun() {
        return !window.shouldClose();
    }

    @Override
    public void tick() {
        input.invokeContinuous();
    }

    @Override
    public void loop() {
        renderTaskQueue = !renderTaskQueue;
        if(renderTaskQueue){
            renderTasks2.forEach(Runnable::run);
            renderTasks2.clear();
        }else{
            renderTasks1.forEach(Runnable::run);
            renderTasks1.clear();
        }

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        Renderer.get().render();
        GLFW.glfwSwapBuffers(window.getWindow());
        window.pollEvents();
        fps++;
    }

    @Override
    public void second() {
        //System.out.println("FPS: " + fps);
        fps = 0;
    }

    @Override
    public void stopSub() {
        renderThread.interrupt();
        try {
            renderThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.initializer.onEnd();
        this.window.close();
        this.input.close();
    }

    public ClientScene getScene() {
        return scene;
    }

    public void setScene(ClientScene scene) {
        this.scene = scene;
    }

    public Window getWindow() {
        return window;
    }

    public HIDInput getInput() {
        return input;
    }

    public int getFPS() {
        return fps;
    }

    @Override
    protected void started() {
        mainThread =  Thread.currentThread();
        renderThread.start();
        try {
            synchronized (mainThread) {
                mainThread.wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.initializer.onInitialize();
    }



    private class Render extends Thread{

        private Render(){
            super("Render Thread");

        }

        @Override
        public void run(){
            init();
            while (!isInterrupted()){
                loop();
            }
        }

        public void init(){
            ClientEngine.this.window = new Window(ClientEngine.this.title);
            ClientEngine.this.window.createAudio((String) ClientConfig.getConfig().getOrDefault("audio_device",window.getAudioDevices().getFirst()));
            ClientEngine.this.input = new HIDInput(initializer);
            ClientEngine.this.input.registerGLFWListeners(ClientEngine.this.window.getWindow());
            GL11.glClearColor(1.0f,1.0f,1.0f,1.0f);

            scene = new ClientScene();

            renderTaskQueue = !renderTaskQueue;
            if(renderTaskQueue){
                renderTasks2.forEach(Runnable::run);
                renderTasks2.clear();
            }else{
                renderTasks1.forEach(Runnable::run);
                renderTasks1.clear();
            }

            addTickable(Renderer.get().getTextBatch());

            synchronized (mainThread){
                mainThread.notify();
            }
        }
    }


}
