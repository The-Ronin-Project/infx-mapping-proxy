package com.projectronin.blueprint

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RoninBlueprintApplication

fun main(args: Array<String>) {
    runApplication<RoninBlueprintApplication>(*args)
}
