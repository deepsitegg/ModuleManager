allprojects {
    group = "com.jazzkuh.modulemanager"
    version = "1.0-SNAPSHOT"
}

subprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    // Each subproject applies `maven-publish` itself and declares its own
    // publication, so only attach the target repository here.
    plugins.withId("maven-publish") {
        configure<PublishingExtension> {
            repositories {
                maven {
                    name = "deepsite"
                    url = uri(
                        if (version.toString().endsWith("SNAPSHOT"))
                            "https://maven.deepsite.gg/snapshots"
                        else
                            "https://maven.deepsite.gg/releases"
                    )
                    credentials {
                        username = System.getenv("DEEPSITE_MAVEN_NAME")
                            ?: project.findProperty("deepsiteUsername") as String?
                        password = System.getenv("DEEPSITE_MAVEN_SECRET")
                            ?: project.findProperty("deepsitePassword") as String?
                    }
                    // Reposilite expects preemptive basic auth
                    authentication {
                        create<BasicAuthentication>("basic")
                    }
                }
            }
        }
    }
}
