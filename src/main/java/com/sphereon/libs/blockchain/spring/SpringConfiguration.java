package com.sphereon.libs.blockchain.spring;

import com.sphereon.libs.blockchain.commons.Digest;
import com.sphereon.libs.blockchain.commons.Operations;
import com.sphereon.libs.blockchain.commons.RegistrationTypeRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

    @Bean
    public Operations operations() {
        return Operations.getInstance();
    }

    @Bean
    public Digest digest() {
        return Digest.getInstance();
    }

    @Bean
    public RegistrationTypeRegistry registrationTypeRegistry() {
        return RegistrationTypeRegistry.getInstance();
    }
}
