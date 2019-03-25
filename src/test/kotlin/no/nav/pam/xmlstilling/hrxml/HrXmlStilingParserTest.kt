package no.nav.pam.xmlstilling.hrxml

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.Reader

import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser.HrXmlValue.*

class HrXmlStilingParserTest {

    @Test
    fun testParseIncomingHrXml() {
        val reader: Reader = InputStreamReader(FileInputStream("src/test/resources/xml/ok__stillinger_for_ws-innsending_x1.xml"))
        val xml: String = reader.readText()

        val hrXmlValues: Map<HrXmlStilingParser.HrXmlValue, String> = HrXmlStilingParser.parse(xml)

        assertThat(hrXmlValues.get(STILLING_ID)).matches("275307656")
        assertThat(hrXmlValues.get(ARBEIDSGIVER)).matches("YIT Building Systems AS")
        assertThat(hrXmlValues.get(STILLINGSTITTEL)).matches("Avdelingsleder prosjekt")
        assertThat(hrXmlValues.get(STILLINGSBESKRIVELSE)).matches("Stillingsbeskrivelse")
        assertThat(hrXmlValues.get(ANTALL_STILLINGER)).matches("1")
        assertThat(hrXmlValues.get(ARBEIDSGIVER_BEDRIFTSPRESENTASJON)).matches("YIT er Norges ledende leverandoer av tekniske bygginstallasjoner.")
        assertThat(hrXmlValues.get(ARBEIDSSTED)).matches("Rogaland")
        assertThat(hrXmlValues.get(STILLINGSPROSENT)).matches("100")
        assertThat(hrXmlValues.get(KONTAKTINFO_PERSON)).matches("Oivin Robberstad")
        assertThat(hrXmlValues.get(KONTAKTINFO_TELEFON)).matches("90 89 39 04")
        assertThat(hrXmlValues.get(KONTAKTINFO_EPOST)).matches("a@b.com")
        assertThat(hrXmlValues.get(PUBLISERES_FRA)).isEmpty()
        assertThat(hrXmlValues.get(SISTE_PUBLISERINGSDATO)).matches("2010-11-21")
        assertThat(hrXmlValues.get(LEDIG_SNAREST)).matches("true")
        assertThat(hrXmlValues.get(LEDIG_FRA)).matches("2008-01-01")
        assertThat(hrXmlValues.get(LEDIG_TIL)).matches("2011-12-12")
        assertThat(hrXmlValues.get(SOKNADSFRIST)).matches("2009-02-22")
        assertThat(hrXmlValues.get(ARBEIDSGIVER_ADRESSE)).matches("Krokatjoennveien 11c")
        assertThat(hrXmlValues.get(ARBEIDSGIVER_POSTNR)).matches("1111")
        assertThat(hrXmlValues.get(ARBEIDSGIVER_WEBADRESSE)).matches("http://www.a.com")
    }

    @Test
    fun testParseNonHrXml() {
        val xml = "<html><foo>bar</foo></html>"
        val hrXmlValues: Map<HrXmlStilingParser.HrXmlValue, String> = HrXmlStilingParser.parse(xml)
        assertThat(hrXmlValues.values.asSequence().all { value -> value.isEmpty() }).isTrue()
    }

    @Test
    fun testParseIncomingHrXmlWithEncodingSpecifiedAsIso8859_1() {
        val reader: Reader = InputStreamReader(FileInputStream("src/test/resources/xml/example-jobbnorge.xml"))
        val xml: String = reader.readText()
        val hrXmlValues: Map<HrXmlStilingParser.HrXmlValue, String> = HrXmlStilingParser.parse(xml)
        assertThat(hrXmlValues.get(ARBEIDSGIVER_BEDRIFTSPRESENTASJON)).contains("Gildeskål")
    }

    @Test
    fun testParseIncomingHrXmlWithEncodingSpecifiedAsUtf_8() {
        val reader: Reader = InputStreamReader(FileInputStream("src/test/resources/xml/example-stepstone.xml"))
        val xml: String = reader.readText()
        val hrXmlValues: Map<HrXmlStilingParser.HrXmlValue, String> = HrXmlStilingParser.parse(xml)
        assertThat(hrXmlValues.get(ARBEIDSGIVER_BEDRIFTSPRESENTASJON)).contains("nærmiljø")
    }

    @Test
    fun testParseIncomingHrXmlWithoutEncodingSpecified() {
        val reader: Reader = InputStreamReader(FileInputStream("src/test/resources/xml/example-globesoft.xml"))
        val xml: String = reader.readText()
        val hrXmlValues: Map<HrXmlStilingParser.HrXmlValue, String> = HrXmlStilingParser.parse(xml)
        assertThat(hrXmlValues.get(STILLINGSTITTEL)).contains("samfunnsøkonom")
    }

    @Test
    fun testParseIncomingHrXmlMedPubliseresFra() {
        val reader: Reader = InputStreamReader(FileInputStream("src/test/resources/xml/example-webcruiter.xml"))
        val xml: String = reader.readText()
        val hrXmlValues: Map<HrXmlStilingParser.HrXmlValue, String> = HrXmlStilingParser.parse(xml)
        assertThat(hrXmlValues.get(PUBLISERES_FRA)).matches("2018-11-29")
    }

}