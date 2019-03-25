# pam-xml-stilling

Plukker opp stillinger som er mottatt fra 3.parter på hr-xml via 
xmlstilling-web. Appen transformerer disse og eksponerer dataene på
json for konsum. 

## Kjøre lokalt, IntelliJ

Krever kotlin-plugin for å kjøre i intellij.

Finn filen src/test/kotlin/no/nav/xmlstilling/DevBootstrap.kt, høyreklikk og 
velg Run

Eventuelt klikk på Run > Edit configurations, velg Add new configurations, 
velg Kotlin og sett no.nav.pam.xmlstilling.DevBootstrapKt som main class.

DevBootstrap starter med en in-memorydatabase populert med testdata 
beskrevet i TestDbUtils.kt 