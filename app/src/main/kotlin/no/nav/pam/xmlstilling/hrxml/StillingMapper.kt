package no.nav.pam.xmlstilling.hrxml

import no.nav.pam.xmlstilling.legacy.StillingBatch
import no.nav.pam.xmlstilling.rest.XmlStillingDto

object StillingMapper {

    fun toStillingDto(hrXmlStilling: HrXmlStilling): XmlStillingDto {
        return XmlStillingDto(
                hrXmlStilling.arbeidsgiver,
                null,
                hrXmlStilling.bedriftspresentasjon,
                hrXmlStilling.stillingsBeskrivelse,
                hrXmlStilling.stillingsTittel,
                hrXmlStilling.soknadsFrist?.atStartOfDay(),
                makeExternalId(hrXmlStilling),
                hrXmlStilling.publiseresFra?.atStartOfDay(),
                hrXmlStilling.sistePubliseringsdato?.atStartOfDay(),
                hrXmlStilling.mottatt
        )
    }

    fun makeExternalId(hrXmlStilling: HrXmlStilling): String {
        return hrXmlStilling.stillingId + "_" + hrXmlStilling.arbeidsgiver + "_" + hrXmlStilling.leverandor
    }

    fun toStillingDtos(stillingBatchEntries: List<StillingBatch.Entry>): List<XmlStillingDto> {
        return stillingBatchEntries
                .map { entry -> HrXmlStilingParser.parse(entry.stillingXml, entry.mottattDato) }
                .map { hrXmlStilling -> toStillingDto(hrXmlStilling) }
    }
}