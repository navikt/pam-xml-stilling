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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.ServerSocket
import java.util.concurrent.TimeUnit

class ApiTest {

    val randomPort = ServerSocket(0).use { it.localPort }
    val application = webApplication(randomPort, StillingBatch(h2FetchQuery))
    val client = HttpClient(CIO)
    val gson = GsonBuilder().create()

    @BeforeEach
    fun before() {
        HikariCP.default(testEnvironment.xmlStillingDataSourceUrl, testEnvironment.username, testEnvironment.password)
        loadBasicTestData()
        loadExtendedTestData()

        application.start()
    }

    @Test
    fun testLoad() {
        val response = runBlocking<HttpResponse> {
            client.get("http://localhost:$randomPort/load/0/count/5")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val turnsType = object : TypeToken<List<StillingBatch>>() {}.type
        val stillingBatcher = gson.fromJson<List<StillingBatch>>(
                runBlocking { response.readText() }, turnsType)

        assertThat(stillingBatcher.size).isEqualTo(5)
    }

    @Test
    fun testIsAlive() {
        val response = runBlocking<HttpResponse> {
            client.get("http://localhost:$randomPort/isAlive")
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testIsReady() {
        val response = runBlocking<HttpResponse> {
            client.get("http://localhost:$randomPort/isReady")
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testPrometheus() {
        val response = runBlocking<HttpResponse> {
            client.get("http://localhost:$randomPort/prometheus")
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @AfterEach
    fun after() {
        application.stop(0, 0, TimeUnit.MILLISECONDS)
        dropStillingBatch()
    }

}