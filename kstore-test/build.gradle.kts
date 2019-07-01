version = "unspecified"

dependencies {
    val kotlinVersion: String by project
    val junitJupiterVersion: String by project

    compile(project(":kstore"))
    compile("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    runtime("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    compile("io.github.benas:random-beans:3.7.0")
    compile("io.github.benas:random-beans-validation:3.7.0")
}
