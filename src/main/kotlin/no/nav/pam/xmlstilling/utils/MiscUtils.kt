package no.nav.pam.xmlstilling.utils

import java.util.regex.Pattern

fun stripIso88591Encoding(s: String): String {
    val pattern = Pattern.compile("\\sencoding=\"iso-8859-1\"", Pattern.MULTILINE or Pattern.CASE_INSENSITIVE)
    return pattern.matcher(s).replaceAll("")
}
