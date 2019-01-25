package no.nav.pam.xmlstilling.hrxml

import org.w3c.dom.Document
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDate
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

object HrXmlStilingParser {

    private val dateFormat: String = "yyyy-MM-dd";

    private val xPath: XPath = XPathFactory.newInstance().newXPath()

    fun parse(xml: String, mottatt: LocalDate): HrXmlStilling {
        val document: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml.byteInputStream(StandardCharsets.UTF_8))

        val stillingId: String = getString("""//Id/IdValue""", document)
        val leverandor: String = getString("""//SupplierId/IdValue""", document)
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

        return HrXmlStilling(
                stillingId,
                leverandor,
                arbeidsgiver,
                stillingsTittel,
                stillingsBeskrivelse,
                antallStillinger,
                bedriftspresentasjon,
                arbeidssted,
                stillingsProsent,
                kontaktPerson,
                kontaktPersonTlfnr,
                if (publiseresFra.isNullOrBlank()) null else SimpleDateFormat(dateFormat).parse(publiseresFra),
                if (sistePubliseringsdato.isNullOrBlank()) null else SimpleDateFormat(dateFormat).parse(sistePubliseringsdato),
                if (soknadsFrist.isNullOrBlank()) null else SimpleDateFormat(dateFormat).parse(soknadsFrist),
                arbeidsgiverAdresse,
                arbeidsgiverPostNr,
                url,
                mottatt)
    }

    private fun getString(expression: String, document: Document): String {
        return xPath.compile(expression).evaluate(document, XPathConstants.STRING) as String
    }

}