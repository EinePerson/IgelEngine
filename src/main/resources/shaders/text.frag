#version 450

layout(location = 0) in vec3 inTex;
layout(location = 1) in vec3 inColor;

uniform sampler2D tex[8];
layout(location = 0) out vec4 color;

void main() {
    //inColor = vec3(1.0f,1.0f,1.0f);
    //color = vec4(inColor,1.0f);
    int id = int(inTex.x);
    color = texture(tex[id],vec2(inTex.y,inTex.z));
    color = vec4(1.0f,1.0f,1.0f,color.a) * vec4(inColor,1.0f);
    //color = vec4(1.0f,1.0f,1.0f,c) * vec4(1.0f,0.0f,1.0f,1.0f);
    //color = vec4(acolor.a);
    //color = vec4(c); //* vec4(1.0f,0.0f,0.0f,1.0f);
    //color = vec4(int(inTex.x),20.0f,20.0f,20.0f);
    //color = vec4(1.0f,0.0f,0.0f,0.0f);
    //color = vec4(inTex,1.0f);
}
