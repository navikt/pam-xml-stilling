package no.nav.pam.xmlstilling.legacy

import kotliquery.*
import kotliquery.action.ListResultQueryAction
import mu.KotlinLogging
import java.time.LocalDateTime

private val oracleFetchQuery = """
    select *
        from "SIX_KOMP"."STILLING_BATCH"
        where BEHANDLET_STATUS != '-1'
        and MOTTATT_DATO > ?
        and MOTTATT_DATO < (
    	    select trunc(min(MOTTATT_DATO) + 1, 'DD') as NEXT_DAY
    	    from "SIX_KOMP"."STILLING_BATCH"
    	    where MOTTATT_DATO > ?)
        order by STILLING_BATCH_ID
""".trimIndent()

class StillingBatch (
        private val fetchQuery: String = oracleFetchQuery
) {

    private val log = KotlinLogging.logger { }

    data class Entry (
        val stillingBatchId: Int,
        val eksternBrukerRef: String,
        val stillingXml: String,
        val mottattDato: java.time.LocalDateTime,
        val behandletDato: java.time.LocalDate?,
        val behandletStatus: String?,
        val arbeidsgiver: String?)


    val toStillingBatchEntry: (Row) -> Entry = { row ->
        Entry(
                stillingBatchId = row.int("STILLING_BATCH_ID"),
                eksternBrukerRef = row.string("EKSTERN_BRUKER_REF"),
                stillingXml = row.string("STILLING_XML"),
                mottattDato = row.localDateTime("MOTTATT_DATO"),
                behandletDato = row.localDateOrNull("BEHANDLET_DATO"),
                behandletStatus = row.stringOrNull("BEHANDLET_STATUS"),
                arbeidsgiver = row.stringOrNull("ARBEIDSGIVER")
        )
    }


    fun fetchbatchQuery(mottattDato: LocalDateTime): ListResultQueryAction<Entry> {
        log.debug("Henter xml-stillinger etter: {} ", mottattDato)
        val query = queryOf(fetchQuery, mottattDato, mottattDato)
        log.debug("""query: "{}"""", query.statement)

        return query.map(toStillingBatchEntry).asList
    }


    fun fetchBatch(mottattDato: LocalDateTime): List<StillingBatch.Entry>
    {
        return using(sessionOf(DatasourceProvider.get())) { session ->
            return@using session.run(fetchbatchQuery(mottattDato))
        }
    }
}