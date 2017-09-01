package com.sphereon.libs.blockchain.commons;

import com.sphereon.ms.blockchain.api.model.HasContent;
import com.sphereon.ms.blockchain.api.model.HasValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.*;


/**
 * Created by niels on 18-10-16.
 */
public class Operations {

    private static volatile Operations instance;
    private static Digest digest = Digest.getInstance();

    public static Operations getInstance() {
        /*
        We use double checked locking and a non final instance, since we do not know beforehand whether we operate
        within a Sring context or not.  We also provide configuration support for the Spring singleton scope
        */
        if (instance == null) {
            synchronized (Operations.class) {
                if (instance == null) {
                    Operations.instance = new Operations();
                }
            }
        }
        return instance;
    }

    public Result<byte[]> concat(Result<byte[]> first, Result<byte[]> append) {
        return concat(first.original(), append.original());
    }

    /**
     * Convatenate two byte arrays
     *
     * @param first
     * @param append
     * @return
     */
    public Result<byte[]> concat(byte[] first, byte[] append) {
        byte[] result = Arrays.copyOf(first, first.length + append.length);
        System.arraycopy(append, 0, result, first.length, append.length);
        return new Result<>(result);
    }


    /**
     * Concatenate a byte array with a single byte
     *
     * @param first
     * @param append
     * @return
     */
    public Result<byte[]> concat(byte[] first, byte append) {
        byte[] result = Arrays.copyOf(first, first.length + 1);
        Array.setByte(result, first.length, append);
        return new Result<>(result);
    }

    public Result<byte[]> concat(byte[] first, String append) {
        return concat(first, append.getBytes());
    }

    /**
     * Convert a string to hex string
     *
     * @param hex
     * @return
     */
    public Result<String> stringFromHex(String hex) {
        String string = new String(fromHex(hex).original());
        return new Result<>(string);
    }

    /**
     * Convert a hex string to a byte array
     *
     * @param hex
     * @return
     */
    public Result<byte[]> fromHex(String hex) {
        return new Result(DatatypeConverter.parseHexBinary(hex));
    }

    /**
     * Convert a byte array to hex
     *
     * @param input
     * @return
     */
    public String toHex(byte[] input) {
        return DatatypeConverter.printHexBinary(input).toLowerCase();
    }


    /**
     * Convert a int value (within short range) to byte array
     *
     * @param value
     * @return
     */
    public Result<byte[]> toByteArray(Integer value) {
        if (value == null) {
            return toByteArray((Short) null);
        } else if (value > Short.MAX_VALUE) {
            throw new RuntimeException("Value " + value + " is to big to store in a short, which is the target datatype");
        }
        return toByteArray(value.shortValue());
    }

    /**
     * Convert a short value to byte array
     *
     * @param value
     * @return
     */
    public Result<byte[]> toByteArray(Short value) {
        if (value == null) {
            throw new RuntimeException("Null bytearray short value not allowed");
        }
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(value);
        return new Result<>(buffer.array());
    }

    /**
     * A chain ID base is generated from the external Ids of the chain only using SHA-256 hashing!
     * The content of the first entry is disregarded, since inclusion of the content would lead to the first entry itself
     * This is not the chainId! It has to be hashed using SHA-256 using HEX encoding to retrieve the chainId.
     * Use generateChainId for ons shot implementation
     *
     * @param externalIds A collection of externalId/Metadata values in byte form
     * @return
     */
    public Result<byte[]> calculateChainIdBaseFromBytes(Collection<byte[]> externalIds) {
        byte[] hash = new byte[0];
        if (!CollectionUtils.isEmpty(externalIds)) {
            for (byte[] externalId : externalIds) {
                hash = concat(hash, digest.getSHA256Hash(externalId)).original();
            }
        }
        // This is only the base. It should be hashed using SHA256 again as per spec. Use generateChainId for one shot
        return new Result<>(hash);
    }


