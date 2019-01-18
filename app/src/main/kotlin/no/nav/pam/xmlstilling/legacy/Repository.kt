package no.nav.pam.xmlstilling.legacy

import kotliquery.HikariCP
import kotliquery.sessionOf
import kotliquery.using

class Repository{

    fun fetchBatch(): List<StillingBatch.Entry> {
        return using(sessionOf(HikariCP.dataSource())) { session ->
            return@using session.run(StillingBatch().fetchAll)
        }
    }

}