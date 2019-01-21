package no.nav.pam.xmlstilling.legacy

import kotliquery.HikariCP
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using

val createSchema: String = """
    CREATE SCHEMA IF NOT EXISTS SIX_KOMP;
""".trimIndent()

val createStillingBatchTable: String = """
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

val insertStillingBatchEntry: String = """
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

val cleanUpStillingBatch: String = """
    Drop table "SIX_KOMP"."STILLING_BATCH"
""".trimIndent()

val h2FetchQuery = """
    select *
    from "SIX_KOMP"."STILLING_BATCH"
    where STILLING_BATCH_ID > ?
    order by STILLING_BATCH_ID
    limit ?""".trimIndent()

val loadBasicTestData: () -> Unit = {

    using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf(createSchema).asExecute)
        session.run(queryOf(createStillingBatchTable).asExecute)
    }

    using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf(insertStillingBatchEntry, 193164, "jobbnorge", null, "2018-01-23", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193165, "webcruiter", null, "2018-01-23", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
    }
}


val loadExtendedTestData: () -> Unit = {

    using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf(insertStillingBatchEntry, 193166, "jobbnorge", null, "2018-01-23", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193167, "webcruiter", null, "2018-01-23", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193168, "jobbnorge", null, "2018-01-23", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193169, "webcruiter", null, "2018-01-23", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193170, "jobbnorge", null, "2018-01-23", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193171, "webcruiter", null, "2018-01-23", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193172, "jobbnorge", null, "2018-01-23", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193173, "webcruiter", null, "2018-01-23", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193174, "jobbnorge", null, "2018-01-23", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193175, "webcruiter", null, "2018-01-23", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193176, "jobbnorge", null, "2018-01-23", "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchEntry, 193177, "webcruiter", null, "2018-01-23", "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
    }
}

val dropStillingBatch: () -> Unit = {

    using(sessionOf(HikariCP.dataSource())) {session ->
        session.run(queryOf(cleanUpStillingBatch).asExecute)
    }
}