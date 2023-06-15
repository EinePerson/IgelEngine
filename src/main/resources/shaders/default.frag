#version 450

layout(location = 0) in vec3 inColor;
uniform sampler2D tex[8];
//layout(location = 1) in vec2 inUV;

layout(location = 0) out vec4 color;

void main() {
    color = texture(tex[int(inColor.x)],vec2(inColor.y,inColor.z));
    //color = vec4(inColor.x,inColor.y,0.0f,0.0f);
   // color = texture(tex,inUV);
}
