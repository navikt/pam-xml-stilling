package no.nav.pam.xmlstilling.rest

import io.prometheus.client.Counter
import mu.KotlinLogging
import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser
import no.nav.pam.xmlstilling.hrxml.StillingMapper
import no.nav.pam.xmlstilling.legacy.StillingBatch
import java.time.LocalDateTime

class StillingFeed (
        private val stillingbatch: StillingBatch = StillingBatch()
) {

    companion object {
        val fetchedStillingerCounter = Counter
                .build()
                .namespace("pam_stilling")
                .name("xml_stilling_loaded")
                .help("Number of stillinger loaded from xmlstilling database")
                .register()
    }


    private val log = KotlinLogging.logger { }

    fun hentStillinger(mottattDato: LocalDateTime): List<XmlStillingDto> {
        return stillingbatch.fetchBatch(mottattDato)
                .map { entry ->
                    log.info( "Parsing xml entry: {}, mottatt: {} - {} - {}", entry.stillingBatchId, entry.mottattDato, entry.eksternBrukerRef, entry.arbeidsgiver)
                    val hrXmlValues: Map<HrXmlStilingParser.HrXmlValue, String> = HrXmlStilingParser.parse(entry.stillingXml)
                    StillingMapper.toStillingDto(hrXmlValues, entry.mottattDato, entry.eksternBrukerRef)
                }.also {
                    results -> fetchedStillingerCounter.inc(results.size.toDouble())
                }
    }

}