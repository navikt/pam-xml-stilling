package no.nav.pam.xmlstilling.rest

import java.time.LocalDateTime


data class XmlStillingDto (
        val arbeidsgiver: String,
        val eksternBrukerRef: String,
        val arbeidsgiverBedriftspresentasjon: String,
        val stillingsbeskrivelse: String,
        val stillingstittel: String,
        val soknadsfrist: LocalDateTime?,
        val eksternId: String,
        val publiseresFra: LocalDateTime?,
        val sistePubliseringsdato: LocalDateTime?,
        val mottattTidspunkt: LocalDateTime,
        val antallStillinger: Int?,
        val arbeidssted: String,
        val stillingsprosent: Float?,
        val kontaktinfoPerson: String,
        val kontaktinfoTelefon: String,
        val kontaktinfoEpost: String,
        val arbeidsgiverAdresse: String,
        val arbeidsgiverPostnummer: String,
        val arbeidsgiverWebadresse: String,
        val arenaId: Int?

)