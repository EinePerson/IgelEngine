package de.igelstudios.igelengine.client;

import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.graphics.Camera;
import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.graphics.text.GLFont;
import de.igelstudios.igelengine.client.gui.GUIManager;
import de.igelstudios.igelengine.client.keys.HIDInput;
import de.igelstudios.igelengine.common.Engine;
import de.igelstudios.igelengine.common.scene.Scene;
import de.igelstudios.igelengine.common.startup.EngineInitializer;
import de.igelstudios.igelengine.common.startup.KeyInitializer;
import de.igelstudios.igelengine.common.util.Tickable;
import de.igelstudios.igelengine.common.util.UnImplemented;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * This class has to be initialised before anything else should be done on the client side
 */
public class ClientEngine extends Engine {

    //private static List<Window> windows;
    //private HIDInput input;

    private String title;
    private EngineInitializer initializer;
    private boolean printFPS = false;
    int fps = 0;

    private static List<Render> renderThreads;

    private static Thread mainThread;
    private static volatile boolean mainTaskQueue = false;
    private static final ConcurrentLinkedQueue<Runnable> mainTasks1 = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Runnable> mainTasks2 = new ConcurrentLinkedQueue<>();

    private static boolean isSingleWindowed = true;
    private static GLFont defaultFont = null;
    private volatile boolean shouldRun = true;

    private KeyInitializer keyInit;
    private static ClientEngine instance;

    /**
     * Because everything that has to do with Open GL(Rendering) has to run on the same thread, you can add a task here so that it may be executed on the next rendering
     * @param task the task to be executed on the renderer thread
     * @see #queueForMainThread(Runnable)
     */
    public static void queueForRenderThread(Runnable task,int id){
        if(renderThreads.get(id).renderTaskQueue) renderThreads.get(id).renderTasks1.add(task);
        else renderThreads.get(id).renderTasks2.add(task);
    }

    /**
     * Executes the specified task on every render thread that is created (For every window)
     * @param task the task to execute
     */
    public static void queueForAllRenderThreads(Runnable task){
        for (int i = 0; i < renderThreads.size(); i++) {
            queueForRenderThread(task,i);
        }
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

        //windows = new ArrayList<>();
        renderThreads = new ArrayList<>();

        keyInit = new KeyInitializer();
        initializer.registerKeys(keyInit);
        keyInit.register();

        isSingleWindowed = ClientMain.getInstance().getSettings().getWindowCount() == 1;
        instance = this;

        //ClientEngine.this.input = new HIDInput(initializer);
    }

    @Override
    public boolean shouldRun() {
        return shouldRun;
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

        //input.invokeContinuous();
    }



    @Override
    public void second() {
        if(printFPS)System.out.println("FPS: " + fps);
        fps = 0;
    }

