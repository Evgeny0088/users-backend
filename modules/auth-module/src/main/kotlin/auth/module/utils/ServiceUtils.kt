package auth.module.utils

import jakarta.ws.rs.client.Client
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import java.util.concurrent.TimeUnit

object ServiceUtils {

    fun restEasyClient(): Client {
        return ResteasyClientBuilder
            .newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}