package no.nav.pam.xmlstilling.utils

import java.util.regex.Pattern

fun stripEncoding(s: String): String {
    val pattern = Pattern.compile("\\sencoding=\"[^\"]+\"", Pattern.MULTILINE or Pattern.CASE_INSENSITIVE)
    return pattern.matcher(s).replaceAll("")
}
