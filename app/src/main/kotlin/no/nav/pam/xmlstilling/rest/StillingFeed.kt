package no.nav.pam.xmlstilling.rest

import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser
import no.nav.pam.xmlstilling.hrxml.HrXmlStilling
import no.nav.pam.xmlstilling.hrxml.StillingMapper
import no.nav.pam.xmlstilling.legacy.StillingBatch
import java.time.LocalDateTime

class StillingFeed {

    fun hentStillinger(timestamp: LocalDateTime): List<XmlStillingDto> {
        val batch: StillingBatch = StillingBatch()
        return batch.fetchBatch(0, 30)
                .map { entry -> HrXmlStilingParser.parse(entry.stillingXml, entry.mottattDato) }
                .map { hrXmlStilling -> StillingMapper.toStillingDto(hrXmlStilling) }
    }
}