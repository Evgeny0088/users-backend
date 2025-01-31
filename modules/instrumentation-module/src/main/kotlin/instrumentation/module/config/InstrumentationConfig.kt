package instrumentation.module.config

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.TimeSource
import kotlin.time.toJavaDuration

object InstrumentationConfig {

    fun registerTimer(name: String, description: String, registry: MeterRegistry): Timer {
        return Timer.builder(name).description(description).register(registry)
    }

    fun registerGauge(name: String, value: AtomicInteger, registry: MeterRegistry) {
        registry.gauge(name, value)
    }

    inline fun timeMetric(timer: Timer, block: () -> Unit ) {
        val mark = TimeSource.Monotonic.markNow()
        try {
            block.invoke()
        } catch (e: Exception) {
            throw e
        } finally {
            timer.record(mark.elapsedNow().toJavaDuration())
        }
    }
}