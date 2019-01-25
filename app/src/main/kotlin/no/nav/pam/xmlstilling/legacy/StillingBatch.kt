package no.nav.pam.xmlstilling.legacy

import kotliquery.*
import kotliquery.action.ListResultQueryAction

private val oracleFetchQuery = """
    select *
    from "SIX_KOMP"."STILLING_BATCH
    where STILLING_BATCH_ID > ?
    order by STILLING_BATCH_ID
    fetch first ? rows only""".trimIndent()

class StillingBatch (
        fetchQuery: String = oracleFetchQuery
) {

    data class Entry (
        val stillingBatchId: Int,
        val eksternBrukerRef: String?,
        val stillingXml: String,
        val mottattDato: java.time.LocalDate,
        val behandletDato: java.time.LocalDate?,
        val behandletStatus: String?,
        val arbeidsgiver: String?)


    val toStillingBatchEntry: (Row) -> Entry = { row ->
        Entry(
                stillingBatchId = row.int("STILLING_BATCH_ID"),
                eksternBrukerRef = row.stringOrNull("EKSTERN_BRUKER_REF"),
                stillingXml = row.string("STILLING_XML"),
                mottattDato = row.localDate("MOTTATT_DATO"),
                behandletDato = row.localDateOrNull("BEHANDLET_DATO"),
                behandletStatus = row.stringOrNull("BEHANDLET_STATUS"),
                arbeidsgiver = row.stringOrNull("ARBEIDSGIVER")
        )
    }


    private val fetchbatchQuery = fun(start: Int, count: Int): ListResultQueryAction<Entry> {
        return queryOf(fetchQuery, start, if(count > 10) 10 else count)
                .map(toStillingBatchEntry)
                .asList
    }


    fun fetchBatch(start: Int, count: Int): List<StillingBatch.Entry>
    {
        return using(sessionOf(HikariCP.dataSource())) { session ->
            return@using session.run(fetchbatchQuery(start, count))
        }
    }
}