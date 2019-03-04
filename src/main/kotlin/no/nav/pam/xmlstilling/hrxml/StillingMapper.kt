package no.nav.pam.xmlstilling.hrxml

import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser.HrXmlValue.*
import no.nav.pam.xmlstilling.legacy.StillingBatch
import no.nav.pam.xmlstilling.rest.XmlStillingDto
import java.time.LocalDate
import java.time.LocalDateTime

object StillingMapper {

    fun toStillingDto(hrXmlValues: Map<HrXmlStilingParser.HrXmlValue, String>, mottatt: LocalDateTime, eksternBrukerRef: String): XmlStillingDto {
        return XmlStillingDto(
                arbeidsgiver = hrXmlValues.getValue(ARBEIDSGIVER),
                eksternBrukerRef = eksternBrukerRef,
                arbeidsgiverBedriftspresentasjon = hrXmlValues.get(ARBEIDSGIVER_BEDRIFTSPRESENTASJON),
                stillingsbeskrivelse = hrXmlValues.get(STILLINGSBESKRIVELSE),
                stillingstittel = hrXmlValues.getValue(STILLINGSTITTEL),
                soknadsfrist = getLocalDate(hrXmlValues.get(SOKNADSFRIST)),
                eksternId = arrayOf(
                        hrXmlValues.get(STILLING_ID),
                        hrXmlValues.get(ARBEIDSGIVER),
                        eksternBrukerRef)
                        .joinToString("_"),
                publiseresFra = getLocalDate(hrXmlValues.get(PUBLISERES_FRA)),
                sistePubliseringsdato = getLocalDate(hrXmlValues.get(SISTE_PUBLISERINGSDATO)),
                mottattTidspunkt = mottatt,
                antallStillinger = hrXmlValues.getValue(ANTALL_STILLINGER).toInt(),
                arbeidssted = hrXmlValues.getValue(ARBEIDSSTED),
                stillingsprosent = hrXmlValues.getValue(STILLINGSPROSENT).toFloatOrNull(),
                kontaktinfoPerson = hrXmlValues.getValue(KONTAKTINFO_PERSON),
                kontaktinfoTelefon = hrXmlValues.getValue(KONTAKTINFO_TELEFON),
                kontaktinfoEpost = hrXmlValues.getValue(KONTAKTINFO_EPOST),
                arbeidsgiverAdresse = hrXmlValues.getValue(ARBEIDSGIVER_ADRESSE),
                arbeidsgiverPostnummer = hrXmlValues.getValue(ARBEIDSGIVER_POSTNR),
                arbeidsgiverWebadresse = hrXmlValues.getValue(ARBEIDSGIVER_WEBADRESSE)
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