#version 450

layout(location = 0) in vec2 pos;
layout(location = 1) in vec4 color;
layout(location = 2) in float thickness;

uniform mat4 projMat;
uniform mat4 viewMat;

out vec4 tescColor;
out float tescThickness;

void main() {
    tescThickness = thickness;
    tescColor = color;
    gl_Position = projMat * viewMat *  vec4(pos,0.0f,1.0f);
}
