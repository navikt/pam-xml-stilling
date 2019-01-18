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
import no.nav.pam.xmlstilling.legacy.Repository
import no.nav.pam.xmlstilling.platform.naisApi

fun main(args: Array<String>) {

    start(Environment())

}

object Bootstrap {

    private val log = KotlinLogging.logger {  }

    fun start(env: Environment, afterEnvLoaded: () -> Unit = {} ) {

        HikariCP.default(env.xmlStillingDataSourceUrl, env.username, env.password)

        afterEnvLoaded()

        Bootstrap.webApplication().start(wait = true)
    }

    fun webApplication(port: Int = 9020) : ApplicationEngine {
        return embeddedServer(Netty, port) {
            install(ContentNegotiation) {
                gson { setPrettyPrinting() }
            }
            routing {
                naisApi()
                get("load") {
                    call.respond(Repository().fetchBatch())
                }

            }
        }
    }
}