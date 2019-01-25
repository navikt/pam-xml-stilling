package no.nav.pam.xmlstilling.hrxml

import org.assertj.core.api.Assertions.assertThat

import no.nav.pam.xmlstilling.legacy.StillingBatch
import org.junit.jupiter.api.Test
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.Reader
import java.time.LocalDate

class StillingMapperTest {

    @Test
    fun testToStillingDtos() {
        val reader: Reader = InputStreamReader(FileInputStream("src/test/resources/xml/ok__stillinger_for_ws-innsending_x1.xml"))
        val xml: String = reader.readText()

        val stillingBatchEntries: MutableList<StillingBatch.Entry> = ArrayList()
        stillingBatchEntries.add(StillingBatch.Entry(
                100,
null,
                xml,
                LocalDate.now(),
                LocalDate.now(),
                "5",
                "NAV"
        ))
        stillingBatchEntries.add(StillingBatch.Entry(
                200,
                null,
                xml,
                LocalDate.now(),
                LocalDate.now(),
                "7",
                "FINN"
        ))

        val stillingDtos = StillingMapper.toStillingDtos(stillingBatchEntries)

        assertThat(stillingDtos[0].title).matches("Avdelingsleder prosjekt")

    }
}

/*
        val stillingBatchId: Int,
        val eksternBrukerRef: String?,
        val stillingXml: String,
        val mottattDato: java.time.LocalDate?,
        val behandletDato: java.time.LocalDate?,
        val behandletStatus: String?,
        val arbeidsgiver: String?)
 */