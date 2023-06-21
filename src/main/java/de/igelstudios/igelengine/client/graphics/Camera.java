package de.igelstudios.igelengine.client.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    public static final int SIZE_X = 80;
    public static final int SIZE_Y = 45;
    private Matrix4f projMat, viewMat;
    private Vector2f pos;

    public Camera(Vector2f pos){
        this.pos = pos;
        projMat = new Matrix4f();
        viewMat = new Matrix4f();
        adjust();
    }

    public void adjust(){
        projMat.identity();
        //INFO 80,45
        projMat.ortho(0.0f,SIZE_X,0.0f,SIZE_Y,0.0f,100f);
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

    public void move(Vector2f pos){
        this.pos.add(pos);
        adjust();
    }

    public Vector2f getPos() {
        return pos;
    }
}
