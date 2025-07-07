package de.igelstudios.igelengine.client.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.*;

/**
 * this is a camera, it holds the projection and view matrix to move the objects in the room when the player moves
 */
public class Camera {
    private static int x = 80;
    private static int y = 45;
    private Matrix4f projMat, viewMat;
    private Vector2f pos;
    private static Camera instance;

    /**
     * @return the x size of the Camera view
     */
    public static int getX() {
        return x;
    }

    /**
     * @return the y size of the Camera view
     */
    public static int getY() {
        return y;
    }

    /**
     * Sets the size of the Camera
     * @param x the new x Size
     * @param y the new y Size
     */
    public static void setSize(int x, int y){
        Camera.x = x;
        Camera.y = y;
        instance.adjust();
    }

    public Camera(Vector2f pos){
        this.pos = pos;
        projMat = new Matrix4f();
        viewMat = new Matrix4f();
        adjust();
        instance = this;

        //alListener3f(AL_POSITION, pos.x, pos.y, 0.0f);
        //alListener3f(AL_VELOCITY, 0, 0, 0);
    }

    public void adjust(){
        projMat.identity();
        projMat.ortho(0.0f, x,0.0f, y,0.0f,100.0f);
        //alListener3f(AL_POSITION, pos.x, pos.y, 0.0f);
        Vector3f up = new Vector3f(0.0f,1.0f,0.0f);
        Vector3f at = new Vector3f(0.0f,0.0f,-1.0f);
        at.add(pos.x,pos.y,0.0f);
        float[] data = new float[6];
        data[0] = at.x;
        data[1] = at.y;
        data[2] = at.z;
        data[3] = up.x;
        data[4] = up.y;
        data[5] = up.z;
        //alListenerfv(AL_ORIENTATION, data);
    }

    public Matrix4f getViewMat() {
        Vector3f cam = new Vector3f(0.0f,0.0f,-1.0f);
        Vector3f up = new Vector3f(0.0f,1.0f,0.0f);
        viewMat.identity();
        return viewMat.lookAt(new Vector3f(pos.x,pos.y,20.0f),cam.add(pos.x,pos.y,0.0f),up);
    }

    public Matrix4f getProjMat() {
        return projMat;
    }

    /**
     * Moves the camera view by the specified amount
     * @param pos the delta in Camera position
     */
    public static void move(Vector2f pos){
        instance.pos.add(pos);
        instance.adjust();
    }

    public static Vector2f getPos() {
        return instance.pos;
    }
}
