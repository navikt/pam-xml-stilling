package no.nav.pam.xmlstilling.rest

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime


data class XmlStillingDto (

    @JsonProperty
    val employer: String?,

    @JsonProperty
    val externalUser: String?, // EKSTERN_BRUKER_REF - Mappes til Medium

    @JsonProperty
    val employerDescription: String?,

    @JsonProperty
    val jobDescription: String?,

    @JsonProperty
    val title: String?,

    @JsonProperty
    val dueDate: LocalDateTime?,

    @JsonProperty
    val externalId: String?,

    @JsonProperty
    val published: LocalDateTime?,

    @JsonProperty
    val expires: LocalDateTime?,

    @JsonProperty
    val received: LocalDateTime
)