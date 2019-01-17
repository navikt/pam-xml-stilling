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
import no.nav.pam.xmlstilling.legacy.Repository
import no.nav.pam.xmlstilling.platform.naisApi

fun main(args: Array<String>) {

    val env = Environment()

    HikariCP.default(env.xmlStillingDataSourceUrl, env.username, env.password)

    Bootstrap.application().start(wait = true)

}

object Bootstrap {

    private val log = KotlinLogging.logger {  }

    fun application() : ApplicationEngine {
        return embeddedServer(Netty, 9020) {
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