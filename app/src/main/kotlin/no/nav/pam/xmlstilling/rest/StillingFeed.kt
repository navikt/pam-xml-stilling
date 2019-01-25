package no.nav.pam.xmlstilling.rest

import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser
import no.nav.pam.xmlstilling.hrxml.HrXmlStilling
import no.nav.pam.xmlstilling.hrxml.StillingMapper
import no.nav.pam.xmlstilling.legacy.StillingBatch

class StillingFeed {

    fun hentStillinger(start: Int, count: Int): List<XmlStillingDto> {
        val batch: StillingBatch = StillingBatch()
        return batch.fetchBatch(start, count)
                .map { entry -> HrXmlStilingParser.parse(entry.stillingXml, entry.mottattDato) }
                .map { hrXmlStilling -> StillingMapper.toStillingDto(hrXmlStilling) }
    }
}