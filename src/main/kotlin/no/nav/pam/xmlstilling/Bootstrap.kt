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
import mu.KotlinLogging
import no.nav.pam.xmlstilling.legacy.DatasourceProvider
import no.nav.pam.xmlstilling.platform.health
import no.nav.pam.xmlstilling.platform.metrics
import no.nav.pam.xmlstilling.rest.StillingFeed
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter.ISO_INSTANT

private val log = KotlinLogging.logger { }

fun main(args: Array<String>) {

    val env = Environment()
    DatasourceProvider.initPostgresDataSource(env.jdbcUrl, env.mountPath, env.dbName)

    webApplication().start(wait = true)

}

fun webApplication(port: Int = 9020, feed: StillingFeed = StillingFeed()): ApplicationEngine {
    return embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                registerTypeAdapter(LocalDateTime::class.java, JsonSerializer<LocalDateTime> { localDateTime, _, _ ->
                    JsonPrimitive(ISO_INSTANT.format(localDateTime.atOffset(ZoneOffset.UTC).toInstant()))
                })
            }
        }
        routing {
            health()
            metrics()
            get("load/{year}/{month}/{day}/{hour}/{minute}/{second}") {
                call.respond(feed.hentStillinger(LocalDateTime.of(
                        call.parameters["year"]!!.toInt(),
                        call.parameters["month"]!!.toInt(),
                        call.parameters["day"]!!.toInt(),
                        call.parameters["hour"]!!.toInt(),
                        call.parameters["minute"]!!.toInt(),
                        call.parameters["second"]!!.toInt())))
            }
        }
    }
}

