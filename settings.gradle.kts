rootProject.name = "ronin-blueprint"

include(
    "ronin-blueprint-database",
    "ronin-blueprint-service"
)

pluginManagement {
    repositories {
        maven {
            url = uri("https://repo.devops.projectronin.io/repository/maven-public/")
        }
        mavenLocal()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://repo.devops.projectronin.io/repository/maven-public/")
        }
        mavenLocal()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("roningradle") {
            from("com.projectronin.services.gradle:ronin-gradle-catalog:2.3.12")
        }
        create("productcommon") {
            from("com.projectronin:ronin-product-common:2.9.23")
        }
    }
}
