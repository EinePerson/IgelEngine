#version 450

in vec4 fragColor;
layout(location = 0) out vec4 color;

void main() {
    color = /*vec4(1.0,0.0,1.0,1.0);*/ fragColor;
}