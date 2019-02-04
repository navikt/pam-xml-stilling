package no.nav.pam.xmlstilling.legacy

import kotliquery.HikariCP
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class RepositoryTest {

    private val forsteJan2018 = LocalDateTime.of(2018, 1,1, 0 , 0, 0)

    private val minimalXML = """<?xml version="1.0" encoding="UTF-8" standalone="no" ?>"""

    companion object {
        @BeforeAll
        @JvmStatic
        fun setupMemDb() {
            HikariCP.default("jdbc:h2:mem:test", "user", "pass")
        }
    }

    @BeforeEach
    fun setup() {
        using(sessionOf(HikariCP.dataSource())) { session ->
            session.run(queryOf(createSchemaSql).asExecute)
            session.run(queryOf(createStillingBatchTableSql).asExecute)
        }
    }

    @Test
    fun testFetchAll() {
        using(sessionOf(HikariCP.dataSource())) {session ->
            session.run(queryOf(insertStillingBatchSql, 193164, "jobbnorge", minimalXML, "2017-11-05", "2018-01-23", "5", "Coop Nordland").asUpdate)
            session.run(queryOf(insertStillingBatchSql, 193165, "webcruiter", minimalXML, "2018-01-23", "2018-10-23", "5", "Evje og Hornnes kommune").asUpdate)
        }

        assertThat(StillingBatch(h2FetchQuerySql).fetchBatch(
                forsteJan2018,
                10).size)
                .isEqualTo(1)
    }

    @Test
    fun testMapping() {
        using(sessionOf(HikariCP.dataSource())) {session ->
            session.run(queryOf(insertStillingBatchSql, 193164, "jobbnorge", minimalXML, "2018-01-23", "2018-01-23", "5", "Coop Nordland").asUpdate)
        }

        val entry = StillingBatch(h2FetchQuerySql).fetchBatch(forsteJan2018, 10).first()

        assertThat(entry.stillingBatchId).isEqualTo(193164)
        assertThat(entry.eksternBrukerRef).isEqualTo("jobbnorge")
        assertThat(entry.stillingXml).isEqualTo(minimalXML)
        assertThat(entry.mottattDato).isEqualTo(LocalDateTime.of(2018, 1, 23, 0, 0, 0))
        assertThat(entry.behandletDato).isEqualTo(LocalDate.of(2018, 1, 23))
        assertThat(entry.behandletStatus).isEqualTo("5")
        assertThat(entry.arbeidsgiver).isEqualTo("Coop Nordland")
    }

    @Test
    fun testNullMappingsOfNullables() {
        using(sessionOf(HikariCP.dataSource())) {session ->
            session.run(queryOf(insertStillingBatchSql, 193164, "karriereno", minimalXML, "2018-01-23", null, null, null).asUpdate)
        }

        val entry = StillingBatch(h2FetchQuerySql).fetchBatch(forsteJan2018, 10).first()

        assertThat(entry.stillingBatchId).isEqualTo(193164)
        assertThat(entry.behandletDato).isNull()
        assertThat(entry.behandletStatus).isNull()
        assertThat(entry.arbeidsgiver).isNull()
    }


    @AfterEach
    fun cleanup() {
        dropStillingBatch()
    }
}

