package no.nav.pam.xmlstilling.rest

import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser
import no.nav.pam.xmlstilling.hrxml.StillingMapper
import no.nav.pam.xmlstilling.legacy.StillingBatch
import java.time.LocalDateTime

private val batch = StillingBatch()

class StillingFeed (
        stillingbatch: StillingBatch = batch
) {

    fun hentStillinger(mottattDato: LocalDateTime): List<XmlStillingDto> {
        return hent(mottattDato, 5)
                .map { entry -> StillingMapper.toStillingDto(HrXmlStilingParser.parse(entry.stillingXml), entry.mottattDato, entry.eksternBrukerRef) }
    }

    private val hent = fun(mottattDato: LocalDateTime, count: Int): List<StillingBatch.Entry> {
        return stillingbatch.fetchBatch(mottattDato, count)
    }
}