    @Override
    public void stopSub() {
        for (Render renderThread : renderThreads) {

            renderThread.interrupt();
            try {
                renderThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        this.initializer.onEnd();
        Window.terminate();
        //this.input.close();
    }

    public int getFPS() {
        return fps;
    }

    /**
     * Returns the specific window for the given id,in one window mode 0 may be used as id
     * @param id the id of the window
     * @return the window
     */
    public static Window getWindow(int id){
        return renderThreads.get(id).window;
    }

    public static Camera getCamera(int id){
        return renderThreads.get(id).renderer.getCamera();
    }

    static int findID(long ptr){
        for (int i = 0; i < renderThreads.size(); i++) {
            if(renderThreads.get(i).window.getWindow() == ptr)return i;
        }
        return -1;
    }

    @Override
    protected void started() {
        mainThread =  Thread.currentThread();
        for (int i = 0;i < ClientMain.getInstance().getSettings().getWindowCount();i++) {
            Render render = new Render();
            render.start();
            try {
                synchronized (mainThread) {
                    mainThread.wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        //ClientEngine.this.window.createAudio((String) ClientConfig.getConfig().getOrDefault("audio_device",window.getAudioDevices().getFirst()));

        this.initializer.onInitialize();

        for(Render renderThread : renderThreads){
            synchronized(renderThread){
                renderThread.notify();
            }
        }
    }

    public class Render extends Thread{
        private int id;
        private static int count = 0;
        private volatile boolean renderTaskQueue = false;
        private final ConcurrentLinkedQueue<Runnable> renderTasks1 = new ConcurrentLinkedQueue<>();
        private final ConcurrentLinkedQueue<Runnable> renderTasks2 = new ConcurrentLinkedQueue<>();
        private Window window;
        private Renderer renderer;
        private HIDInput input;

        private Render(){
            super("Render Thread " + count++);
        }

        @Override
        public void run(){
            init();
            while (!isInterrupted() && !window.shouldClose()){
                loop();
            }
            window.closeMonitor();
            shouldRun = false;
            input.close();
            //windows.set(id,null);
            //renderThreads.set(id,null);
            //ClientEngine.this.window.close();
        }

        public void init(){
            id = renderThreads.size();
            renderThreads.add(this);
            window = new Window(ClientEngine.this.title,id);
            renderer = new Renderer(new Camera(),id);

            Renderer.add(renderer);
            //windows.add(window);
            input = new HIDInput(id);
            input.registerGLFWListeners(window.getWindow());

            GL11.glClearColor(1.0f,1.0f,1.0f,1.0f);

            renderTaskQueue = !renderTaskQueue;
            if(renderTaskQueue){
                renderTasks2.forEach(Runnable::run);
                renderTasks2.clear();
            }else{
                renderTasks1.forEach(Runnable::run);
                renderTasks1.clear();
            }

            addTickable(Renderer.get(id).getTextBatch());

            printFPS = (boolean) ClientConfig.getConfig().getOrDefault("print_fps",false);

            synchronized (mainThread){
                mainThread.notify();

            }
            synchronized (this){
                try{
                    wait();
                }catch(InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        }

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
            renderer.render();
            GLFW.glfwSwapBuffers(window.getWindow());

            window.pollEvents();
            fps++;
        }
    }


    /**
     * Used for making sure that code that may only run on the main thread does so <br>
     * If called from the main thread, task gets executed immediately else it gets queued to be executed on the main thread
     * @param task the task that may only run on the main thread
     * @see #enforceRenderThread(Runnable,int)
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
    public static void enforceRenderThread(Runnable task,int id){
        if(id == -1)return;
        if(Thread.currentThread() != renderThreads.get(id))queueForRenderThread(task,id);
        else task.run();
    }

    /**
     * Used for making sure that code that may only run on the render thread does so, in this case it will probably get scheduled when multiple windows are present <br>
     * If called from the render thread, task gets executed immediately else it gets queued to be executed on the render thread
     * @param task the task that may only run on the render thread
     * @see #enforceMainThread(Runnable)
     */
    public static void enforceAllRenderThreads(Runnable task){
        for (int i = 0; i < renderThreads.size(); i++) {
            enforceRenderThread(task,i);
        }
    }

    public static boolean isSingleWindowed() {
        return isSingleWindowed;
    }

    public static void singleWindowCheck(){
        if(!ClientEngine.isSingleWindowed())throw new IllegalStateException("Can only obtain global renderer for Single windowed mode");
    }

    public static void addScene(ClientScene scene){
        singleWindowCheck();
        scene.onAddToRenderer(0);
    }

    public static void removeScene(ClientScene scene){
        singleWindowCheck();
        scene.onAddToRenderer(0);
    }

    public static void addScene(ClientScene scene,int id){
        scene.onAddToRenderer(id);
    }

    public static void removeScene(ClientScene scene,int id){
        scene.onRemoveFromRenderer(id);
    }

    @UnImplemented
    public static void removeAllScenes(int id){

    }

    public static int getRenderThreadIdOr0(){
        Thread currentThread = Thread.currentThread();

        for(int i = 0; i < renderThreads.size(); i++){
            if(renderThreads.get(i) == currentThread)return i;
        }
        return 0;
    }

    /*public void focusWindow(int id){
        GLFW.glfwFocusWindow(renderThreads.get(id).window.getWindow());
    }*/

    public static int getWindowCount(){
        return ClientMain.getInstance().getSettings().getWindowCount();
    }

    public static ClientEngine getInstance() {
        return instance;
    }
}
