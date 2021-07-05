package com.thedarksideofcode.routes

import io.ktor.application.*
import io.ktor.routing.*

fun Route.publicClassifiedsRoute(){
route("/api/v1"){
    classifiedRouting()
}
}

fun Application.registerRoute(){
    routing {
        publicClassifiedsRoute()
    }
}