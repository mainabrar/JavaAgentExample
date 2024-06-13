plugins {
    id("java")
}

group = "abr.tas.javaagentexample"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-util:9.5")
    implementation("org.ow2.asm:asm-commons:9.5")
}

tasks.jar {
    manifest {
        attributes(
            "Premain-Class" to "abr.tas.javaagentexample.JavaAgentExample",
            "Agent-Class" to "abr.tas.javaagentexample.JavaAgentExample",
            "Can-Redefine-Classes" to "true",
            "Can-Retransform-Classes" to "true"
        )
    }
}