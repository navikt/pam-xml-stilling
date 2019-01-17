package no.nav.pam.xmlstilling.legacy

import kotliquery.HikariCP
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import java.time.LocalDate
class RepositoryTest {

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

            session.run(queryOf(createSchema).asExecute)

            session.run(queryOf(createStillingBatchTable).asExecute)

        }
    }

    @Test
    fun testFetchAll() {

        using(sessionOf(HikariCP.dataSource())) {session ->
            session.run(queryOf(insertStillingBatchEntry, 193164, "jobbnorge", null, "2018-01-23", "2018-01-23", "5", "Coop Nordland").asUpdate)
            session.run(queryOf(insertStillingBatchEntry, 193165, "webcruiter", null, "2018-01-23", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        }

        assertThat(Repository().fetchBatch().size).isEqualTo(2)
    }

    @Test
    fun testMapping() {

        using(sessionOf(HikariCP.dataSource())) {session ->
            session.run(queryOf(insertStillingBatchEntry, 193164, "jobbnorge", """<?xml version="1.0" encoding="UTF-8" standalone="no" ?>""","2018-01-23", "2018-01-23", "5", "Coop Nordland").asUpdate)
        }

        val entry = Repository().fetchBatch().first()

        assertThat(entry.stillingBatchId).isEqualTo(193164)
        assertThat(entry.eksternBrukerRef).isEqualTo("jobbnorge")
        assertThat(entry.stillingXml).isEqualTo("""<?xml version="1.0" encoding="UTF-8" standalone="no" ?>""")
        assertThat(entry.mottattDato).isEqualTo(LocalDate.of(2018, 1, 23))
        assertThat(entry.behandletDato).isEqualTo(LocalDate.of(2018, 1, 23))
        assertThat(entry.behandletStatus).isEqualTo("5")
        assertThat(entry.arbeidsgiver).isEqualTo("Coop Nordland")
    }

    @Test
    fun testNullMappings() {

        using(sessionOf(HikariCP.dataSource())) {session ->
            session.run(queryOf(insertStillingBatchEntry, 193164, null, null, null, null, null, null).asUpdate)
        }

        val entry = Repository().fetchBatch().first()

        assertThat(entry.stillingBatchId).isEqualTo(193164)
        assertThat(entry.eksternBrukerRef).isNull()
        assertThat(entry.stillingXml).isNull()
        assertThat(entry.mottattDato).isNull()
        assertThat(entry.behandletDato).isNull()
        assertThat(entry.behandletStatus).isNull()
        assertThat(entry.arbeidsgiver).isNull()
    }


    @AfterEach
    fun cleanup() {
        dropStillingBatch()
    }
}

