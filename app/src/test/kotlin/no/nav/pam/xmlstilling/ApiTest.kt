package no.nav.pam.xmlstilling

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.HttpStatusCode
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
import java.util.concurrent.TimeUnit

class ApiTest {

    init {
        HikariCP.default(testEnvironment.xmlStillingDataSourceUrl, testEnvironment.username, testEnvironment.password)
    }

    val randomPort = ServerSocket(0).use { it.localPort }
    val stillingBatch = StillingBatch(h2FetchQuerySql)
    val application = webApplication(randomPort, stillingBatch, StillingFeed(stillingBatch))
    val client = HttpClient(CIO)
    val gson = GsonBuilder().create()
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
    fun testLoadFirstBatch() {
        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/load/$forsteJan2018/count/5") }
                .also { assertEquals(HttpStatusCode.OK, it.status) }
                .let(mapJsonToEntry)
                .also { list ->
                    assertThat(list.size).isEqualTo(5)
                    assertThat(list.map { entry -> entry.mottattDato }).containsAll(mottattDatoer.subList(0, 4))
                }
    }

    @Test
    fun testLoadNextBatch() {
        val fraTidspunkt = mottattDatoer[4].toString()
        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/load/$fraTidspunkt/count/5") }
                .also { assertEquals(HttpStatusCode.OK, it.status) }
                .let(mapJsonToEntry)
                .also { list ->
                    assertThat(list.size).isEqualTo(5)
                    assertThat(list.map { entry -> entry.mottattDato }).containsAll(mottattDatoer.subList(5, 9))
                }
    }

    @Test
    fun testLastBatch() {
        val fraTidspunkt = mottattDatoer[mottattDatoer.lastIndex - 2].toString()
        println(mottattDatoer.lastIndex)
        println(mottattDatoer.size)
        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/load/$fraTidspunkt/count/5") }
                .also { assertEquals(HttpStatusCode.OK, it.status) }
                .let(mapJsonToEntry)
                .also { list -> assertThat(list.size).isEqualTo(2) }
    }

    @Test
    fun testEmptyBatch() {
        val fraTidspunkt = mottattDatoer.last().toString()
        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/load/$fraTidspunkt/count/5") }
                .also { assertEquals(HttpStatusCode.OK, it.status) }
                .let(mapJsonToEntry)
                .also { list -> assertThat(list.size).isEqualTo(0) }
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
                    assertThat(list.size).isEqualTo(5)
                    assertThat(list.asSequence().all { stilling -> !stilling.externalId.isNullOrEmpty() }).isTrue()
                }
    }

    @AfterEach
    fun after() {
        application.stop(0, 0, TimeUnit.MILLISECONDS)
        dropStillingBatch()
    }

}