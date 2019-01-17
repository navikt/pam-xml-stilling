package no.nav.pam.xmlstilling.legacy

import kotliquery.Row
import kotliquery.queryOf

class StillingBatch {

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


    val fetchAll = queryOf("""select * from "SIX_KOMP"."STILLING_BATCH"""").map(toStillingBatchEntry).asList

}