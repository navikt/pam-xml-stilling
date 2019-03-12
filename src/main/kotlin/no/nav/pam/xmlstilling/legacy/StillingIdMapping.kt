package no.nav.pam.xmlstilling.legacy

import kotliquery.HikariCP
import kotliquery.action.NullableResultQueryAction
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import mu.KotlinLogging

private val oracleFetchQuery = """
    select max(ARENA_STILLING_ID) as ARENA_STILLING_ID
        from "SIX_KOMP"."STILLING_ID_MAPPING"
        where EKSTERN_STILLING_ID = ?
        and EKSTERN_AKTOR_NAVN = ?
        and ARBEIDSGIVER = ?
        and ARENA_STILLING_ID is not null
""".trimIndent()

class StillingIdMapping (
        fetchQuery: String = oracleFetchQuery
) {

    private val log = KotlinLogging.logger { }


    private val fetchArenaIdQuery = fun(eksternStillingId: String, eksternAktorNavn: String, arbeidsgiver: String): NullableResultQueryAction<Int> {
        val query = queryOf(fetchQuery, eksternStillingId, eksternAktorNavn, arbeidsgiver)
        return query.map{row -> row.intOrNull("ARENA_STILLING_ID")}.asSingle
    }

    fun fetchArenaId(eksternStillingId: String, eksternAktorNavn: String, arbeidsgiver: String): Int? {
        return using(sessionOf(HikariCP.dataSource())) { session ->
            return@using session.run(fetchArenaIdQuery(eksternStillingId, eksternAktorNavn, arbeidsgiver))
        }
    }

}