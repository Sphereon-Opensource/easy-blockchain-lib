package com.sphereon.libs.blockchain.commons.links;

import com.sphereon.libs.blockchain.commons.RegistrationType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
        Link.Builder builder = externalIdLink.newBuilder(RegistrationType.Defaults.HASH).context("context").chainId("chain1").entryId("entry1").externalId(0);
        Assert.assertEquals("/context/chains/chain1/entries/entry1/externalids/0", builder.buildTargetLink());
        Assert.assertEquals(4, builder.getParts().size());
        Assert.assertEquals(Link.CONTEXT, builder.getParts().firstKey());
        Assert.assertEquals("context", builder.getParts().get(Link.CONTEXT));
        Assert.assertEquals(Link.EXTERNAL_ID, builder.getParts().lastKey());
        Assert.assertEquals("0", builder.getParts().get(Link.EXTERNAL_ID));
        Assert.assertEquals("ChainLink:Hash", builder.buildLinkKey());
    }
}
