package de.igelstudios.igelengine.client;

import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.client.graphics.Renderer;
import de.igelstudios.igelengine.client.graphics.text.GLFont;
import de.igelstudios.igelengine.client.graphics.texture.Texture;
import de.igelstudios.igelengine.client.keys.HIDInput;
import de.igelstudios.igelengine.client.lang.Text;
import de.igelstudios.igelengine.common.Engine;
import de.igelstudios.igelengine.common.networking.client.Client;
import de.igelstudios.igelengine.common.scene.SceneObject;
import de.igelstudios.igelengine.common.util.Test;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ClientEngine extends Engine {

    private Window window;
    private final HIDInput input;
    private ClientScene scene;

    private static GLFont defaultFont = null;

    public static GLFont getDefaultFont() {
        return defaultFont;
    }

    int fps = 0;
    @Test
    int o = 0;
    @Test
    SceneObject object;



    public ClientEngine(){
        window = new Window();
        input = new HIDInput();
        input.registerGLFWListeners(window.getWindow());
        GL11.glClearColor(1.0f,1.0f,1.0f,1.0f);
        scene = new ClientScene();

        scene.addObject(new SceneObject().setPos(new Vector2f(0f,4f)).setTex(0).setUv(0,0).setTex(Texture.get("Igel_gaming.png").getID()));
        //System.out.println(Texture.get("Igel_gaming.png").getID());

        Texture tex = Texture.get("test2.png");

        object = new SceneObject().setPos(new Vector2f(0f,0f)).setTex(0).setUv(0,15).setTex(tex.getID());
        scene.addObject(object);
        scene.addObject(new SceneObject().setPos(new Vector2f(0f,1f)).setTex(0).setUv(0,0).setTex(tex.getID()));
        System.out.println(tex.getID());


        if(defaultFont == null) defaultFont = new GLFont("Candaraz");
        scene.add(Text.literal("a b").setColor(1.0f,0.0f,0.0f).setPos(new Vector2f(0.1f,0.1f)).setScale(1f));
        scene.add(Text.literal("cde").setColor(0.0f,1.0f,0.0f).setPos(new Vector2f(1.0f,1.0f)).setScale(1f));
        scene.add(Text.literal("fgh").setColor(0.0f,0.0f,1.0f).setPos(new Vector2f(2.0f,20.0f)).setScale(1f));


        //scene.addObject(new SceneObject().setPos(new Vector2f(79f,44f)).setCol(new Vector4f(0.0f,1.0f,0.0f,0.0f)));
        //lmn;lnm;mnl;mln;nlm;nml;
        /*int k = 0;
        float l = 0.001f;
        float m = 0.001f;
        float n = 0.001f;
        for (int i = 0; i < 80; i++) {
            for (int j = 0; j < 45; j++) {
                scene.addObject(new SceneObject().setPos(new Vector2f(i,j)).setCol(new Vector4f(l * (j + 1),m * (j + 1),n * (j + 1),0.0f)));
                if(k < 1200)l += 0.00001f;
                else if(k < 2400)m += 0.00001f;
                else n += 0.00001f;
                k++;
            }
        }*/
    }

    @Override
    public boolean shouldRun() {
        return !window.shouldClose();
    }

    @Override
    public void tick(long deltat) {
        input.invokeContinuous();
        //scene.getCam().move(new Vector2f(1.0f,1.0f));
        //if(o == 1000)object.setPos(new Vector2f(15.0f,15.0f));
        o++;
        //scene.getCam().getPos().x -= 0.01f;
        //scene.getCam().getPos().y -= 0.005625f;
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
