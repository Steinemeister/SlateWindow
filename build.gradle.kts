plugins {
    java
}

group = "org.slatenative"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

val lwjglVersion = "3.3.2"

dependencies {
    // Use the LWJGL BOM to align versions
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")

    // Add platform-specific natives at runtime
    runtimeOnly("org.lwjgl:lwjgl::natives-windows")
    runtimeOnly("org.lwjgl:lwjgl::natives-linux")
    runtimeOnly("org.lwjgl:lwjgl::natives-macos")
    runtimeOnly("org.lwjgl:lwjgl::natives-macos-arm64")
    runtimeOnly("org.lwjgl:lwjgl-glfw::natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-glfw::natives-linux")
    runtimeOnly("org.lwjgl:lwjgl-glfw::natives-macos")
    runtimeOnly("org.lwjgl:lwjgl-glfw::natives-macos-arm64")

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
}

tasks.test {
    useJUnitPlatform()
}

// Helpful run configuration
tasks.register("run", JavaExec::class) {
    group = "application"
    mainClass.set("slatewindow.example.Main")
    classpath = sourceSets.main.get().runtimeClasspath
}
