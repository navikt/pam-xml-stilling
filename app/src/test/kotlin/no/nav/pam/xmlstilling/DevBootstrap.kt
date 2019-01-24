package no.nav.pam.xmlstilling

import no.nav.pam.xmlstilling.legacy.*

val testEnvironment = Environment(
        xmlStillingDataSourceUrl = "jdbc:h2:mem:test",
        username = "user",
        password = "pass"
)

fun main(args: Array<String>) {

    Bootstrap.initializeDatabase(testEnvironment)
            .run { createStillingBatchTable() }
            .also { loadBasicTestData() }
            .also { loadExtendedTestData() }

    Bootstrap.start(webApplication(repo = StillingBatch(fetchQuery = h2FetchQuerySql)))

}
