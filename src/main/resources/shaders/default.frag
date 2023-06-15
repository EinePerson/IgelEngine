#version 450

layout(location = 0) in vec4 inColor;
//uniform sampler2D tex;
//layout(location = 1) in vec2 inUV;

layout(location = 0) out vec4 color;

void main() {
    color = inColor;
   // color = texture(tex,inUV);
}
