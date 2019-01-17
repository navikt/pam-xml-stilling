package no.nav.pam.xmlstilling.legacy

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

// TODO remove this comment
// Insert into EXPORT_TABLE (STILLING_BATCH_ID,EKSTERN_BRUKER_REF,MOTTATT_DATO,BEHANDLET_DATO,BEHANDLET_STATUS,ARBEIDSGIVER)
// values (193192,'stepstone',to_date('23-JAN-18','DD-MON-RR'),null,'5','Hjelp24 Region NordVest');