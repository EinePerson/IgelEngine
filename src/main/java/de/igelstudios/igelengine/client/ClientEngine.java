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

    private String title;
    private EngineInitializer initializer;
    private boolean printFPS = false;
    int fps = 0;

    private static Thread renderThread;
    private static volatile boolean renderTaskQueue = false;
    private static final ConcurrentLinkedQueue<Runnable> renderTasks1 = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Runnable> renderTasks2 = new ConcurrentLinkedQueue<>();

    private static Thread mainThread;
    private static volatile boolean mainTaskQueue = false;
    private static final ConcurrentLinkedQueue<Runnable> mainTasks1 = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Runnable> mainTasks2 = new ConcurrentLinkedQueue<>();

    private static GLFont defaultFont = null;

    /**
     * Because everything that has to do with Open GL(Rendering) has to run on the same thread, you can add a task here so that it may be executed on the next rendering
     * @param task the task to be executed on the renderer thread
     * @see #queueForMainThread(Runnable)
     */
    public static void queueForRenderThread(Runnable task){
        if(renderTaskQueue) renderTasks1.add(task);
        else renderTasks2.add(task);
    }

    /**
     * Logic should run on the main thread you can enqueue them here so that they may be run on the next tick
     * @param task the task to be executed on the main/logic thread
     */
    public static void queueForMainThread(Runnable task){
        if(mainTaskQueue) mainTasks1.add(task);
        else mainTasks2.add(task);
    }

    /**
     * Returns the default fonts of the Engine
     * @return the default font
     */
    public static GLFont getDefaultFont() {
        if(defaultFont == null) defaultFont = new GLFont("Cantarell-VF");
        return defaultFont;
    }

    public ClientEngine(EngineInitializer initializer,String title){
        this.initializer = initializer;
        this.title = title;

        renderThread = new Render();

        ClientEngine.this.input = new HIDInput(initializer);
    }

    @Override
    public boolean shouldRun() {
        return !window.shouldClose();
    }

    @Override
    public void tick() {
        mainTaskQueue = !mainTaskQueue;
        if(mainTaskQueue){
            mainTasks2.forEach(Runnable::run);
            mainTasks2.clear();
        }else{
            mainTasks1.forEach(Runnable::run);
            mainTasks1.clear();
        }

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
        if(printFPS)System.out.println("FPS: " + fps);
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
        ClientEngine.this.window.createAudio((String) ClientConfig.getConfig().getOrDefault("audio_device",window.getAudioDevices().getFirst()));
        ClientEngine.this.input.registerGLFWListeners(ClientEngine.this.window.getWindow());
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
            ClientEngine.this.window.close();
        }

        public void init(){
            ClientEngine.this.window = new Window(ClientEngine.this.title);
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

            printFPS = (boolean) ClientConfig.getConfig().getOrDefault("print_fps",false);

            synchronized (mainThread){
                mainThread.notify();
            }
        }
    }

    /**
     * Used for making sure that code that may only run on the main thread does so <br>
     * If called from the main thread, task gets executed immediately else it gets queued to be executed on the main thread
     * @param task the task that may only run on the main thread
     * @see #enforceRenderThread(Runnable)
     */
    public static void enforceMainThread(Runnable task){
        if(Thread.currentThread() != mainThread)queueForMainThread(task);
        else task.run();
    }

    /**
     * Used for making sure that code that may only run on the render thread does so <br>
     * If called from the render thread, task gets executed immediately else it gets queued to be executed on the render thread
     * @param task the task that may only run on the render thread
     * @see #enforceMainThread(Runnable)
     */
    public static void enforceRenderThread(Runnable task){
        if(Thread.currentThread() != renderThread)queueForRenderThread(task);
        else task.run();
    }

}
