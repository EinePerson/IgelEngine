#version 450

layout(location = 0) in vec4 inColor;

layout(location = 0) out vec4 color;

void main() {
    color = inColor;
}