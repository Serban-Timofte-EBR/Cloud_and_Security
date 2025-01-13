package eu.learn.ro.cloudvault.security;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfiguration {

    @Bean
    public RateLimiter rateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(1)) // Wait up to 1 second before throwing a RateLimiter exception
                .limitRefreshPeriod(Duration.ofSeconds(10)) // Refresh the rate limit every 10 seconds
                .limitForPeriod(5) // Allow 5 calls per refresh period
                .build();

        return RateLimiter.of("defaultRateLimiter", config);
    }
}