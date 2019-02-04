package no.nav.pam.xmlstilling.rest

import java.time.LocalDateTime


data class XmlStillingDto (

    val employer: String?,

    val externalUser: String?, // EKSTERN_BRUKER_REF - Mappes til Medium

    val employerDescription: String?,

    val jobDescription: String?,

    val title: String?,

    val dueDate: LocalDateTime?,

    val externalId: String?,

    val published: LocalDateTime?,

    val expires: LocalDateTime?,

    val received: LocalDateTime
)