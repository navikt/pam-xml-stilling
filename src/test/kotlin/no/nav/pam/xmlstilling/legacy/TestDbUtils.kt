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
        "MOTTATT_DATO" DATETIME,
        "BEHANDLET_DATO" DATETIME,
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

//val h2FetchQuerySql = """
//        select *
//        from "SIX_KOMP"."STILLING_BATCH"
//        where MOTTATT_DATO > ?
//        and MOTTATT_DATO < (
//    	    select trunc(min(MOTTATT_DATO) + 1, 'DD') as NEXT_DAY
//    	    from "SIX_KOMP"."STILLING_BATCH"
//    	    where MOTTATT_DATO > ?)
//        order by STILLING_BATCH_ID;
//        """.trimIndent()

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
        session.run(queryOf(insertStillingBatchSql, 193164, "jobbnorge", Leverandor.JOBBNORGE.xml(), forsteMottattDato, "2018-01-23", "5", "Coop Nordland").asUpdate)
        session.run(queryOf(insertStillingBatchSql, 193165, "webcruiter", Leverandor.WEBCRUITER.xml(), forsteMottattDato.plusDays(1), "2018-01-23", "5", "Evje og Hornnes kommune").asUpdate)
    }
}


val loadExtendedTestData = {
    using(sessionOf(HikariCP.dataSource())) { session ->
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
    using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf(dropStillingBatchSql).asExecute)
    }
}