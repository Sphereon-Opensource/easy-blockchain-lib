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

package com.sphereon.libs.blockchain.commons.links;

import com.sphereon.libs.blockchain.commons.Digest;
import com.sphereon.libs.blockchain.commons.Operations;
import com.sphereon.libs.blockchain.api.HasContent;
import com.sphereon.libs.blockchain.api.HasValue;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TestOperations {

    public static final String CHAIN_ID = "502b99271ff6a3f8509ba2764e1e1c1482ad800140c17b25e165837ab5320501";
    public static final String CHAIN_FIRST_EXTERNAL_ID = "first external id";
    public static final String CHAIN_SECOND_EXTERNAL_ID = "second external id";

    public static final String ENTRY_ID = "3d945a53b2e0ec5bc4464ae83955e818b6cb5906c93a1eaf80291bd1e241653c";
    public static final String ENTRY_CONTENT = "Test Entry Content";
    public static final String ENTRY_FIRST_EXTERNAL_ID = "first entry external id";
    public static final String ENTRY_SECOND_EXTERNAL_ID = "second entry external id";

    private Operations operations = Operations.getInstance();

    @Test
    public void testChainId() {
        Collection<Data> externalIds = new Data(CHAIN_FIRST_EXTERNAL_ID).collect(CHAIN_SECOND_EXTERNAL_ID);
        Assert.assertEquals(CHAIN_ID, operations.calculateChainIdBase(externalIds).stringHash(Digest.Algorithm.SHA_256, Digest.Encoding.HEX));
        Assert.assertEquals(CHAIN_ID, operations.generateChainId(externalIds));
        Assert.assertEquals("322b18e1f267202565bf0d1ee03865f076fd22357ff6dc46c7a6dfef454f3871", operations.generateFirstEntryId(null, externalIds));
    }

    @Test
    public void testEntryId() {
        Collection<Data> externalIds = new Data(ENTRY_FIRST_EXTERNAL_ID).collect(ENTRY_SECOND_EXTERNAL_ID);
        Data content = new Data(ENTRY_CONTENT);
        Assert.assertEquals(ENTRY_ID, operations.calculateEntryIdBase(CHAIN_ID, content, externalIds).stringHash(Digest.Algorithm.SHA_256, Digest.Encoding.HEX));
        Assert.assertEquals(ENTRY_ID, operations.generateEntryID(CHAIN_ID, content, externalIds));
    }


    class Data implements HasContent<byte[]>, HasValue<byte[]> {

        private byte[] data;

        public Data(String data) {
            this.data = data == null ? new byte[]{} : data.getBytes();
        }

        public Data(byte[] data) {
            this.data = data;
        }

        @Override
        public byte[] getContent() {
            return data;
        }

        @Override
        public byte[] getValue() {
            return data;
        }

        public Collection<Data> collect() {
            return Collections.singletonList(this);
        }

        public Collection<Data> collect(String... data) {
            if (data == null || data.length == 0) {
                return collect();
            }

            Data[] converted = new Data[data.length];
            int i = 0;
            for (String val : data) {
                converted[i] = new Data(val);
            }
            return collect(converted);
        }

        public Collection<Data> collect(Data... data) {
            if (data == null || data.length == 0) {
                return collect();
            }
            List<Data> collected = new ArrayList<>();
            collected.add(this);
            for (Data datum : data) {
                collected.add(datum);
            }
            return collected;
        }

    }

}
