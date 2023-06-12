package de.igelstudios.igelengine.client;

import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.keys.HIDInput;
import de.igelstudios.igelengine.common.Engine;
import de.igelstudios.igelengine.common.networking.client.Client;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class ClientEngine extends Engine {
    private Window window;
    private final HIDInput input;
    private Renderer renderer;
    private ClientScene scene;
    int fps = 0;

    public ClientEngine(){
        window = new Window();
        input = new HIDInput();
        input.registerGLFWListeners(window.getWindow());
        renderer = new Renderer();
        GL11.glClearColor(1.0f,1.0f,1.0f,1.0f);
        scene = new ClientScene();
    }

    @Override
    public boolean shouldRun() {
        return !window.shouldClose();
    }

    @Override
    public void tick(long deltat) {
        input.invokeContinuous();
        scene.getCam().getPos().x -= 1.0f;
        scene.getCam().getPos().y -= 0.5625f;
    }

    @Override
    public void loop() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        renderer.render(scene);
        GLFW.glfwSwapBuffers(window.getWindow());
        window.pollEvents();
        fps++;
    }

    @Override
    public void second() {
        ClientMain.LOGGER.info("FPS:" + fps);
        fps = 0;
    }

    public ClientScene getScene() {
        return scene;
    }
}
