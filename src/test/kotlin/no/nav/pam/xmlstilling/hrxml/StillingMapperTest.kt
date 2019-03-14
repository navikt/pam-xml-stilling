package no.nav.pam.xmlstilling.hrxml

import org.assertj.core.api.Assertions.assertThat

import no.nav.pam.xmlstilling.legacy.StillingBatch
import org.junit.jupiter.api.Test
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.Reader
import java.time.LocalDate
import java.time.LocalDateTime

class StillingMapperTest {

    private val dateAndTime = "2019-01-22T20:30:00"

    @Test
    fun testToStillingDtos() {
        val reader: Reader = InputStreamReader(FileInputStream("src/test/resources/xml/ok__stillinger_for_ws-innsending_x1.xml"))
        val xml: String = reader.readText()
        val time = LocalDateTime.parse(dateAndTime)

        val stillingBatchEntries: MutableList<StillingBatch.Entry> = ArrayList()
        stillingBatchEntries.add(StillingBatch.Entry(
                100,
                "jobbnorge",
                xml,
                LocalDateTime.now(),
                LocalDate.now(),
                "5",
                "NAV"
        ))
        stillingBatchEntries.add(StillingBatch.Entry(
                200,
                "webcruiter",
                xml,
                time,
                LocalDate.now(),
                "7",
                "FINN"
        ))
        stillingBatchEntries.add(StillingBatch.Entry(
                300,
                "webcruiter",
                "<html></html>",
                time,
                LocalDate.now(),
                "0",
                "VY"
        ))

        //NumberToFill

        val stillingDtos = StillingMapper.toStillingDtos(stillingBatchEntries)

        assertThat(stillingDtos.size).isEqualTo(3)
        assertThat(stillingDtos[0].stillingstittel).matches("Avdelingsleder prosjekt")
        assertThat(stillingDtos[1].mottattTidspunkt).isEqualTo(dateAndTime)
        assertThat(stillingDtos[0].eksternBrukerRef).isEqualTo("jobbnorge")
        assertThat(stillingDtos[1].eksternBrukerRef).isEqualTo("webcruiter")
        assertThat(stillingDtos[2].antallStillinger).isNull()
    }
}
