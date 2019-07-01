version = "unspecified"

dependencies {
    val kotlinVersion: String by project

    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    compile("io.reactivex:rxjava:1.3.4")
    compile("io.github.microutils:kotlin-logging:1.4.9")

}
