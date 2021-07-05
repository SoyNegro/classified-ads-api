package com.thedarksideofcode

import com.fasterxml.jackson.databind.SerializationFeature
import com.thedarksideofcode.model.AccessCredential
import com.thedarksideofcode.model.Classified
import com.thedarksideofcode.routes.registerRoute
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import org.kodein.di.ktor.di
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(HSTS) {
        includeSubDomains = true
    }
// GET and POST method are accepted by default
    install(CORS) {
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    // will send this header with each response
    install(DefaultHeaders) {
        header("X-Classified-API", "Reading classifieds content")
    }

    install(Authentication) {
        basic("public-api") {
            realm = "Access to /api"
            validate { credential ->
                if (DB.credentialCollection.findOne(
                        and(
                            AccessCredential::apiKey eq credential.name,
                            AccessCredential::password eq credential.password
                        )
                    ) != null
                ) UserIdPrincipal(credential.name) else null
            }
        }
    }

    di {
        bindService()

    }

    registerRoute()
}

class DB {
    companion object {
        private val client = KMongo.createClient().coroutine
        private val database = client.getDatabase("classifieds")
        val classifiedsCollection = database.getCollection<Classified>()
        val credentialCollection = database.getCollection<AccessCredential>()
    }
}


