val ktlint by configurations.creating

repositories {
    // artipie - an open source project that is used to store ktlint and diktat dependencies
    maven {
        url = uri("https://central.artipie.com/akuleshov7/diktat")
    }
    mavenCentral()
    jcenter()


}
dependencies {
    ktlint("com.pinterest:ktlint:0.37.1-fork") {
        // need to exclude standard ruleset to use only diktat rules
        exclude("com.pinterest.ktlint", "ktlint-ruleset-standard")
    }

    // diktat ruleset
    ktlint("org.cqfn.diktat:diktat-rules:1.0.3") {
        exclude("org.slf4j", "slf4j-log4j12")
    }
}

val outputDir = "${project.buildDir}/reports/diktat/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val diktatCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Check Kotlin code style."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"

    // specify proper path to sources that should be checked here
    args = listOf("src/main/kotlin/**/*.kt")
}

val diktatFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"

    // specify proper path to sources that should be checked here
    args = listOf("-F", "src/main/kotlin/**/*.kt")
}