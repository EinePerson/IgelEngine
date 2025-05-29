package de.igelstudios.igelengine.client.graphics.shader;

import de.igelstudios.ClientMain;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.lwjgl.opengl.GL46.*;

public class Shader {
    private int[] shaders;
    private int program;
    private boolean used = false;

    public Shader(ShaderData[] data){
        shaders = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            shaders[i] = load(data[i].type,data[i].name);
        }
        program = glCreateProgram();
        for (int shader : shaders) {
            glAttachShader(program, shader);
        }
        glLinkProgram(program);

        int i = glGetProgrami(program, GL_LINK_STATUS);
        //if (i == GL_FALSE) ClientMain.LOGGER.error(data + " Linking of shaders failed." + glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH)));

    }
    private static int load(int type,String name){
        try(InputStream stream = Objects.requireNonNull(Shader.class.getClassLoader().getResourceAsStream("shaders/" + name))) {
            int id = glCreateShader(type);
            glShaderSource(id,new String(stream.readAllBytes()));
            glCompileShader(id);
            int i = glGetShaderi(id, GL_COMPILE_STATUS);
            //if (i == GL_FALSE) ClientMain.LOGGER.error(name + " Shader compilation failed." + glGetShaderInfoLog(id, glGetShaderi(id, GL_INFO_LOG_LENGTH)));

            return id;
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public Shader(String name){
        this(new Shader.ShaderData[]{new Shader.ShaderData(name + ".vert",GL_VERTEX_SHADER),new Shader.ShaderData(name + ".frag",GL_FRAGMENT_SHADER)});
    }

    public void putMat(String id, Matrix4f mat){
        int loc = glGetUniformLocation(program,id);
        use();
        FloatBuffer fp = BufferUtils.createFloatBuffer(16);
        mat.get(fp);
        glUniformMatrix4fv(loc,false,fp);
    }

    public void putVec4(String id, Vector4f vec){
        int loc = glGetUniformLocation(program,id);
        use();
        glUniform4f(loc,vec.x,vec.y,vec.z,vec.w);
    }

    public void putFloat(String id,float f){
        int loc = glGetUniformLocation(program,id);
        use();
        glUniform1f(loc,f);
    }

    public void putInt(String id,int i){
        int loc = glGetUniformLocation(program,id);
        use();
        glUniform1i(loc,i);
    }

    public void putTex(String id,int i){
        int loc = glGetUniformLocation(program,id);
        use();
        glUniform1i(loc,i);
    }

    public void pitInt(String varName, int[] arr) {
        int pos = glGetUniformLocation(program, varName);
        use();
        glUniform1iv(pos, arr);
    }

    public void use(){
        if(used)return;
        glUseProgram(program);
        used = true;
    }

    public void unUse(){
        if(!used)return;
        glUseProgram(0);
        used = false;
    }

    public record ShaderData(String name,int type){}
}