    /**
     * A chain ID base is generated from the external Ids of the chain only using SHA-256 hashing!
     * The content of the first entry is disregarded, since inclusion of the content would lead to the Id of the first entry itself
     * This is not the chainId! It has to be hashed using SHA-256 using HEX encoding to retrieve the chainId.
     * Use generateChainId for ons shot implementation
     *
     * @param externalIds A collection of externalId/Metadata values in byte form
     * @return
     */
    public Result<byte[]> calculateChainIdBase(Collection<? extends HasValue<byte[]>> externalIds) {
        List<byte[]> byteList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(externalIds)) {
            for (HasValue<byte[]> externalId : externalIds) {
                byteList.add(externalId.getValue());
            }
        }
        return calculateChainIdBaseFromBytes(byteList);
    }

    /**
     * A chain ID is generated from the external Ids of the chain only using SHA-256 hashing!
     * The content of the first entry is disregarded, since inclusion of the content would lead to the first entry itself
     * This is the chainId used by the blockchain API. It is a SHA-256 hash in HEX form
     *
     * @param externalIds A collection of externalId/Metadata values in byte form
     * @return
     */
    public String generateChainId(Collection<? extends HasValue<byte[]>> externalIds) {
        return calculateChainIdBase(externalIds).stringHash(Digest.Algorithm.SHA_256, Digest.Encoding.HEX);
    }


    /**
     * The first entryId of a chain is calculated from the external Ids and the content of the entry!
     * This is the entryId in SHA-256 hash hex form!
     *
     * @param entryData
     * @param externalIds
     * @return
     */
    public String generateFirstEntryID(HasContent<byte[]> entryData, Collection<? extends HasValue<byte[]>> externalIds) {
        return calculateEntryIdBase(null, entryData, externalIds).stringHash(Digest.Algorithm.SHA_256, Digest.Encoding.HEX);
    }


    /**
     * An entryId is calculated from the chain Id in hex form, the external Ids and the content of the entry!
     * If the chainId in hex form is ommited it will be calculated as if it is the first entry in a chain
     * This is the entryId in SHA-256 hash hex form!
     *
     * @param chainIdHex
     * @param entryData
     * @param externalIds
     * @return
     */
    public String generateEntryID(String chainIdHex, HasContent<byte[]> entryData, Collection<? extends HasValue<byte[]>> externalIds) {
        return calculateEntryIdBase(chainIdHex, entryData, externalIds).stringHash(Digest.Algorithm.SHA_256, Digest.Encoding.HEX);
    }

    /**
     * An entryId base is calculated from the chain Id in hex form, the external Ids and the content of the entry!
     * If the chainId in hex form is ommited it will be calculated as if it is the first entry in a chain
     * This is not the entryId! It has to be hashed using SHA-256 using HEX encoding to retrieve the entryId.
     * Use generateEntryId for one shot implementation
     *
     * @param chainIdHex
     * @param entryData
     * @param externalIds
     * @return
     */

    public Result<byte[]> calculateEntryIdBase(String chainIdHex, HasContent<byte[]> entryData, Collection<? extends HasValue<byte[]>> externalIds) {
        // add entryData has is the whole entryData as a byte string.
        // hash that with sha512
        // append the entrybytes to that 512 hash
        // hash that new byte array with sha256
        byte[] entryBytes = entryToBytes(chainIdHex, entryData, externalIds).original();
        return concat(digest.getSHA512Hash(entryBytes), entryBytes);
    }


    protected Result<byte[]> entryToBytes(HasContent<byte[]> entryData, Collection<? extends HasValue<byte[]>> externalIds) {
        return entryToBytes(null, entryData, externalIds);
    }


    protected Result<byte[]> entryToBytes(String chainIdHex, HasContent<byte[]> entryData, Collection<? extends HasValue<byte[]>> externalIds) {
        byte[] chainID;
        if (!StringUtils.isEmpty(chainIdHex)) {
            chainID = fromHex(chainIdHex).original();
        } else {
            chainID = calculateChainIdBase(externalIds).byteHash(Digest.Algorithm.SHA_256);
        }
        byte[] externalIdBytes = externalIdsToBytes(externalIds).original();
        byte[] bytes = new byte[0];
        //version
        bytes = concat(bytes, (byte) 0).original();
        bytes = concat(bytes, chainID).original();
        bytes = concat(bytes, externalIdBytes).original();
        if (entryData != null && entryData.getContent() != null) {
            bytes = concat(bytes, entryData.getContent()).original();
        }
        return new Result<>(bytes);
    }


    protected Result<byte[]> chainToBytes(HasContent<byte[]> firstEntryData, Collection<? extends HasValue<byte[]>> externalIds) {
        byte[] chainID = calculateChainIdBase(externalIds).byteHash(Digest.Algorithm.SHA_256);
        byte[] entryBytes = entryToBytes(firstEntryData, externalIds).original();
        return concat(chainID, entryBytes);
    }

    protected Result<byte[]> externalIdsToBytes(Collection<? extends HasValue<byte[]>> externalIds) {
        if (externalIds == null || externalIds.isEmpty()) {
            return new Result(new byte[]{(byte) 0});
        }
        byte[] bytes = new byte[0];
        short externalIdLength = 0;
        for (HasValue<byte[]> externalId : externalIds) {
            if (externalId.getValue() == null) {
                throw new NullPointerException("External Id needs a value");
            }
            // We need to add 2 to store the next section's externalID length value
            externalIdLength += (externalId.getValue().length + 2);
        }
        bytes = concat(bytes, toByteArray(externalIdLength).original()).original();

        for (HasValue<byte[]> externalId : externalIds) {
            bytes = concat(bytes, toByteArray(externalId.getValue().length).original()).original();
            bytes = concat(bytes, externalId.getValue()).original();
        }

        return new Result<>(bytes);
    }


    public Result<byte[]> currentTimeMillis() {
        long now = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(now);
        byte[] holder = buffer.array();
        byte[] resp = new byte[]{holder[2], holder[3], holder[4], holder[5], holder[6], holder[7]};
        return new Result<>(resp);
    }


    public class Result<T> implements HasValue<T>, HasContent<T> {
        private final T original;

        private Result(T result) {
            this.original = result;
        }


        private boolean isStringOriginal() {
            return original != null && String.class.isAssignableFrom(original.getClass());
        }

        private boolean isBytesOrignal() {
            // Only strings and bytes for now
            return original != null && !isStringOriginal();
        }


        public T original() {
            return original;
        }

        @Override
        public T getContent() {
            return original();
        }

        @Override
        public T getValue() {
            return original();
        }

        public byte[] byteHash(Digest.Algorithm algorithm) {
            if (isBytesOrignal()) {
                return digest.getHash(algorithm, (byte[]) original);
            } else if (isStringOriginal()) {
                return digest.getHash(algorithm, original.toString());
            }
            throw new RuntimeException("Cannot create hash without input");
        }

        public String stringHash(Digest.Algorithm algorithm, Digest.Encoding encoding) {
            if (isBytesOrignal()) {
                return digest.getHashAsString(algorithm, (byte[]) original, encoding);
            } else if (isStringOriginal()) {
                return digest.getHashAsString(algorithm, original.toString(), encoding);
            }
            throw new RuntimeException("Cannot create hash without input");
        }
    }
}