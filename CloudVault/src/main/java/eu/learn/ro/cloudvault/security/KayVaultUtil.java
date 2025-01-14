package eu.learn.ro.cloudvault.security;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class KayVaultUtil {
    private final SecretClient secretClient;

    public KayVaultUtil() {
        this.secretClient = new SecretClientBuilder()
                .vaultUrl("https://cloudvaultkeys.vault.azure.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }

    public String getSecret(String secretName) {
        return secretClient.getSecret(secretName).getValue();
    }
}
