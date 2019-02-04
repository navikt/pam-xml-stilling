package no.nav.pam.xmlstilling.hrxml

import org.w3c.dom.Document
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

object HrXmlStilingParser {

    private val xPath: XPath = XPathFactory.newInstance().newXPath()

    fun parse(xml: String, mottatt: LocalDateTime, eksternBrukerRef: String): HrXmlStilling {
        val document: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml.byteInputStream(StandardCharsets.UTF_8))

        val stillingId: String = getString("""//Id/IdValue""", document)
        val arbeidsgiver: String = getString("""//EntityName""", document)
        val stillingsTittel: String = getString("""//ProfileName""", document)
        val stillingsBeskrivelse: String = getString("""//FormattedPositionDescription/Value""", document)
        val antallStillinger: Int = getString("""//NumberToFill""", document).toInt()
        val bedriftspresentasjon: String = getString("""//Organization""", document)
        val arbeidssted: String = getString("""//PhysicalLocation/Name""", document)
        val stillingsProsent: Float = getString("""//PositionSchedule/@percentage""", document).toFloat()
        val kontaktPerson: String = getString("""//FormattedName""", document)
        val kontaktPersonTlfnr: String = getString("""//HowToApply//FormattedNumber""", document)
        val publiseresFra: String? = getString("""//DistributionGuidelines""", document)
        val sistePubliseringsdato: String? = getString("""//MaximumEndDate""", document)
        val soknadsFrist: String? = getString("""//MaximumStartDate""", document)
        val arbeidsgiverAdresse: String = getString("""//PhysicalLocation//AddressLine""", document)
        val arbeidsgiverPostNr: String = getString("""//PhysicalLocation//PostalCode""", document)
        val url: String = getString("""//InternetWebAddress""", document)

        // Avklare om disse skal inkluderes
        val kontaktEpost: String = getString("""//HowToApply//InternetWebAddress""", document)
        val startSaaSnartSomMulig: String = getString("""//StartAsSoonAsPossible""", document)
        val ledigTil: String = getString("""//ExpectedEndDate""", document)

        return HrXmlStilling(
                stillingId,
                eksternBrukerRef,
                arbeidsgiver,
                stillingsTittel,
                stillingsBeskrivelse,
                antallStillinger,
                bedriftspresentasjon,
                arbeidssted,
                stillingsProsent,
                kontaktPerson,
                kontaktPersonTlfnr,
                getLocalDate(publiseresFra),
                getLocalDate(sistePubliseringsdato),
                getLocalDate(soknadsFrist),
                arbeidsgiverAdresse,
                arbeidsgiverPostNr,
                url,
                mottatt)
    }

    private fun getString(expression: String, document: Document): String {
        return xPath.compile(expression).evaluate(document, XPathConstants.STRING) as String
    }

    private fun getLocalDate(date: String?): LocalDate? {
        return if (date.isNullOrBlank()) null else LocalDate.parse(date)
    }

}