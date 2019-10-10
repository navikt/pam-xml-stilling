package no.nav.pam.xmlstilling.legacy

import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser
import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser.HrXmlValue.ARBEIDSGIVER
import no.nav.pam.xmlstilling.hrxml.HrXmlStilingParser.HrXmlValue.STILLING_ID
import java.time.LocalDateTime

val createSchemaSql: String = """
    CREATE SCHEMA IF NOT EXISTS SIX_KOMP;
""".trimIndent()

val createStillingBatchTableSql: String = """
    CREATE TABLE STILLING_BATCH
    (	"STILLING_BATCH_ID" NUMERIC(19, 1),
        "EKSTERN_BRUKER_REF" VARCHAR(150),
	    "STILLING_XML" TEXT,
	    "MOTTATT_DATO" DATE,
	    "BEHANDLET_DATO" DATE,
	    "BEHANDLET_STATUS" VARCHAR(3),
	    "ARBEIDSGIVER" VARCHAR(150),
	    CONSTRAINT PK_ID PRIMARY KEY (STILLING_BATCH_ID));
""".trimIndent()

val insertStillingBatchSql: String = """
    Insert into STILLING_BATCH (
        STILLING_BATCH_ID,
        EKSTERN_BRUKER_REF,
        STILLING_XML,
        MOTTATT_DATO,
        BEHANDLET_DATO,
        BEHANDLET_STATUS,
        ARBEIDSGIVER)
    values (?, ?, ?, ?, ?, ?, ?);
""".trimIndent()


val dropStillingBatchSql: String = """
    Drop table STILLING_BATCH
""".trimIndent()

val createStillingBatchTable = {
    using(sessionOf(DatasourceProvider.get())) { session ->
        session.run(queryOf(createSchemaSql).asExecute)
        session.run(queryOf(createStillingBatchTableSql).asExecute)
    }
}

val forsteMottattDato = LocalDateTime.of(2018, 1, 23, 0, 1 ,0)

val loadBasicTestData = {
    using(sessionOf(DatasourceProvider.get())) { session ->
        session.run(queryOf(insertStillingBatchSql, 193164, "jobbnorge", Leverandor.JOBBNORGE.xml(), forsteMottattDato, "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193165, "webcruiter", Leverandor.WEBCRUITER.xml(), forsteMottattDato.plusDays(1), "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
    }
}

val loadExtendedTestData = {
    using(sessionOf(DatasourceProvider.get())) { session ->
        session.run(queryOf(insertStillingBatchSql, 193166, "jobbnorge",  Leverandor.MYNETWORK.xml(),  forsteMottattDato.plusSeconds(4), "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193167, "webcruiter", Leverandor.HRMANAGER.xml(),  forsteMottattDato.plusDays(1), "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193168, "jobbnorge",  Leverandor.STEPSTONE.xml(),  forsteMottattDato.plusDays(1).plusMinutes(40), "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193169, "webcruiter", Leverandor.KARRIERENO.xml(), forsteMottattDato.plusDays(3), "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193170, "jobbnorge",  Leverandor.JOBBNORGE.xml(),  forsteMottattDato.plusDays(3), "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193171, "webcruiter", Leverandor.GLOBESOFT.xml(),  forsteMottattDato.plusDays(4), "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193172, "jobbnorge",  Leverandor.GLOBESOFT.xml(),  forsteMottattDato.plusDays(5).plusMinutes(40), "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193173, "webcruiter", Leverandor.GLOBESOFT.xml(),  forsteMottattDato.plusDays(5).plusMinutes(40), "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193174, "jobbnorge",  Leverandor.GLOBESOFT.xml(),  forsteMottattDato.plusDays(5), "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193175, "webcruiter", Leverandor.GLOBESOFT.xml(),  forsteMottattDato.plusDays(5).plusMinutes(40), "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193176, "jobbnorge",  Leverandor.GLOBESOFT.xml(),  forsteMottattDato.plusDays(5).plusMinutes(40), "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193177, "webcruiter", Leverandor.GLOBESOFT.xml(),  forsteMottattDato.plusDays(5).plusMinutes(40), "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
    }
}

val dropStillingBatch = {
    using(sessionOf(DatasourceProvider.get())) { session ->
        session.run(queryOf(dropStillingBatchSql).asExecute)
    }
}

