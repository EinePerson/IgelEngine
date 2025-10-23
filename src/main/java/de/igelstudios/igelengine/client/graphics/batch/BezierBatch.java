package de.igelstudios.igelengine.client.graphics.batch;

import de.igelstudios.igelengine.client.graphics.BezierCurve;
import de.igelstudios.igelengine.client.graphics.Camera;
import de.igelstudios.igelengine.client.graphics.shader.Shader;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL44;

import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.opengl.ARBTessellationShader.GL_PATCH_VERTICES;
import static org.lwjgl.opengl.ARBTessellationShader.glPatchParameteri;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_PATCHES;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;

public class BezierBatch extends Batch<BezierCurve> {
    public static final int BEZIER_INTERVALS = 10;
    public BezierBatch(int size) {
        super(size,Shader.of("bezier",GL_VERTEX_SHADER,GL_TESS_CONTROL_SHADER,GL_TESS_EVALUATION_SHADER,GL_GEOMETRY_SHADER,GL_FRAGMENT_SHADER).noTexture()
                ,3,true,false,2,4,1);
        drawMode = (supplier) -> {
            glPatchParameteri(GL_PATCH_VERTICES, 3);
            glDrawArrays(GL_PATCHES, 0, 30);
        };
    }

    @Override
    public boolean dirtyCheck(List<BezierCurve> objs,BatchSupplier<BezierCurve> supplier) {
        boolean dirty = false;
        for (int i = 0; i < objs.size(); i++) {
            if(objs.get(i).toRemove()){
                clear(i,objs);
                //objects.get(i).removed();
                objs.remove(i);
                dirty = true;
                continue;
            }
            if(objs.get(i).isDirty(id)){
                add(i,objs.get(i),supplier);
                dirty = true;
            }
        }
        return dirty;
    }

    @Override
    protected void addP(int j,BezierCurve obj) {
        vertices[j] = obj.getStart().x;
        j++;
        vertices[j] = obj.getStart().y;
        j++;
        putColor(j,obj);
        j += 5;
        vertices[j] = obj.getControlPoint().x;
        j++;
        vertices[j] = obj.getControlPoint().y;
        j++;
        putColor(j,obj);
        j += 5;
        vertices[j] = obj.getEnd().x;
        j++;
        vertices[j] = obj.getEnd().y;
        j++;
        putColor(j,obj);
    }

    private void putColor(int j,BezierCurve obj) {
        vertices[j] = obj.getR();
        j++;
        vertices[j] = obj.getG();
        j++;
        vertices[j] = obj.getB();
        j++;
        vertices[j] = obj.getA();
        j++;
        vertices[j] = obj.getThickness();
    }

    @Override
    protected void bindUniforms(Shader shader,BatchSupplier<BezierCurve> supplier) {
        shader.putVec2("relLength",new Vector2f(supplier.getCamera().getX(),supplier.getCamera().getY()));
    }
}
