package no.nav.pam.xmlstilling.legacy

import com.zaxxer.hikari.HikariConfig
import mu.KotlinLogging
import no.nav.vault.jdbc.hikaricp.HikariCPVaultUtil
import javax.sql.DataSource

object DatasourceProvider {

    private val log = KotlinLogging.logger { }

    @Volatile private var dataSource: DataSource? = null

    fun get(): DataSource = dataSource!!

    fun init(dataSource: DataSource) {
        this.dataSource ?: synchronized(this) {
            this.dataSource = this.dataSource ?: dataSource
        }
    }

    fun initPostgresDataSource(jdbcUrl: String, mountPath: String, dbName: String) =
            init(navPostgresDataSource(jdbcUrl, mountPath, dbName))

    private fun navPostgresDataSource(jdbcUrl: String, mountPath: String, dbName: String): DataSource {
        log.debug("Initializing database connection pool")

        val config = HikariConfig()
        config.jdbcUrl = jdbcUrl
        config.maximumPoolSize = 2
        config.minimumIdle = 1
        return HikariCPVaultUtil.createHikariDataSourceWithVaultIntegration(
                config,
                mountPath,
                String.format("%s-readonly", dbName))!!
    }

}

