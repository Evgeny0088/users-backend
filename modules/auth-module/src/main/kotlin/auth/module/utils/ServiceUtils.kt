package auth.module.utils

import auth.module.properties.KeycloakProps
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.util.retry.Retry
import reactor.util.retry.RetryBackoffSpec
import reactor.util.retry.RetrySpec
import java.io.InputStream
import java.nio.file.Files
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.time.Duration
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import kotlin.io.path.Path

object ServiceUtils {

    fun setupSSLContext(keycloakProps: KeycloakProps): SSLContext {
        val inputStream: InputStream? =
            if (keycloakProps.mode == "LOCAL") {
                this::class.java.classLoader.getResourceAsStream("keycloak.crt")
            } else {
                Files.newInputStream(Path(keycloakProps.sslPath.plus("tls.crt")))
            }

        inputStream
            .use {
                val certFactory: CertificateFactory = CertificateFactory.getInstance("X.509")
                val cert: Certificate = certFactory.generateCertificate(it)

                val keystore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                keystore.load(null, null)
                val trustFactory: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                keystore.setCertificateEntry("keystore_cert", cert)
                trustFactory.init(keystore)
                val context: SSLContext = SSLContext.getInstance("TLS")
                context.init(null, trustFactory.trustManagers, null)
                return context
            }
    }

    fun retryPolicyWebClient(): RetryBackoffSpec {
        return Retry.backoff(2, Duration.ofSeconds(1)).jitter(0.75)
            .filter { ex-> ex is WebClientResponseException }
            .onRetryExhaustedThrow { _, ex -> throw ex.failure() }
    }
}
