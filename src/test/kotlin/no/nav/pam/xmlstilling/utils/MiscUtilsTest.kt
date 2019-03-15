package no.nav.pam.xmlstilling.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MiscUtilsTest {

    @Test
    fun testStripEncoding() {
        assertThat(stripEncoding("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>")).isEqualTo("<?xml version=\"1.0\"?>")
        assertThat(stripEncoding("<?xml version=\"1.0\" EncoDING=\"isO-8859-1\"?><b a=\"c\">")).isEqualTo("<?xml version=\"1.0\"?><b a=\"c\">")
        assertThat(stripEncoding("<?xml version=\"1.0\" encoding=\"utf-8\"?>")).isEqualTo("<?xml version=\"1.0\"?>")
        assertThat(stripEncoding("<?xml version=\"1.0\" encoding=\"Windows-1252\"?>")).isEqualTo("<?xml version=\"1.0\"?>")
        assertThat(stripEncoding("\n\n<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n\n<foo>bar</foo>"))
                .isEqualTo("\n\n<?xml version=\"1.0\"?>\n\n<foo>bar</foo>")
    }
}