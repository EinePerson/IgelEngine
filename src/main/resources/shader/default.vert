#version 330

layout(location = 0) in vec3 pos;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 uv;

uniform mat4 projMat;
uniform mat4 viewMat;

out vec4 oColor;
out vec2 oUV;

void main() {
    oUV = uv;
    oColor = color;
    gl_Position = vec4(pos,1.0f);
    gl_Position = projMat * viewMat * vec4(pos,1.0f);
}
