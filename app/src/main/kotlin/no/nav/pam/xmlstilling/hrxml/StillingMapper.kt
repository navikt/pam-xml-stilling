package no.nav.pam.xmlstilling.hrxml

import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser.HrXmlValue.*
import no.nav.pam.xmlstilling.legacy.StillingBatch
import no.nav.pam.xmlstilling.rest.XmlStillingDto
import java.time.LocalDate
import java.time.LocalDateTime

object StillingMapper {

    fun toStillingDto(hrXmlValues: Map<HrXmlStilingParser.HrXmlValue, String>, mottatt: LocalDateTime, eksternBrukerRef: String): XmlStillingDto {
        return XmlStillingDto(
                hrXmlValues.getValue(ARBEIDSGIVER),
                eksternBrukerRef,
                hrXmlValues.get(ARBEIDSGIVER_BEDRIFTSPRESENTASJON),
                hrXmlValues.get(STILLINGSBESKRIVELSE),
                hrXmlValues.getValue(STILLINGSTITTEL),
                getLocalDate(hrXmlValues.get(SOKNADSFRIST)),
                arrayOf(
                        hrXmlValues.get(STILLING_ID),
                        hrXmlValues.get(ARBEIDSGIVER),
                        eksternBrukerRef)
                        .joinToString("_"),
                getLocalDate(hrXmlValues.get(PUBLISERES_FRA)),
                getLocalDate(hrXmlValues.get(SISTE_PUBLISERINGSDATO)),
                mottatt,
                hrXmlValues.getValue(ANTALL_STILLINGER).toInt(),
                hrXmlValues.getValue(ARBEIDSSTED),
                hrXmlValues.getValue(STILLINGSPROSENT).toFloatOrNull(),
                hrXmlValues.getValue(KONTAKTINFO_PERSON),
                hrXmlValues.getValue(KONTAKTINFO_TELEFON),
                hrXmlValues.getValue(KONTAKTINFO_EPOST),
                hrXmlValues.getValue(ARBEIDSGIVER_ADRESSE),
                hrXmlValues.getValue(ARBEIDSGIVER_POSTNR),
                hrXmlValues.getValue(ARBEIDSGIVER_WEBADRESSE)
        )
    }

    private fun getLocalDate(date: String?): LocalDateTime? {
        return if (date.isNullOrBlank()) null else LocalDate.parse(date).atStartOfDay()
    }

    fun toStillingDtos(stillingBatchEntries: List<StillingBatch.Entry>): List<XmlStillingDto> {
        return stillingBatchEntries
                .map { entry -> toStillingDto(HrXmlStilingParser.parse(entry.stillingXml), entry.mottattDato, entry.eksternBrukerRef) }
    }
}