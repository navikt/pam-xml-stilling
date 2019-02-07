package no.nav.pam.xmlstilling

data class Environment(
        val xmlStillingDataSourceUrl: String = getEnvVar("AETATNOXMLSTILLINGADMINDS_URL"),
        val username: String = getEnvVar("AETATNOXMLSTILLINGADMINDS_USERNAME"),
        val password: String = getEnvVar("AETATNOXMLSTILLINGADMINDS_PASSWORD")

)

fun getEnvVar(varName: String, defaultValue: String? = null) =
        System.getenv(varName) ?: defaultValue ?: throw RuntimeException("Missing required variable \"$varName\"")
