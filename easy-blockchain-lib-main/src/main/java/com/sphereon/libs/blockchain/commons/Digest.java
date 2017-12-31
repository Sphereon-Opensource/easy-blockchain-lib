/*
 * Copyright (c) 2017 Sphereon B.V. <https://sphereon.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sphereon.libs.blockchain.commons;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by niels on 3-7-2017.
 */
public class Digest {

    private static final int BUFFER_SIZE_8K = 8192;

    private static volatile Digest instance;

    public enum Encoding {
        UTF_8, HEX
    }

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
            if (Utils.String.isNotEmpty(value) && value.contains("512")) {
                return SHA_512;
            }
            return SHA_256;
        }
    }


    private Digest() {
    }


    public static Digest getInstance() {
        /*
        We use double checked locking and a non final instance, since we do not know beforehand whether we operate
        within a Spring context or not.  We also provide configuration support for the Spring singleton scope in the spring module
        */
        if (instance == null) {
            synchronized (Digest.class) {
                if (instance == null) {
                    Digest.instance = new Digest();
                }
            }
        }
        return instance;
    }


    public byte[] getHash(Algorithm algorithm, String input) {
        if (Utils.String.isEmpty(input)) {
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


    public byte[] getHash(Algorithm algorithm, InputStream inputStream) {
        try {
            byte[] buffer = new byte[BUFFER_SIZE_8K];
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm.getImplementation());
            try (DigestInputStream dis = new DigestInputStream(inputStream, messageDigest)) {
                while (dis.read(buffer) != -1)
                    ;
            }
            return messageDigest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }



    public byte[] getHashAsHex(Algorithm algorithm, byte[] input) {
        return getHashAsString(algorithm, input, Encoding.HEX).getBytes();
    }


    public byte[] getHashAsHex(Algorithm algorithm, InputStream input) {
        return getHashAsString(algorithm, input, Encoding.HEX).getBytes();
    }


    public byte[] getSHA256Hash(byte[] base) {
        return getHash(Algorithm.SHA_256, base);
    }


    public byte[] getSHA512Hash(byte[] base) {
        return getHash(Algorithm.SHA_512, base);
    }


    public String getHashAsString(Algorithm algorithm, String input, Encoding encoding) {
        if (Utils.String.isEmpty(input)) {
            return "";
        }
        return getHashAsString(algorithm, input.getBytes(), encoding);

    }


    public String getHashAsString(Algorithm algorithm, byte[] input, Encoding encoding) {
        byte[] hash = getHash(algorithm, input);
        if (encoding == Encoding.UTF_8) {
            return new String(hash, Charset.forName("UTF-8"));
        } else {
            return Utils.Hex.encodeAsString(hash);
        }
    }

    public String getHashAsString(Algorithm algorithm, InputStream input, Encoding encoding) {
        byte[] hash = getHash(algorithm, input);
        if (encoding == Encoding.UTF_8) {
            return new String(hash, Charset.forName("UTF-8"));
        } else {
            return Utils.Hex.encodeAsString(hash);
        }
    }

}
