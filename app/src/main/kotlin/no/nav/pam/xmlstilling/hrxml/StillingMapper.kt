package no.nav.pam.xmlstilling.hrxml

import io.ktor.util.toLocalDateTime
import no.nav.pam.xmlstilling.legacy.StillingBatch
import no.nav.pam.xmlstilling.rest.XmlStillingDto
import java.time.LocalDateTime

object StillingMapper {

    fun toStillingDto(hrXmlStilling: HrXmlStilling): XmlStillingDto {
        return XmlStillingDto(
                hrXmlStilling.arbeidsgiver,
                null,
                hrXmlStilling.bedriftspresentasjon,
                hrXmlStilling.stillingsBeskrivelse,
                hrXmlStilling.stillingsTittel,
                hrXmlStilling.soknadsFrist?.toLocalDateTime(),
                makeExternalId(hrXmlStilling),
                hrXmlStilling.publiseresFra?.toLocalDateTime(),
                hrXmlStilling.sistePubliseringsdato?.toLocalDateTime(),
                hrXmlStilling.mottatt.atStartOfDay()
        )
    }

    fun makeExternalId(hrXmlStilling: HrXmlStilling): String {
        return hrXmlStilling.stillingId + "_" + hrXmlStilling.arbeidsgiver + "_" + hrXmlStilling.leverandor
    }

    fun toStillingDtos(stillingBatchEntries: List<StillingBatch.Entry>): List<XmlStillingDto> {
        return stillingBatchEntries
                .map { entry -> HrXmlStilingParser.parse(entry.stillingXml, entry.mottattDato) }
                .map { hrXmlStilling -> StillingMapper.toStillingDto(hrXmlStilling) }
    }
}