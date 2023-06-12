#version 330

in vec4 oColor;
uniform sampler2D tex;
in vec2 oUV;

void main() {
    gl_FragColor = texture(tex,oUV);
}
