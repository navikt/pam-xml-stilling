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

    val mapJsonToEntry: (HttpResponse) -> List<StillingBatch.Entry> = { response ->
        val turnsType = object : TypeToken<List<StillingBatch.Entry>>() {}.type
        gson.fromJson<List<StillingBatch.Entry>>(runBlocking { response.readText() }, turnsType)
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
        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/load/0/count/5") }
                .also { assertEquals(HttpStatusCode.OK, it.status) }
                .let(mapJsonToEntry)
                .also { list ->
                    assertThat(list.size).isEqualTo(5)
                    assertThat(list.map { entry -> entry.stillingBatchId }).containsAll(listOf(193164, 193165, 193166, 193167, 193168))
                }
    }

    @Test
    fun testLoadNextBatch() {

        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/load/193168/count/5") }
                .also { assertEquals(HttpStatusCode.OK, it.status) }
                .let(mapJsonToEntry)
                .also { list ->
                    assertThat(list.size).isEqualTo(5)
                    assertThat(list.map { entry -> entry.stillingBatchId }).containsAll(listOf(193169, 193170, 193171, 193172, 193173))
                }

    }

    @Test
    fun testLastBatch() {

        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/load/193175/count/5") }
                .also { assertEquals(HttpStatusCode.OK, it.status) }
                .let(mapJsonToEntry)
                .also { list -> assertThat(list.size).isEqualTo(2) }

    }

    @Test
    fun testEmptyBatch() {

        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/load/99999999/count/5") }
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
        runBlocking<HttpResponse> { client.get("http://localhost:$randomPort/load/2019/01/20/13/30/20") }
                .also { assertEquals(HttpStatusCode.OK, it.status) }
    }

    @AfterEach
    fun after() {
        application.stop(0, 0, TimeUnit.MILLISECONDS)
        dropStillingBatch()
    }

}