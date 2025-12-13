#version 450 core
layout(vertices = 3) out;

in vec4 tescColor[];
in float tescThickness[];

patch out vec4 teseColor;
patch out float teseThickness;

void main() {
    teseThickness = tescThickness[gl_InvocationID];
    teseColor = tescColor[gl_InvocationID];
    gl_out[gl_InvocationID].gl_Position = gl_in[gl_InvocationID].gl_Position;

    //if (gl_InvocationID == 0) {
        gl_TessLevelOuter[0] = 128.0;
        gl_TessLevelOuter[1] = 128.0;
    //}
}