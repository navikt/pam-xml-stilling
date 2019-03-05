package no.nav.pam.xmlstilling.rest

import mu.KotlinLogging
import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser
import no.nav.pam.xmlstilling.hrxml.StillingMapper
import no.nav.pam.xmlstilling.legacy.StillingBatch
import java.time.LocalDateTime

private val batch = StillingBatch()

class StillingFeed (
        stillingbatch: StillingBatch = batch
) {

    private val log = KotlinLogging.logger { }

    fun hentStillinger(mottattDato: LocalDateTime): List<XmlStillingDto> {
        return hent(mottattDato)
                .map { entry ->
                    log.info( "Parsing xml entry: {}, mottatt: {} - {} - {}", entry.stillingBatchId, entry.mottattDato, entry.eksternBrukerRef, entry.arbeidsgiver)
                    StillingMapper.toStillingDto(HrXmlStilingParser.parse(entry.stillingXml), entry.mottattDato, entry.eksternBrukerRef)
                }
    }

    private val hent = fun(mottattDato: LocalDateTime): List<StillingBatch.Entry> {
        return stillingbatch.fetchBatch(mottattDato)
    }
}