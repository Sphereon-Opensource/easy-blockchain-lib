package com.sphereon.libs.blockchain.commons.links;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestLinkBuilder {
    @Test
    public void buildNoneLink() {
        Link noneLink = Link.NONE;
        Link.Builder builder = noneLink.newBuilder().none("TEST123");
        Assert.assertEquals("TEST123",  builder.build());
        Assert.assertEquals(1, builder.getParts().size());
        Assert.assertEquals(noneLink, builder.getParts().firstKey());
    }

    @Test
    public void buildExternalIdLink() {
        Link externalIdLink = Link.EXTERNAL_ID;
        String externalIdTemplate = externalIdLink.getTemplate();
        Link.Builder builder = externalIdLink.newBuilder().context("context").chainId("chain1").entryId("entry1").externalId(0);
        Assert.assertEquals("/context/chains/chain1/entries/entry1/externalids/0",  builder.build());
        Assert.assertEquals(4, builder.getParts().size());
        Assert.assertEquals(Link.CONTEXT, builder.getParts().firstKey());
    }
}
