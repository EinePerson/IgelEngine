package de.igelstudios.igelengine.client;

import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.keys.HIDInput;
import de.igelstudios.igelengine.common.Engine;
import de.igelstudios.igelengine.common.networking.client.Client;
import de.igelstudios.igelengine.common.scene.SceneObject;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;

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


        //scene.addObject(new SceneObject().setPos(new Vector2f(0f,0f)).setCol(new Vector4f(1.0f,0.0f,0.0f,0.0f)));
        //scene.addObject(new SceneObject().setPos(new Vector2f(79f,44f)).setCol(new Vector4f(0.0f,1.0f,0.0f,0.0f)));
        //lmn;lnm;mnl;mln;nlm;nml;
        int k = 0;
        float l = 0.001f;
        float m = 0.001f;
        float n = 0.001f;
        for (int i = 0; i < 80; i++) {
            for (int j = 0; j < 45; j++) {
                scene.addObject(new SceneObject().setPos(new Vector2f(i,j)).setCol(new Vector4f(l * (j + 1),m * (j + 1),n * (j + 1),0.0f)));
                if(k < 1200)m += 0.00001f;
                else if(k < 2400)l += 0.00001f;
                else n += 0.00001f;
                k++;
            }
        }
    }

    @Override
    public boolean shouldRun() {
        return !window.shouldClose();
    }

    @Override
    public void tick(long deltat) {
        input.invokeContinuous();
        //scene.getCam().getPos().x -= 1.0f;
        //scene.getCam().getPos().y -= 0.5625f;
    }

    @Override
    public void loop() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        scene.render();
        //renderer.render(scene);
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
