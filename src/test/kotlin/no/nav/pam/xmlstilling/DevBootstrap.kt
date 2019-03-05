package no.nav.pam.xmlstilling

import no.nav.pam.xmlstilling.legacy.*
import no.nav.pam.xmlstilling.rest.StillingFeed

val testEnvironment = Environment(
        xmlStillingDataSourceUrl = "jdbc:h2:mem:test;",
        username = "user",
        password = "pass"
)

fun main(args: Array<String>) {

    val batch = StillingBatch()

    Bootstrap.initializeDatabase(testEnvironment)
            .run { createStillingBatchTable() }
            .also { loadBasicTestData() }
            .also { loadExtendedTestData() }

    Bootstrap.start(webApplication(repo = batch, feed = StillingFeed(batch)))

}
