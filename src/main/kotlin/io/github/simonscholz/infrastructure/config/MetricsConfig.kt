package io.github.simonscholz.infrastructure.config

import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import javax.enterprise.inject.Produces
import javax.inject.Singleton

@Singleton
class MetricsConfig {

    @Produces
    @Singleton
    fun enableHistogramForServerRequests() = object : MeterFilter {
        override fun configure(id: Meter.Id, config: DistributionStatisticConfig): DistributionStatisticConfig? {
            return if (id.name.startsWith("http.server.requests")) {
                DistributionStatisticConfig.builder()
                    .percentiles(0.25, 0.5, 0.9, 0.95, 0.99)
                    .percentilesHistogram(true)
                    .build()
                    .merge(config)
            } else {
                config
            }
        }
    }

    /**
     * Used for this dashboard: https://grafana.com/grafana/dashboards/4701-jvm-micrometer/
     */
    @Produces
    @Singleton
    fun commonTags(): MeterFilter =
        MeterFilter.commonTags(
            listOf(
                Tag.of("application", "quarkus-performance-analysis"),
            ),
        )
}
