package no.nav.pam.xmlstilling.hrxml

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.util.DateUtil.parse
import org.junit.jupiter.api.Test
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.Reader
import java.time.LocalDateTime

class HrXmlStilingParserTest {

    @Test
    fun testParseIncomingHrXml() {
        val reader: Reader = InputStreamReader(FileInputStream("src/test/resources/xml/ok__stillinger_for_ws-innsending_x1.xml"))
        val xml: String = reader.readText()

        val hrXmlStilling: HrXmlStilling = HrXmlStilingParser.parse(xml, LocalDateTime.now())
        assertThat(hrXmlStilling.stillingId).matches("275307656")
        assertThat(hrXmlStilling.leverandor).matches("Webcruiter")
        assertThat(hrXmlStilling.arbeidsgiver).matches("YIT Building Systems AS")
        assertThat(hrXmlStilling.stillingsTittel).matches("Avdelingsleder prosjekt")
        assertThat(hrXmlStilling.stillingsBeskrivelse).matches("Stillingsbeskrivelse")
        assertThat(hrXmlStilling.antallStillinger).isEqualTo(1)
        assertThat(hrXmlStilling.bedriftspresentasjon).matches("YIT er Norges ledende leverandoer av tekniske bygginstallasjoner.")
        assertThat(hrXmlStilling.arbeidssted).matches("Rogaland")
        assertThat(hrXmlStilling.stillingsProsent).isEqualTo(100.0f)
        assertThat(hrXmlStilling.kontaktPerson).matches("Oivin Robberstad")
        assertThat(hrXmlStilling.kontaktPersonTlfnr).matches("90 89 39 04")
        assertThat(hrXmlStilling.publiseresFra).isNull()
        assertThat(hrXmlStilling.sistePubliseringsdato).isNotNull().isEqualTo(parse("2010-11-21"))
        assertThat(hrXmlStilling.soknadsFrist).isNotNull().isEqualTo(parse("2009-02-22"))
        assertThat(hrXmlStilling.arbeidsgiverAdresse).matches("Krokatjoennveien 11c")
        assertThat(hrXmlStilling.arbeidsgiverPostNr).matches("1111")
        assertThat(hrXmlStilling.url).matches("http://www.a.com")
    }

}