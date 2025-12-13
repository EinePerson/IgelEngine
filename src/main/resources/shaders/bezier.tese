#version 450 core
layout(isolines, fractional_even_spacing, cw) in;

patch in vec4 teseColor;
patch in float teseThickness;

out vec4 geomColor;
out float geomThickness;

void main() {
    geomColor = teseColor;
    geomThickness = teseThickness;

    float t = gl_TessCoord.x;
    vec2 p0 = gl_in[0].gl_Position.xy;
    vec2 p1 = gl_in[1].gl_Position.xy;
    vec2 p2 = gl_in[2].gl_Position.xy;

    float u = 1.0 - t;
    vec2 pos = u * u * p0 + 2 * u * t * p1 + t * t * p2;

    gl_Position = vec4(pos,0.0f, 1.0);
}