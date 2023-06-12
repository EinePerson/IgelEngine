package de.igelstudios.igelengine.client.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private static final int TILES_X = 160;
    private static final int TILES_Y = 90;
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
        projMat.ortho(0.0f,16 * TILES_X,0.0f,16 * TILES_Y,0.0f,100f);
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

    public Vector2f getPos() {
        return pos;
    }
}
