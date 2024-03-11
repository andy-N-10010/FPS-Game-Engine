#version 330 core

in vec3 position;
in vec3 color;
out vec3 passColor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 local;
uniform mat4 projection;

void main() {
    gl_Position = projection * view * local * model  * vec4(position, 1.0);
    passColor = color;

}