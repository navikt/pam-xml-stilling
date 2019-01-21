package no.nav.pam.xmlstilling

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

fun main(args: Array<String>) {

    Bootstrap.initializeDatabase(Environment())

    start(webApplication())

}

fun webApplication(port: Int = 9020, repo: StillingBatch = StillingBatch()) : ApplicationEngine {
    return embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        routing {
            naisApi()
            get("load/{start}/count/{count}") {
                call.respond(repo.fetchBatch(
                        start = call.parameters["applicationName"]!!.toInt(),
                        count = call.parameters["applicationName"]!!.toInt()))
            }

        }
    }
}

object Bootstrap {

    private val log = KotlinLogging.logger {  }

    fun initializeDatabase(env: Environment) {
        log.debug("Initializing database connection pool")
        HikariCP.default(env.xmlStillingDataSourceUrl, env.username, env.password)
    }

    fun start(webApplication: ApplicationEngine) {
        log.debug("Starting weg application")
        webApplication.start(wait = true)
    }
}