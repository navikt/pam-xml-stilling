package no.nav.pam.xmlstilling.legacy

import java.io.FileInputStream
import java.io.InputStreamReader

private object XmlTestFiles {

    private val PATH = "src/test/resources/xml/example-"
    private var FILE_CONTENT: Map<Leverandor, String>

    init {
        FILE_CONTENT =
            Leverandor.values()
                    .map { it to InputStreamReader(FileInputStream(PATH + it.name.toLowerCase() + ".xml")).readText() }
                    .toMap()
    }

    fun get(leverandor: Leverandor): String {
        return FILE_CONTENT.get(leverandor).orEmpty()
    }
}

enum class Leverandor {
    GLOBESOFT,
    HRMANAGER,
    JOBBNORGE,
    KARRIERENO,
    MYNETWORK,
    STEPSTONE,
    WEBCRUITER;

    fun xml(): String = XmlTestFiles.get(this)
}