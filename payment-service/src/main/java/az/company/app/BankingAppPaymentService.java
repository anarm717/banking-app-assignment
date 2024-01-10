package az.company.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@OpenAPIDefinition(info = @Info(title = "Banking app project", version = "1.0", description = "Banking app Project"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer")
@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
public class BankingAppPaymentService {

	public static void main(String[] args) {
		SpringApplication.run(BankingAppPaymentService.class, args);
	}

}
