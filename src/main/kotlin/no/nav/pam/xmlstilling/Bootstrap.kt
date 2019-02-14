package no.nav.pam.xmlstilling

import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotliquery.HikariCP
import mu.KotlinLogging
import no.nav.pam.xmlstilling.Bootstrap.start
import no.nav.pam.xmlstilling.legacy.StillingBatch
import no.nav.pam.xmlstilling.platform.naisApi
import no.nav.pam.xmlstilling.rest.StillingFeed
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter.ISO_INSTANT

fun main(args: Array<String>) {

    Bootstrap.initializeDatabase(Environment())

    start(webApplication())

}

fun webApplication(port: Int = 9020, repo: StillingBatch = StillingBatch(), feed: StillingFeed = StillingFeed()): ApplicationEngine {
    return embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                registerTypeAdapter(LocalDateTime::class.java, JsonSerializer<LocalDateTime> { localDateTime, type, context ->
                    JsonPrimitive(ISO_INSTANT.format(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                })
            }
        }
        routing {
            naisApi()
            get("load/{yyyy}/{MM}/{dd}/{HH}/{mm}/{ss}") {
                call.respond(feed.hentStillinger(LocalDateTime.parse(
                        call.parameters["yyyy"] + "-"
                                + call.parameters["MM"] + "-"
                                + call.parameters["dd"] + "T"
                                + call.parameters["HH"] + ":"
                                + call.parameters["mm"] + ":"
                                + call.parameters["ss"])))
            }
        }
    }
}

object Bootstrap {

    private val log = KotlinLogging.logger { }

    fun initializeDatabase(env: Environment) {
        log.debug("Initializing database connection pool")
        HikariCP.default(env.xmlStillingDataSourceUrl, env.username, env.password)
    }

    fun start(webApplication: ApplicationEngine) {
        log.debug("Starting weg application")
        webApplication.start(wait = true)
    }
}