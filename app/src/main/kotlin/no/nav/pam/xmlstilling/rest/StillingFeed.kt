package no.nav.pam.xmlstilling.rest

import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser
import no.nav.pam.xmlstilling.hrxml.StillingMapper
import no.nav.pam.xmlstilling.legacy.StillingBatch
import java.time.LocalDateTime

private val batch = StillingBatch()

class StillingFeed (
        stillingbatch: StillingBatch = batch
) {

    fun hentStillinger(timestamp: LocalDateTime): List<XmlStillingDto> {
        return hent(0, 5)
                .map { entry -> HrXmlStilingParser.parse(entry.stillingXml, entry.mottattDato) }
                .map { hrXmlStilling -> StillingMapper.toStillingDto(hrXmlStilling) }
    }

    private val hent = fun(start: Int, count: Int): List<StillingBatch.Entry> {
        return stillingbatch.fetchBatch(start, count)
    }
}