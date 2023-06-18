#version 450

layout(location = 0) in vec2 pos;
layout(location = 1) in vec3 tex;
layout(location = 2) in vec3 color;
//in vec2 uv;

uniform mat4 projMat;
uniform mat4 viewMat;

layout(location = 0) out vec3 outTex;
layout(location = 1) out vec3 outColor;
//layout(location = 1) out vec2 inUV;

void main() {
    //inUV = uv;

    outColor = color;
    outTex = tex;
    //outColor = color;
    gl_Position = projMat * viewMat * vec4(pos,0.0f,1.0f);
    //gl_Position = vec4(pos,1.0f);
}
