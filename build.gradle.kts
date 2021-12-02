plugins {
    `maven-publish`
    kotlin("jvm") version "1.6.0"
    id("org.jetbrains.dokka") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

dependencies {

    compileOnly("com.sylphmc:raccicore:0.3.1")
    compileOnly("org.purpurmc.purpur:purpur-api:1.18-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0") // This is needed so that it doesn't put itself in your final Jar

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

}

java {
    toolchain {languageVersion.set(JavaLanguageVersion.of(17))}
}

tasks {

    withType<Test> {
        useJUnitPlatform()
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("plugin.yml") {
                expand("version" to project.version)
            }
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    val sourcesJar by registering(Jar::class) {
        dependsOn(JavaPlugin.CLASSES_TASK_NAME)
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    val javadocJar by registering(Jar::class) {
        dependsOn("dokkaJavadoc")
        archiveClassifier.set("javadoc")
        from(dokkaJavadoc.get().outputDirectory)
    }

    artifacts {
        archives(sourcesJar)
        archives(javadocJar)
    }

    build {
        dependsOn(publishToMavenLocal)
    }

}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        artifact(tasks["sourcesJar"])
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
    // Citizens API
    maven("https://repo.citizensnpcs.co")
    // Purpur
    maven("https://repo.purpurmc.org")
    // ACF
    maven("https://repo.aikar.co/content/groups/aikar/")
    // Kyori
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

// These are in the gradle.properties file
group = findProperty("group")!!
version = findProperty("version")!!