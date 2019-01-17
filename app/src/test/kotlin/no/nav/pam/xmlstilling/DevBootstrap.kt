package no.nav.pam.xmlstilling

import kotliquery.HikariCP
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import no.nav.pam.xmlstilling.legacy.createSchema
import no.nav.pam.xmlstilling.legacy.createStillingBatchTable
import no.nav.pam.xmlstilling.legacy.insertStillingBatchEntry

fun main(args: Array<String>) {

    HikariCP.default("jdbc:h2:mem:test", "user", "pass")

    using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf(createSchema).asExecute)
        session.run(queryOf(createStillingBatchTable).asExecute)
    }

    using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf(insertStillingBatchEntry, 193164, "jobbnorge", null, "2018-01-23", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193165, "webcruiter", null, "2018-01-23", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
    }

    Bootstrap.application().start(wait = true)

}
