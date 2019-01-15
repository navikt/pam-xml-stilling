package no.nav.pam.xmlstilling;

import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = Application.class)
@EnableOIDCTokenValidation(ignore="org.springframework")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
