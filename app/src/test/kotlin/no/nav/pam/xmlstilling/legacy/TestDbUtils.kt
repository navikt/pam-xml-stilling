package no.nav.pam.xmlstilling.legacy

import kotliquery.HikariCP
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using
import java.time.LocalDateTime

val createSchemaSql: String = """
    CREATE SCHEMA IF NOT EXISTS SIX_KOMP;
""".trimIndent()

val createStillingBatchTableSql: String = """
    CREATE TABLE "SIX_KOMP"."STILLING_BATCH"
    (	"STILLING_BATCH_ID" NUMBER NOT NULL,
        "EKSTERN_BRUKER_REF" VARCHAR2(150 BYTE),
        "STILLING_XML" CLOB,
        "MOTTATT_DATO" DATE,
        "BEHANDLET_DATO" DATE,
        "BEHANDLET_STATUS" VARCHAR2(3 BYTE),
        "ARBEIDSGIVER" VARCHAR2(150 BYTE),
        CONSTRAINT "PK_STILLING_BATCH" PRIMARY KEY ("STILLING_BATCH_ID"));
""".trimIndent()

val insertStillingBatchSql: String = """
    Insert into "SIX_KOMP"."STILLING_BATCH" (
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
    Drop table "SIX_KOMP"."STILLING_BATCH"
""".trimIndent()

val h2FetchQuerySql = """
    select *
    from "SIX_KOMP"."STILLING_BATCH"
    where MOTTATT_DATO > ?
    order by STILLING_BATCH_ID
    limit ?""".trimIndent()

val createStillingBatchTable = {
    using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf(createSchemaSql).asExecute)
        session.run(queryOf(createStillingBatchTableSql).asExecute)
    }
}

val forsteMottattDato = LocalDateTime.of(2018, 1, 23, 0, 0 ,0)
val mottattDatoer = (0L..13).map { i ->  forsteMottattDato.plusDays(i) }

val loadBasicTestData = {
    using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf(insertStillingBatchSql, 193164, "jobbnorge", Leverandor.JOBBNORGE.xml(), "2018-01-23", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193165, "webcruiter", Leverandor.WEBCRUITER.xml(), "2018-01-24", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
    }
}


val loadExtendedTestData = {
    using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf(insertStillingBatchSql, 193166, "jobbnorge", Leverandor.MYNETWORK.xml(), "2018-01-25", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193167, "webcruiter", Leverandor.HRMANAGER.xml(), "2018-01-26", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193168, "jobbnorge", Leverandor.STEPSTONE.xml(), "2018-01-27", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193169, "webcruiter", Leverandor.KARRIERENO.xml(), "2018-01-28", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193170, "jobbnorge", Leverandor.JOBBNORGE.xml(), "2018-01-29", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193171, "webcruiter", Leverandor.GLOBESOFT.xml(), "2018-01-30", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193172, "jobbnorge", Leverandor.GLOBESOFT.xml(), "2018-01-31", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193173, "webcruiter", Leverandor.GLOBESOFT.xml(), "2018-02-01", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193174, "jobbnorge", Leverandor.GLOBESOFT.xml(), "2018-02-02", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193175, "webcruiter", Leverandor.GLOBESOFT.xml(), "2018-02-03", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193176, "jobbnorge", Leverandor.GLOBESOFT.xml(), "2018-02-04", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193177, "webcruiter", Leverandor.GLOBESOFT.xml(), "2018-02-05", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
    }
}

val dropStillingBatch = {
    using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf(dropStillingBatchSql).asExecute)
    }
}