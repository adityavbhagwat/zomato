package com.zomato.authservice.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtKeyConfig {

    @Value("${jwt.keystore.location}")
    private String keystoreLocation;

    @Value("${jwt.keystore.password}")
    private String keystorePassword;

    @Value("${jwt.key.alias}")
    private String keyAlias;

    @Value("${jwt.key.password}")
    private String keyPassword;

    @Bean
    public KeyStore keyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(keystoreLocation);
            keyStore.load(resourceAsStream, keystorePassword.toCharArray());
            return keyStore;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load keystore", e);
        }
    }

    @Bean
    public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
        try {
            return (RSAPrivateKey) keyStore.getKey(keyAlias, keyPassword.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key", e);
        }
    }

    @Bean
    public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
        try {
            return (RSAPublicKey) keyStore.getCertificate(keyAlias).getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    @Bean
    public JWKSet jwkSet(RSAPublicKey publicKey) {
        RSAKey.Builder builder = new RSAKey.Builder(publicKey)
                .keyID("zomato-jwt-key-id");
        return new JWKSet(builder.build());
    }
}