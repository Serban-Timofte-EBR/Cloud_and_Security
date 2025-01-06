package eu.learn.ro.cloudvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"eu.learn.ro.cloudvault", "eu.learn.ro.cloudvault.security"})
public class CloudVaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudVaultApplication.class, args);
    }

}
