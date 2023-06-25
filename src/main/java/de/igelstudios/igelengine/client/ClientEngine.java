package de.igelstudios.igelengine.client;

import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.graphics.text.GLFont;
import de.igelstudios.igelengine.client.keys.HIDInput;
import de.igelstudios.igelengine.common.Engine;
import de.igelstudios.igelengine.common.startup.EngineInitializer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;


/**
 * This class has to be initialised before anything else should be done on the client side
 */
public class ClientEngine extends Engine {

    private Window window;
    private final HIDInput input;
    private ClientScene scene;

    private static GLFont defaultFont = null;

    public static GLFont getDefaultFont() {
        return defaultFont;
    }
    private EngineInitializer initializer;

    int fps = 0;

    public ClientEngine(EngineInitializer initializer){
        this.initializer = initializer;
        window = new Window();
        input = new HIDInput(initializer);
        input.registerGLFWListeners(window.getWindow());
        GL11.glClearColor(1.0f,1.0f,1.0f,1.0f);
        scene = new ClientScene();

        if(defaultFont == null) defaultFont = new GLFont("calibri");
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
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        Renderer.get().render();
        GLFW.glfwSwapBuffers(window.getWindow());
        window.pollEvents();
        fps++;
    }

    @Override
    public void second() {
        ClientMain.LOGGER.info("FPS:" + fps);
        fps = 0;
    }

    @Override
    public void stopSub() {
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
}
