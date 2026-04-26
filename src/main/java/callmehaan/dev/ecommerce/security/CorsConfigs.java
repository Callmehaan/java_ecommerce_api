package callmehaan.dev.ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfigs {
    private final CorsConfigurationProperties corsConfigurationProperties;

    public CorsConfigs(CorsConfigurationProperties corsConfigurationProperties) {
        this.corsConfigurationProperties = corsConfigurationProperties;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(corsConfigurationProperties.getAllowedOrigins());
        config.setAllowedMethods(corsConfigurationProperties.getAllowedMethods());
        config.setAllowedHeaders(corsConfigurationProperties.getAllowedHeaders());
        config.setExposedHeaders(corsConfigurationProperties.getExposedHeaders());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return source;
    }
}
