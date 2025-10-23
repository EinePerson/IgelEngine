#version 450 core
layout(lines) in;
layout(triangle_strip, max_vertices = 4) out;

in vec4 geomColor[];
in float geomThickness[];

out vec4 fragColor;

uniform vec2 relLength;

void main() {
    fragColor = geomColor[0];

    vec4 p0 = gl_in[0].gl_Position;
    vec4 p1 = gl_in[1].gl_Position;

    vec2 dir = normalize(p1.xy - p0.xy);

    //vec2 dir = vec2(0.7,0.7);
    vec2 perp = vec2(-dir.y / relLength.x, dir.x / relLength.y);

    float t0 = /*0.0125 * */ geomThickness[0];

    vec2 test = vec2(p0.xy) * 10;
    fragColor = vec4(test.x,0,test.y,1);

    gl_Position = vec4(p0.xy + perp * t0,0,1);
    EmitVertex();

    gl_Position = vec4(p0.xy - perp * t0,0,1);
    EmitVertex();

    gl_Position = vec4(p1.xy + perp * t0 + vec2(dir.x * 0.0005,dir.y * 0.0005),0,1);
    EmitVertex();

    gl_Position = vec4(p1.xy - perp * t0 + vec2(dir.x * 0.0005,dir.y * 0.0005),0,1);
    EmitVertex();

    EndPrimitive();
}
