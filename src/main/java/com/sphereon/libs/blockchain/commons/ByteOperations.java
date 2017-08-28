package com.sphereon.libs.blockchain.commons;

import com.sphereon.libs.blockchain.commons.links.Digest;
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
public class ByteOperations {

    private static final ByteOperations INSTANCE = new ByteOperations();

    private ByteOperations() {
    }

    public ByteOperations getInstance() {
        return INSTANCE;
    }


    private static Digest digest = new Digest();

    public byte[] concat(byte[] first, byte[] append) {
        byte[] result = Arrays.copyOf(first, first.length + append.length);
        System.arraycopy(append, 0, result, first.length, append.length);
        return result;
    }

    public byte[] concat(byte[] first, byte append) {
        byte[] result = Arrays.copyOf(first, first.length + 1);
        Array.setByte(result, first.length, append);
        return result;
    }

    public byte[] concat(byte[] first, String append) {
        return concat(first, append.getBytes());
    }

    public String stringFromHex(String hex) {
        return new String(fromHex(hex));
    }

    public byte[] fromHex(String hex) {
        return DatatypeConverter.parseHexBinary(hex);
    }

    public String toHex(byte[] input) {
        return DatatypeConverter.printHexBinary(input).toLowerCase();
    }


    public byte[] toByteArray(Integer value) {
        if (value == null) {
            return toByteArray((Short) null);
        } else if (value != null && value > Short.MAX_VALUE) {
            throw new RuntimeException("Value " + value + " is to big to store in a short, which is the target datatype");
        }
        return toByteArray(value == null ? null : value.shortValue());
    }

    public byte[] toByteArray(Short value) {
        if (value == null) {
            throw new RuntimeException("Null bytearray short value not allowed");
        }
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(value);
        return buffer.array();
    }

    public byte[] generateChainIDFromBytes(Collection<byte[]> externalIds) {
        byte[] hash = new byte[0];
        if (!CollectionUtils.isEmpty(externalIds)) {
            for (byte[] externalId : externalIds) {
                hash = concat(hash, digest.getSHA256Hash(externalId));
            }
        }
        // Hash result once again as per spec
        return digest.getSHA256Hash(hash);
    }

    public byte[] generateChainID(Collection<HasValue<byte[]>> externalIds) {
        List<byte[]> byteList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(externalIds)) {
            for (HasValue<byte[]> externalId : externalIds) {
                byteList.add(externalId.getValue());
            }
        }
        return generateChainIDFromBytes(byteList);
    }

    public byte[] entryToBytes(HasContent<byte[]> entryData, Collection<HasValue<byte[]>> externalIds) {
        return entryToBytes(null, entryData, externalIds);
    }

    public byte[] entryToBytes(String chainIdHex, HasContent<byte[]> entryData, Collection<HasValue<byte[]>> externalIds) {
        byte[] chainID = null;
        if (!StringUtils.isEmpty(chainIdHex)) {
            chainID = fromHex(chainIdHex);
        } else {
            chainID = generateChainIDFromBytes(Collections.singletonList(entryData.getContent()));
        }
        byte[] externalIdBytes = externalIdsToBytes(externalIds);
        //version
        byte[] bytes = new byte[0];
        bytes = concat(bytes, (byte) 0);

        bytes = concat(bytes, chainID);
        bytes = concat(bytes, externalIdBytes);
        if (entryData.getContent() != null) {
            bytes = concat(bytes, entryData.getContent());
        }
        return bytes;
    }

    public byte[] entryToHash(HasContent<byte[]> entryData, Collection<HasValue<byte[]>> externalIds) {
        return entryToHash(null, entryData, externalIds);
    }

    public byte[] entryToHash(String chainIdHex, HasContent<byte[]> entryData, Collection<HasValue<byte[]>> externalIds) {
        // add entryData has is the whole entryData as a byte string.
        // hash that with sha512
        // append the entrybytes to that 512 hash
        // hash that new byte array with sha256
        byte[] entryBytes = entryToBytes(chainIdHex, entryData, externalIds);
        return digest.getSHA256Hash((concat(digest.getSHA512Hash(entryBytes), entryBytes)));
    }

    public String entryToHexHash(String chainIDHex, HasContent<byte[]> entryData, Collection<HasValue<byte[]>> externalIds) {
        return toHex(entryToHash(chainIDHex, entryData, externalIds));
    }

    public byte[] chainToBytes(HasContent<byte[]> firstEntryData, Collection<HasValue<byte[]>> externalIds) {
        byte[] chainID = generateChainIDFromBytes(Collections.singletonList(firstEntryData.getContent()));
        byte[] entryBytes = entryToBytes(firstEntryData, externalIds);
        return concat(chainID, entryBytes);
    }

    protected byte[] externalIdsToBytes(Collection<HasValue<byte[]>> externalIds) {
        if (externalIds == null || externalIds.isEmpty()) {
            return new byte[]{(byte) 0};
        }
        byte[] bytes = new byte[0];
        short externalIdLength = 0;
        for (HasValue<byte[]> externalId : externalIds) {
            if (externalId.getValue() == null) {
                throw new NullPointerException();
            }
            // We need to add 2 to store the next section's externalID length value
            externalIdLength += (externalId.getValue().length + 2);
        }
        bytes = concat(bytes, toByteArray(externalIdLength));

        for (HasValue<byte[]> externalId : externalIds) {
            bytes = concat(bytes, toByteArray(externalId.getValue().length));
            bytes = concat(bytes, externalId.getValue());
        }

        return bytes;
    }


    public byte[] currentTimeMillis() {
        long now = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(now);
        byte[] holder = buffer.array();
        byte[] resp = new byte[]{holder[2], holder[3], holder[4], holder[5], holder[6], holder[7]};
        return resp;
    }


}
