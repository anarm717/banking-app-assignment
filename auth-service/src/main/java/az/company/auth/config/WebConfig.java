package az.company.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://85.132.65.34:3002");
        config.addAllowedOrigin("http://localhost:3002");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://localhost");
        config.addAllowedOrigin("https://apfqrup.com");
        config.addAllowedOrigin("https://apfqrup.com/backend");
        config.addAllowedOrigin("https://apfqrup.com/auth");
        config.addAllowedOrigin("http://apfqrup.com");
        config.addAllowedOrigin("http://127.0.0.1");
        config.addAllowedOrigin("http://127.0.0.1:3000");
        config.addAllowedOrigin("http://185.161.226.58:3000");
        config.addAllowedOrigin("http://146.190.216.225:443");
        config.addAllowedOrigin("http://146.190.216.225:8080");
        config.addAllowedOrigin("http://146.190.216.225:8081");
        config.addAllowedOrigin("http://146.190.216.225:8082");
        config.addAllowedOrigin("http://146.190.216.225:8083");
        config.addAllowedOrigin("https://146.190.216.225");
        config.addAllowedOrigin("http://146.190.216.225:3000");


        config.addAllowedOrigin("http://localhost:3000");

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addAllowedMethod("OPTIONS");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
