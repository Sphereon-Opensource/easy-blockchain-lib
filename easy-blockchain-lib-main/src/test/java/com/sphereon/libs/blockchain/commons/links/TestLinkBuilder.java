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

import com.sphereon.libs.blockchain.commons.RegistrationType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.SortedMap;

@RunWith(JUnit4.class)
public class TestLinkBuilder {
    @Test
    public void buildNoneLink() {
        Link noneLink = Link.NONE;
        Link.Builder builder = noneLink.newBuilder(RegistrationType.Defaults.CHAIN_LINK).none("TEST123");
        Assert.assertEquals("TEST123", builder.buildTargetLink());
        Assert.assertEquals(1, builder.getParts().size());
        Assert.assertEquals(RegistrationType.Defaults.CHAIN_LINK.getName(), builder.buildLinkKey());
        Assert.assertEquals(noneLink, builder.getParts().firstKey());
        Assert.assertEquals("ChainLink", builder.buildLinkKey());
    }

    @Test
    public void buildExternalIdLink() {
        Link externalIdLink = Link.EXTERNAL_ID;
        String externalIdTemplate = externalIdLink.getTemplate();
        Link.Builder builder = externalIdLink
                .newBuilder(RegistrationType.Defaults.HASH)
                .context("context")
                .chainId("chain1")
                .entryId("entry1").externalId(0);
        Assert.assertEquals("/context/chains/chain1/entries/entry1/externalids/0", builder.buildTargetLink());
        Assert.assertEquals(4, builder.getParts().size());
        Assert.assertEquals(Link.CONTEXT, builder.getParts().firstKey());
        Assert.assertEquals("context", builder.getParts().get(Link.CONTEXT));
        Assert.assertEquals(Link.EXTERNAL_ID, builder.getParts().lastKey());
        Assert.assertEquals("0", builder.getParts().get(Link.EXTERNAL_ID));
        Assert.assertEquals("ChainLink:Hash", builder.buildLinkKey());
    }

    @Test
    public void parseLinks() {
        final String link = "/context/chains/chain1/entries/entry1/externalids/0";
        Assert.assertEquals(Link.EXTERNAL_ID, Link.parser().targetLinkType(link));
        SortedMap<Link, String> parts = Link.parser().targetLinkParts(link);
        Assert.assertEquals(4, parts.size());

        Assert.assertEquals(Link.EXTERNAL_ID, parts.lastKey());
        Assert.assertEquals("context", parts.get(Link.CONTEXT));
        Assert.assertEquals("chain1", parts.get(Link.CHAIN_ID));
        Assert.assertEquals("entry1", parts.get(Link.ENTRY_ID));
        Assert.assertEquals("0", parts.get(Link.EXTERNAL_ID));

        // Recheck building link again from parsed parts
        Link.Builder builder = Link.EXTERNAL_ID.newBuilder(RegistrationType.Defaults.GENERAL).add(parts);
        Assert.assertEquals(link, builder.buildTargetLink());


        Assert.assertFalse(Link.parser().isLinkKey(null));
        Assert.assertFalse(Link.parser().isLinkKey(""));

        // Needs ChainLink: prefix
        Assert.assertFalse(Link.parser().isLinkKey(RegistrationType.Defaults.CASE_ID.getName()));
        Assert.assertFalse(Link.parser().isLinkKey(RegistrationType.Defaults.CHAIN_LINK.getName()));
        Assert.assertTrue(Link.parser().isLinkKey(RegistrationType.Defaults.CHAIN_LINK_KEY));
        Assert.assertTrue(Link.parser().isLinkKey(RegistrationType.Defaults.CHAIN_LINK_KEY + RegistrationType.Defaults.CASE_ID.getName()));

        Assert.assertEquals("CaseId", Link.parser().linkKeyValue(RegistrationType.Defaults.CHAIN_LINK_KEY + RegistrationType.Defaults.CASE_ID.getName()));
        Assert.assertEquals("Test", Link.parser().linkKeyValue(RegistrationType.Defaults.CHAIN_LINK_KEY + "Test"));
        Assert.assertNull(Link.parser().linkKeyValue("Test"));
        Assert.assertEquals("Test", Link.parser().linkKeyValueOrInput("Test"));




    }
}
