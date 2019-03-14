package no.nav.pam.xmlstilling.rest

import io.prometheus.client.Counter
import mu.KotlinLogging
import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser
import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser.HrXmlValue.*
import no.nav.pam.xmlstilling.hrxml.StillingMapper
import no.nav.pam.xmlstilling.legacy.StillingBatch
import no.nav.pam.xmlstilling.legacy.StillingIdMapping
import java.time.LocalDateTime

class StillingFeed (
        private val stillingbatch: StillingBatch = StillingBatch() ,
        private val arenaIdProvider: (String, String, String) -> Int? = StillingIdMapping().fetchArenaId
) {

    private val log = KotlinLogging.logger { }

    fun hentStillinger(mottattDato: LocalDateTime): List<XmlStillingDto> {
        return stillingbatch.fetchBatch(mottattDato)
                .map { entry ->
                    log.info( "Parsing xml entry: {}, mottatt: {} - {} - {}", entry.stillingBatchId, entry.mottattDato, entry.eksternBrukerRef, entry.arbeidsgiver)
                    val hrXmlValues: Map<HrXmlStilingParser.HrXmlValue, String> = HrXmlStilingParser.parse(entry.stillingXml)
                    val arenaId: Int? = arenaIdProvider(hrXmlValues.getValue(STILLING_ID), entry.eksternBrukerRef, hrXmlValues.getValue(ARBEIDSGIVER))
                    StillingMapper.toStillingDto(hrXmlValues, entry.mottattDato, entry.eksternBrukerRef, arenaId)
                }
    }

}