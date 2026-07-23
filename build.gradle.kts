plugins {
    `java-library`
    `maven-publish`
}

group = "org.slatenative"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
}

repositories {
    mavenCentral()
}

val lwjglVersion = "3.3.2"

val lwjglNatives = Pair(
    System.getProperty("os.name").lowercase(),
    System.getProperty("os.arch").lowercase()
).let { (os, arch) ->
    when {
        os.contains("windows") -> if (arch.contains("64")) "natives-windows" else "natives-windows-x86"
        os.contains("mac") -> if (arch.contains("aarch64") || arch.contains("arm")) "natives-macos-arm64" else "natives-macos"
        os.contains("linux") -> if (arch.contains("arm") || arch.contains("aarch64")) "natives-linux-arm64" else "natives-linux"
        else -> "natives-windows"
    }
}

dependencies {
    api(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    api("org.lwjgl:lwjgl")
    api("org.lwjgl:lwjgl-glfw")
    api("org.lwjgl:lwjgl-stb")

    runtimeOnly("org.lwjgl:lwjgl")
    runtimeOnly("org.lwjgl:lwjgl-glfw")
    runtimeOnly("org.lwjgl:lwjgl-stb")

    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
}

tasks.test {
    useJUnitPlatform()
}

// Helpful run configuration
tasks.register("runDemo", JavaExec::class) {
    group = "application"
    mainClass.set("slatewindow.example.Demo")
    classpath = sourceSets.main.get().runtimeClasspath + sourceSets.test.get().runtimeClasspath

    if (System.getProperty("os.name").contains("Mac", ignoreCase = true)) {
        jvmArgs("-XstartOnFirstThread")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}