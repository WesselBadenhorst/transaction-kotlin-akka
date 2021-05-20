import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
}

group = "me.wessel"
version = "1.0-SNAPSHOT"

val scalaBinaryVersion = "2.13"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("com.typesafe.akka:akka-bom_$scalaBinaryVersion:2.6.14"))
    implementation("com.typesafe.akka:akka-actor-typed_$scalaBinaryVersion")
    implementation("org.slf4j:slf4j-simple:1.7.27")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("com.typesafe.akka:akka-actor-testkit-typed_$scalaBinaryVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
