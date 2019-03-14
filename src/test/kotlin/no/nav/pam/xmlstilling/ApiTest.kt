package no.nav.pam.xmlstilling

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.HttpStatusCode
import io.prometheus.client.hotspot.DefaultExports
import kotlinx.coroutines.runBlocking
import kotliquery.HikariCP
import no.nav.pam.xmlstilling.legacy.*
import no.nav.pam.xmlstilling.rest.StillingFeed
import no.nav.pam.xmlstilling.rest.XmlStillingDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.ServerSocket
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

class ApiTest {

    init {
        HikariCP.default(testEnvironment.xmlStillingDataSourceUrl, testEnvironment.username, testEnvironment.password)
    }

    val randomPort = ServerSocket(0).use { it.localPort }
    val stillingBatch = StillingBatch()
    val application = webApplication(randomPort, StillingFeed(stillingBatch))
    val client = HttpClient(Apache)
    val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java,
            JsonDeserializer<LocalDateTime> {
                element, _, _ -> LocalDateTime.ofInstant(Instant.parse(element.asString), ZoneId.systemDefault())
            }).create()
    val forsteJan2018 = "2018-01-01T00:00:00"

    val mapJsonToEntry: (HttpResponse) -> List<StillingBatch.Entry> = { response ->
        val turnsType = object : TypeToken<List<StillingBatch.Entry>>() {}.type
        gson.fromJson<List<StillingBatch.Entry>>(runBlocking { response.readText() }, turnsType)
    }

    fun mapJsonToXmlStillingDto(httpResponse: HttpResponse): List<XmlStillingDto> {
        val turnsType = object : TypeToken<List<XmlStillingDto>>() {}.type
        return gson.fromJson<List<XmlStillingDto>>(
                runBlocking { httpResponse.readText() },
                turnsType)
    }

    @BeforeEach
    fun before() {
        createStillingBatchTable()
                .also { loadBasicTestData() }
                .also { loadExtendedTestData() }
        application.start()
    }

    @Test
    fun testIsAlive() {
        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/isAlive") }
                .also { assertEquals(HttpStatusCode.OK, it.status) }
    }

    @Test
    fun testIsReady() {
        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/isReady") }
                .also { assertEquals(HttpStatusCode.OK, it.status) }
    }

    @Test
    fun testPrometheus() {
        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/prometheus") }
                .also { assertEquals(HttpStatusCode.OK, it.status) }
    }


    @Test
    fun testStillingFeed() {
        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/load/2017/01/20/13/30/20") }
                .also { assertEquals(HttpStatusCode.OK, it.status) }
                .let { mapJsonToXmlStillingDto(it) }
                .also { list ->
                    assertThat(list.asSequence().all { stilling -> !stilling.eksternId.isNullOrEmpty() }).isTrue()
                }

    }

    @Test
    fun testFetchMultipleBatches() {
        var nextDateTime = LocalDateTime.of(2017, 1, 20, 13, 30, 20)

        nextDateTime = mapJsonToXmlStillingDto(runBlocking<HttpResponse> { client.get(urlFrom(nextDateTime)) })
                .also { list -> assertThat(list.size).isEqualTo(2) }
                .let { it.last().mottattTidspunkt }

        nextDateTime = mapJsonToXmlStillingDto(runBlocking<HttpResponse> { client.get(urlFrom(nextDateTime)) })
                .also { list ->
                    assertThat(list.size).isEqualTo(3) }
                .let { it.last().mottattTidspunkt }

        nextDateTime = mapJsonToXmlStillingDto(runBlocking<HttpResponse> { client.get(urlFrom(nextDateTime)) })
                .also { list -> assertThat(list.size).isEqualTo(2) }
                .let { it.last().mottattTidspunkt }

        nextDateTime = mapJsonToXmlStillingDto(runBlocking<HttpResponse> { client.get(urlFrom(nextDateTime)) })
                .also { list -> assertThat(list.size).isEqualTo(1) }
                .let { it.last().mottattTidspunkt }

        nextDateTime = mapJsonToXmlStillingDto(runBlocking<HttpResponse> { client.get(urlFrom(nextDateTime)) })
                .also { list -> assertThat(list.size).isEqualTo(6) }
                .let { it.last().mottattTidspunkt }

        mapJsonToXmlStillingDto(runBlocking<HttpResponse> { client.get(urlFrom(nextDateTime)) })
                .also { list -> assertThat(list.size).isEqualTo(0) }
    }

    fun urlFrom(dateTime: LocalDateTime): String {
        return "http://localhost:$randomPort/load/${dateTime.year}/${dateTime.monthValue}/${dateTime.dayOfMonth}/${dateTime.hour}/${dateTime.minute}/${dateTime.second}"
    }

    @AfterEach
    fun after() {
        application.stop(0, 0, TimeUnit.MILLISECONDS)
        dropStillingBatch()
        DefaultExports.initialize()
    }

}