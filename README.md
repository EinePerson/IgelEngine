# IgelEngine
This is a Small Java Game "Engine"

Example buid.gradle

Replace engine_version with the version of the engine you want to use(Every version can be found as package)
```gradle
import org.codehaus.groovy.runtime.GStringImpl

plugins {
    id 'java'
}

repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

switch (org.gradle.internal.os.OperatingSystem.current()) {
    case org.gradle.internal.os.OperatingSystem.LINUX:
        def osArch = System.getProperty("os.arch")
        project.ext.lwjglNatives = osArch.startsWith("arm") || osArch.startsWith("aarch64")
                ? "natives-linux-${osArch.contains("64") || osArch.startsWith("armv8") ? "arm64" : "arm32"}"
                : "natives-linux"
        break
    case org.gradle.internal.os.OperatingSystem.MAC_OS:
        project.ext.lwjglNatives = System.getProperty("os.arch").startsWith("aarch64") ? "natives-macos-arm64" : "natives-macos" as GStringImpl as GStringImpl
        break
    case org.gradle.internal.os.OperatingSystem.WINDOWS:
        def osArch = System.getProperty("os.arch")
        project.ext.lwjglNatives = osArch.contains("64")
                ? "natives-windows${osArch.startsWith("aarch64") ? "-arm64" : ""}"
                : "natives-windows-x86" as GStringImpl as GStringImpl
        break
}

dependencies {
    implementation "com.github.EinePerson:IgelEngine:engine_version"

    implementation platform("org.lwjgl:lwjgl-bom:3.3.2")
    implementation "org.lwjgl:lwjgl"
    implementation "org.lwjgl:lwjgl-assimp"
    implementation "org.lwjgl:lwjgl-glfw"
    implementation "org.lwjgl:lwjgl-nfd"
    implementation "org.lwjgl:lwjgl-openal"
    implementation "org.lwjgl:lwjgl-opengl"
    implementation "org.lwjgl:lwjgl-shaderc"
    implementation "org.lwjgl:lwjgl-stb"
    runtimeOnly "org.lwjgl:lwjgl::$lwjglNatives"
    runtimeOnly "org.lwjgl:lwjgl-assimp::$lwjglNatives"
    runtimeOnly "org.lwjgl:lwjgl-glfw::$lwjglNatives"
    runtimeOnly "org.lwjgl:lwjgl-nfd::$lwjglNatives"
    runtimeOnly "org.lwjgl:lwjgl-openal::$lwjglNatives"
    runtimeOnly "org.lwjgl:lwjgl-opengl::$lwjglNatives"
    runtimeOnly "org.lwjgl:lwjgl-shaderc::$lwjglNatives"
    runtimeOnly "org.lwjgl:lwjgl-stb::$lwjglNatives"
}

jar{
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
}

tasks.register('fatJar', Jar) {
    manifest.from jar.manifest
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    } {
        exclude "META-INF/*.SF"
        exclude "META-INF/*.DSA"
        exclude "META-INF/*.RSA"
    }
    with jar
}

tasks.withType(Jar).all {
    duplicatesStrategy 'exclude'
}

artifacts {
    archives fatJar
}
```
