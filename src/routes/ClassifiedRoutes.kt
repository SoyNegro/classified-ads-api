package com.thedarksideofcode.routes

import com.thedarksideofcode.model.Classified
import com.thedarksideofcode.service.ClassifiedService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.di
import java.time.LocalDateTime

fun Route.classifiedRouting() {
    authenticate("public-api") {
        val classifiedService by di().instance<ClassifiedService>()
        route("/classified/") {
            get("{classifiedId}") {
                val classifiedId: String =
                    call.parameters["classifiedId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val classified =
                    classifiedService.getClassifiedById(classifiedId)
                        ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(classified)

            }
            get("{subcategory}/{page}/{size}") {
                val subcategory: String =
                    call.parameters["subcategory"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val page = classifiedService.validateSize(call.parameters["page"]?.toInt())
                val size = classifiedService.validateSize(call.parameters["size"]?.toInt())
                val classifiedPage =
                    classifiedService.getClassifiedBySubCategory(subcategory, page, size) ?: return@get call.respond(
                        HttpStatusCode.NotFound
                    )
                call.respond(classifiedPage)

            }
            get("{state}/{page}/{size}") {
                val state: String =
                    call.parameters["state"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val page = classifiedService.validateSize(call.parameters["page"]?.toInt())
                val size = classifiedService.validateSize(call.parameters["size"]?.toInt())
                val classifiedPage =
                    classifiedService.getClassifiedByState(state, page, size)
                        ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(classifiedPage)

            }
            get("{city}/{page}/{size}") {
                val city: String =
                    call.parameters["city"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val page = classifiedService.validateSize(call.parameters["page"]?.toInt())
                val size = classifiedService.validateSize(call.parameters["size"]?.toInt())
                val classifiedPage =
                    classifiedService.getClassifiedByCity(city, page, size)
                        ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(classifiedPage)

            }
            get("{publishedDate}/{page}/{size}") {
                val publishedDate: LocalDateTime =
                    call.receive()
                val page = classifiedService.validateSize(call.parameters["page"]?.toInt())
                val size = classifiedService.validateSize(call.parameters["size"]?.toInt())
                val classifiedPage =
                    classifiedService.getClassifiedByPublishedDate(publishedDate, page, size)
                        ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(classifiedPage)

            }
            post {
                val classified: Classified
                try {
                    classified = call.receive()
                    classifiedService.save(classified)
                    call.respond(HttpStatusCode.Accepted)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
            put {
                try {
                    val classified: Classified = call.receive()
                    classifiedService.update(classified)
                    call.respond(HttpStatusCode.Accepted)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
            delete("{classifiedId}") {
                val classifiedId: String =
                    call.parameters["classifiedId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                classifiedService.deleteById(classifiedId)
                call.respond(HttpStatusCode.Accepted)
            }
        }
    }
}