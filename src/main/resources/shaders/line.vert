#version 450

layout(location = 0) in vec2 start;
layout(location = 1) in vec4 color;

layout(location = 1) in mat4 line_data;

uniform mat4 projMat;
uniform mat4 viewMat;

layout(location = 0) out vec4 outColor;

void main(){
    outColor = color;
    gl_Position = projMat * viewMat * vec4(start,0.0f,1.0f);
}