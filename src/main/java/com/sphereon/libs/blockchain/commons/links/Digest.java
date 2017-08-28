package com.sphereon.libs.blockchain.commons.links;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by niels on 3-7-2017.
 */
@Component
public class Digest {
    public enum Algorithm {
        SHA_256("SHA-256"), SHA_512("SHA-512");
        private final String algorithm;

        Algorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getImplementation() {
            return algorithm;
        }

        public static Algorithm from(String value) {
            if (!StringUtils.isEmpty(value) && value.contains("512")) {
                return SHA_512;
            }
            return SHA_256;
        }
    }

    public enum Encoding {
        UTF_8, HEX;
    }

    public byte[] getHash(Algorithm algorithm, String input) {
        if (StringUtils.isEmpty(input)) {
            return new byte[]{};
        }
        return getHash(algorithm, input.getBytes());
    }

    public byte[] getHash(Algorithm algorithm, byte[] input) {
        try {
            return MessageDigest.getInstance(algorithm.getImplementation()).digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public byte[] getSHA256Hash(byte[] base) {
        return getHash(Algorithm.SHA_256, base);
    }

    public byte[] getSHA512Hash(byte[] base) {
        return getHash(Algorithm.SHA_512, base);
    }

    public String getHashAsString(Algorithm algorithm, String input, Encoding encoding) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        return getHashAsString(algorithm, input.getBytes(), encoding);

    }

    public String getHashAsString(Algorithm algorithm, byte[] input, Encoding encoding) {
        byte[] hash = getHash(algorithm, input);
        if (encoding == Encoding.UTF_8) {
            return new String(hash, Charset.forName("UTF-8"));
        } else {
            return Hex.encodeHexString(hash);
        }
    }

}
