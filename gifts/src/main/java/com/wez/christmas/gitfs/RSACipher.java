package com.wez.christmas.gitfs;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

class RSACipher {

    private static final String RSA = "RSA";

    private final KeyFactory keyFactory;

    @Inject
    @SneakyThrows
    public RSACipher() {
        this.keyFactory = KeyFactory.getInstance(RSA);
    }

    @SneakyThrows
    public String encrypt(String plainText, String publicKeyBase64) {
        PublicKey publicKey = convertToPublicKey(publicKeyBase64);
        Cipher encryptCipher = Cipher.getInstance(RSA);
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(cipherText);
    }

    @VisibleForTesting
    @SneakyThrows
    Pair<String, String> generateKeyPair() {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
        generator.initialize(1024, new SecureRandom());
        KeyPair keyPair = generator.generateKeyPair();
        return Pair.of(convertToString(keyPair.getPublic()), convertToString(keyPair.getPrivate()));
    }

    @SneakyThrows
    private PublicKey convertToPublicKey(String publicKeyBase64) {
        byte[] publicBytes = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        return keyFactory.generatePublic(keySpec);
    }

    @SneakyThrows
    private PrivateKey convertToPrivateKey(String publicKeyBase64) {
        byte[] publicBytes = Base64.getDecoder().decode(publicKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(publicBytes);
        return keyFactory.generatePrivate(keySpec);
    }

    @SneakyThrows
    private String convertToString(PublicKey publicKey) {
        X509EncodedKeySpec spec = keyFactory.getKeySpec(publicKey, X509EncodedKeySpec.class);
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

    @SneakyThrows
    private String convertToString(PrivateKey privateKey) {
        PKCS8EncodedKeySpec spec = keyFactory.getKeySpec(privateKey, PKCS8EncodedKeySpec.class);
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

}
