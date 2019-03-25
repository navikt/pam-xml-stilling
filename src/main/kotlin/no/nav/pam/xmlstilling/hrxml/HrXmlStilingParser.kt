package no.nav.pam.xmlstilling.hrxml

import no.nav.pam.xmlstilling.utils.stripEncoding
import org.w3c.dom.Document
import java.nio.charset.StandardCharsets
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

object HrXmlStilingParser {

    private val xPath: XPath = XPathFactory.newInstance().newXPath()

    fun parse(xml: String): Map<HrXmlValue, String> {
        val document: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stripEncoding(xml).byteInputStream(StandardCharsets.UTF_8))

        return HrXmlValue.values()
                .map{ it to xPath.compile(it.xpathExpression).evaluate(document, XPathConstants.STRING) as String }
                .map{ it.first to it.second.trim() }
                .toMap()
    }

    enum class HrXmlValue(val xpathExpression: String) {
        ARBEIDSGIVER("""//EntityName"""),
        ARBEIDSGIVER_BEDRIFTSPRESENTASJON("""//Organization"""),
        ARBEIDSGIVER_ADRESSE("""//PhysicalLocation//AddressLine"""),
        ARBEIDSGIVER_POSTNR("""//PhysicalLocation//PostalCode"""),
        ARBEIDSGIVER_WEBADRESSE("""//InternetWebAddress"""),
        STILLING_ID("""//Id/IdValue"""),
        STILLINGSTITTEL("""//ProfileName"""),
        STILLINGSBESKRIVELSE("""//FormattedPositionDescription/Value"""),
        STILLINGSPROSENT("""//PositionSchedule/@percentage"""),
        ANTALL_STILLINGER("""//NumberToFill"""),
        ARBEIDSSTED("""//PhysicalLocation/Name"""),
        PUBLISERES_FRA("""//DistributionGuidelines/@validFrom"""),
        LEDIG_SNAREST("""//StartAsSoonAsPossible"""),
        LEDIG_FRA("""//StartDate"""),
        LEDIG_TIL("""//ExpectedEndDate"""),
        SISTE_PUBLISERINGSDATO("""//MaximumEndDate"""),
        SOKNADSFRIST("""//MaximumStartDate"""),
        KONTAKTINFO_PERSON("""//FormattedName"""),
        KONTAKTINFO_TELEFON("""//HowToApply//FormattedNumber"""),
        KONTAKTINFO_EPOST("""//HowToApply//InternetEmailAddress""")
    }
}