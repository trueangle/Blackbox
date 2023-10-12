import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.signing
import java.util.*

plugins {
    `maven-publish`
    signing
}

// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.secretKeyRingFile"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null

// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

fun getExtraString(name: String) = ext[name]?.toString()

publishing {
    // Configure maven central repository
    repositories {
        maven {
            name = "sonatype"
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            val url = if (version.toString().endsWith("SNAPSHOT")) {
                uri(snapshotsRepoUrl)
            } else {
                uri(releasesRepoUrl)
            }

            setUrl(url)
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    // Configure all publications
    publications.withType<MavenPublication> {
        artifact(javadocJar.get())

        pom {
            name.set("Blackbox")
            description.set("A lightweight framework that includes a set of architecture tools and approaches for building scalable, fully multiplatform applications (iOS, Android, Desktop, Web) by leveraging Compose Multiplatform.")
            url.set("https://github.com/trueangle/Blackbox")

            licenses {
                license {
                    name.set("Apache-2.0 license")
                    url.set("https://opensource.org/license/apache-2-0/")
                }
            }
            developers {
                developer {
                    id.set("trueangle")
                    name.set("Viacheslav Ivanovichev")
                    email.set("s.ivanovichev@gmail.com")
                }
            }
            scm {
                url.set("https://github.com/trueangle/Blackbox")
                connection.set("scm:git:git://github.com/trueangle/Blackbox.git")
                developerConnection.set("scm:git:git://github.com/trueangle/Blackbox.git")
            }
        }
    }
}

signing {
    sign(publishing.publications)
}