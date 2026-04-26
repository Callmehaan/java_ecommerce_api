package callmehaan.dev.ecommerce;

import callmehaan.dev.ecommerce.security.CorsConfigurationProperties;
import callmehaan.dev.ecommerce.storage.StorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(CorsConfigurationProperties.class)
public class EcommerceApplication {

    public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

}
