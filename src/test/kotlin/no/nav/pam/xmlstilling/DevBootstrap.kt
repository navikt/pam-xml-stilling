package no.nav.pam.xmlstilling

import kotliquery.HikariCP
import no.nav.pam.xmlstilling.legacy.*
import no.nav.pam.xmlstilling.rest.StillingFeed

val testEnvironment = Environment(
        jdbcUrl = "jdbc:h2:mem:test;",
        dbName = "",
        mountPath = ""
)

fun main(args: Array<String>) {

    val batch = StillingBatch()

    DatasourceProvider.init(HikariCP.default(testEnvironment.jdbcUrl, "user", "pass"))

    createStillingBatchTable()
    loadBasicTestData()
    loadExtendedTestData()

    webApplication(feed = StillingFeed(batch)).start(wait = true)

}
