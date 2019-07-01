import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.JavaVersion.*
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile


buildscript {
    val springBootVersion: String by project
    val kotlinVersion: String by project

    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
    }
}


plugins {
    val kotlinVersion: String by project

    kotlin("jvm") version kotlinVersion
    id("com.github.ksoichiro.build.info") version "0.2.0"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("application")
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
}

allprojects {
    val springBootVersion: String by project

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        jcenter()
        mavenCentral()
    }

    configure<DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
        }
    }

    tasks.named<KotlinJvmCompile>("compileKotlin") {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.named<KotlinJvmCompile>("compileTestKotlin") {
        kotlinOptions.jvmTarget = "1.8"
    }

    java {
        sourceCompatibility = VERSION_1_8
        targetCompatibility = VERSION_1_8
    }

    tasks.withType<Test> {
        useJUnitPlatform { }
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    dependencies {
        val kotlinVersion: String by project
        val junitJupiterVersion: String by project

        compile(kotlin("stdlib"))
        implementation(kotlin("reflect"))

        testCompile("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
        testCompile("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
        testCompile("org.assertj:assertj-core:3.9.0")
        testCompile("org.awaitility:awaitility:3.0.0")
    }
}

application {
    mainClassName = "org.kstore.demo.stars.Application"
}

dependencies {
    val kotlinVersion: String by project
    val log4jVersion: String by project

    compile(project(":kstore"))

    compile("org.springframework.boot:spring-boot-starter")
    compile("org.springframework.boot:spring-boot-starter-logging")
    compile("com.h2database:h2")
    compile("jline:jline:2.14.6")

    compile("io.projectreactor:reactor-core:3.1.2.RELEASE")
    compile("com.aol.simplereact:cyclops-react:2.0.0-RC1")
    compile("com.aol.cyclops:cyclops-reactor:9.0.0-MI7")
    compile("io.reactivex:rxjava:1.3.4")
    compile("org.reflections:reflections:0.9.11")
    compile("io.github.microutils:kotlin-logging:1.4.9")
    compile("org.hibernate.validator:hibernate-validator:6.0.7.Final")
    compile("org.hibernate.validator:hibernate-validator-annotation-processor:6.0.7.Final")
    compile("com.fasterxml.jackson.core:jackson-databind:2.8.9")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.4.1")

    testCompile(project(":random-beans-junit5"))
    testCompile(project(":kstore-test"))
    testCompile("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

    testCompile("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testCompile("io.projectreactor:reactor-test:3.1.2.RELEASE")

    testCompile("io.github.benas:random-beans:3.7.0")

    // To use Log4J"s LogManager
    testRuntime("org.apache.logging.log4j:log4j-core:$log4jVersion")
    testRuntime("org.apache.logging.log4j:log4j-jul:$log4jVersion")
}

tasks {
    withType<Jar> {
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
        // here zip stuff found in runtimeClasspath:
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
}