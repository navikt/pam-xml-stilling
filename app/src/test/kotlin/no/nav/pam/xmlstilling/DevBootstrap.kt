package no.nav.pam.xmlstilling

import no.nav.pam.xmlstilling.legacy.loadBasicTestData

val testEnvironment = Environment(
        xmlStillingDataSourceUrl = "jdbc:h2:mem:test",
//        xmlStillingDataSourceUrl = "jdbc:oracle:thin:@localhost:1521/schema",
        username = "user",
        password = "pass"
)

fun main(args: Array<String>) {

    Bootstrap.start(testEnvironment) { loadBasicTestData }

}
