package no.nav.pam.xmlstilling.hrxml

import java.time.LocalDate
import java.util.*

data class HrXmlStilling (
    val stillingId: String?,
    val leverandor: String,
    val arbeidsgiver: String,
    val stillingsTittel: String,
    val stillingsBeskrivelse: String,
    val antallStillinger: Int,
    val bedriftspresentasjon: String,
    val arbeidssted: String,
    val stillingsProsent: Float,
    val kontaktPerson: String,
    val kontaktPersonTlfnr: String,
    val publiseresFra: Date?,
    val sistePubliseringsdato: Date?,
    val soknadsFrist: Date?,
    val arbeidsgiverAdresse: String,
    val arbeidsgiverPostNr: String,
    val url: String,
    val mottatt: LocalDate
)
