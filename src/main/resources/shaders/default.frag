#version 450

layout(location = 0) in vec3 inColor;
uniform sampler2D tex[8];
//layout(location = 1) in vec2 inUV;

layout(location = 0) out vec4 color;

void main() {
    int id = int(inColor.x);
    color = texture(tex[id],vec2(inColor.y,inColor.z));
    //color = vec4(id,0.0f,1.0f,1.0f);
    //color = vec4(inColor,1.0f);
    //color = vec4(inColor.x,inColor.y,0.0f,0.0f);
   // color = texture(tex,inUV);
}
