package no.nav.pam.xmlstilling.rest

import mu.KotlinLogging
import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser
import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser.HrXmlValue.*
import no.nav.pam.xmlstilling.hrxml.StillingMapper
import no.nav.pam.xmlstilling.legacy.StillingBatch
import no.nav.pam.xmlstilling.legacy.StillingIdMapping
import java.time.LocalDateTime

private val batch = StillingBatch()
private val idMapping = StillingIdMapping()

class StillingFeed (
        stillingbatch: StillingBatch = batch ,
        stillingIdMapping: StillingIdMapping = idMapping
) {

    private val log = KotlinLogging.logger { }

    fun hentStillinger(mottattDato: LocalDateTime): List<XmlStillingDto> {
        return hent(mottattDato)
                .map { entry ->
                    log.info( "Parsing xml entry: {}, mottatt: {} - {} - {}", entry.stillingBatchId, entry.mottattDato, entry.eksternBrukerRef, entry.arbeidsgiver)
                    val hrXmlValues: Map<HrXmlStilingParser.HrXmlValue, String> = HrXmlStilingParser.parse(entry.stillingXml)
                    val arenaId: Int? = hentArenaId(hrXmlValues.getValue(STILLING_ID), entry.eksternBrukerRef, hrXmlValues.getValue(ARBEIDSGIVER))
                    StillingMapper.toStillingDto(hrXmlValues, entry.mottattDato, entry.eksternBrukerRef, arenaId)
                }
    }

    private val hent = fun(mottattDato: LocalDateTime): List<StillingBatch.Entry> {
        return stillingbatch.fetchBatch(mottattDato)
    }

    private val hentArenaId = fun(eksternStillingId: String, eksternAktorNavn: String, arbeidsgiver: String): Int? {
        return stillingIdMapping.fetchArenaId(eksternStillingId, eksternAktorNavn, arbeidsgiver)
    }
}