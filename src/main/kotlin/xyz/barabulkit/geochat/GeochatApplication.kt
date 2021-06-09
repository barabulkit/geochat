package xyz.barabulkit.geochat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GeochatApplication

fun main(args: Array<String>) {
    runApplication<GeochatApplication>(*args)
}
