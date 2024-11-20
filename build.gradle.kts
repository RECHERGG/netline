plugins {
    id("java")
}

group = "dev.httpmarco"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform(rootProject.libs.junitBom))
    testImplementation(rootProject.libs.bundles.testing)

    testAnnotationProcessor(rootProject.libs.lombok)

    implementation(rootProject.libs.netty5)
    implementation(rootProject.libs.bundles.utils)

    annotationProcessor(rootProject.libs.lombok)
}

tasks.test {
    useJUnitPlatform()
}