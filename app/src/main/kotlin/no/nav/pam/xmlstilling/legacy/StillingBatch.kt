package no.nav.pam.xmlstilling.legacy

import kotliquery.*
import kotliquery.action.ListResultQueryAction

private val oracleFetchQuery = """
    select *
    from "SIX_KOMP"."STILLING_BATCH
    ORDER BY STILLING_BATCH_ID
    where STILLING_BATCH_ID > ? order by STILLING_BATCH_ID
    fetch first 10 rows only""".trimIndent()

class StillingBatch (
        fetchQuery: String = oracleFetchQuery
) {

    data class Entry (
        val stillingBatchId: Int,
        val eksternBrukerRef: String?,
        val stillingXml: String?,
        val mottattDato: java.time.LocalDate?,
        val behandletDato: java.time.LocalDate?,
        val behandletStatus: String?,
        val arbeidsgiver: String?)


    val toStillingBatchEntry: (Row) -> Entry = { row ->
        Entry(
                stillingBatchId = row.int("STILLING_BATCH_ID"),
                eksternBrukerRef = row.stringOrNull("EKSTERN_BRUKER_REF"),
                stillingXml = row.stringOrNull("STILLING_XML"),
                mottattDato = row.localDateOrNull("MOTTATT_DATO"),
                behandletDato = row.localDateOrNull("BEHANDLET_DATO"),
                behandletStatus = row.stringOrNull("BEHANDLET_STATUS"),
                arbeidsgiver = row.stringOrNull("ARBEIDSGIVER")
        )
    }


    private val fetchbatchQuery = fun(start : Int): ListResultQueryAction<Entry> {
        return queryOf(fetchQuery, start)
                .map(toStillingBatchEntry)
                .asList
    }


    fun fetchBatch(start: Int = 0, count: Int = 100): List<StillingBatch.Entry>
    {
        return using(sessionOf(HikariCP.dataSource())) { session ->
            return@using session.run(fetchbatchQuery(start))
        }
    }
}