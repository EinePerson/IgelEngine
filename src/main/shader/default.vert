#version 330

layout(location = 0) in vec3 pos;
layout(location = 1) in vec4 color;

out vec4 outColor;

void main() {
    outColor = color;
    gl_Position = vec4(pos,1.0f);
}
