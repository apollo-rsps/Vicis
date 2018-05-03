#version 330

layout(location = 0) out vec4 FragColor;

in vec3 fragColor;

void main() {
	FragColor = vec4(fragColor, 1.0);
}
