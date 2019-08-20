package no.nav.pam.xmlstilling

data class Environment(
        val jdbcUrl: String = getEnvVar("JDBC_URL"),
        val dbName: String = getEnvVar("DB_NAME"),
        val mountPath: String = getEnvVar("VAULT_MOUNT_PATH")
)

fun getEnvVar(varName: String) = System.getenv(varName) ?: throw RuntimeException("Missing required variable \"$varName\"")
