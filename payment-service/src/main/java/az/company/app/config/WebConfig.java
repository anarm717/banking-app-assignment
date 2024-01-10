package az.company.app.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**

 Configuration class to enable Cross-Origin Resource Sharing (CORS) filter and other necessary configuration

 This allows cross-origin requests from specified origins to access the resources of the server.
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**

     Creates a CorsFilter bean that filters requests based on the allowed origins, headers and methods.

     @return CorsFilter bean
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://85.132.65.34:3002");
        config.addAllowedOrigin("http://localhost:3002");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://localhost");
        config.addAllowedOrigin("http://127.0.0.1");
        config.addAllowedOrigin("http://127.0.0.1:3000");
        
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
