plugins {
    id("java")
}

group = "dev.httpmarco"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-suite-engine")
    testImplementation("org.awaitility:awaitility:4.2.2")


    testAnnotationProcessor(rootProject.libs.lombok)

    implementation(rootProject.libs.netty5)

    implementation(rootProject.libs.lombok)
    annotationProcessor(rootProject.libs.lombok)


    implementation(rootProject.libs.annotations)

    implementation(rootProject.libs.log4j2)
    testImplementation(rootProject.libs.log4j2.simple)
}

tasks.test {
    useJUnitPlatform()
}