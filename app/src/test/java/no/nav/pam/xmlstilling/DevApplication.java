package no.nav.pam.xmlstilling;

import no.nav.security.oidc.test.support.spring.TokenGeneratorConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Import(value = TokenGeneratorConfiguration.class)
@SpringBootApplication(scanBasePackageClasses = DevApplication.class)
public class DevApplication {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList(args));
        list.add("--spring.profiles.active=dev");

        SpringApplication.run(DevApplication.class, list.toArray(new String[0]));
    }
}
