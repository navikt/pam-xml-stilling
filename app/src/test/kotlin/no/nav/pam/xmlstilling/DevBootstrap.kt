package no.nav.pam.xmlstilling

import no.nav.pam.xmlstilling.legacy.StillingBatch
import no.nav.pam.xmlstilling.legacy.h2FetchQuery
import no.nav.pam.xmlstilling.legacy.loadBasicTestData
import no.nav.pam.xmlstilling.legacy.loadExtendedTestData

val testEnvironment = Environment(
        xmlStillingDataSourceUrl = "jdbc:h2:mem:test",
        username = "user",
        password = "pass"
)

fun main(args: Array<String>) {

    Bootstrap.initializeDatabase(testEnvironment)

    loadBasicTestData()
    loadExtendedTestData()

    Bootstrap.start(webApplication(repo = StillingBatch(fetchQuery = h2FetchQuery)))

}
