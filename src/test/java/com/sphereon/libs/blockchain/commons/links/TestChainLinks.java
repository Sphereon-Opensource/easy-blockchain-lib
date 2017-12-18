/*
 *
 * Copyright 2017 Sphereon B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.sphereon.libs.blockchain.commons.links;

import com.sphereon.libs.blockchain.commons.RegistrationType;
import com.sphereon.libs.blockchain.commons.RegistrationTypeRegistry;
import com.sphereon.libs.blockchain.spring.SpringConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfiguration.class})
public class TestChainLinks {

    @Autowired
    private RegistrationTypeRegistry registry;

    @Test
    public void testAlfrescoRegistration() {
        registry.initSubsystem(Subsystem.Default.ALFRESCO);
        Assert.assertTrue(registry.contains(RegistrationType.Defaults.CHAIN_LINK, Subsystem.Default.ALFRESCO));

        Assert.assertTrue(registry.contains(RegistrationType.Defaults.SITE, Subsystem.Default.ALFRESCO));
        Assert.assertTrue(registry.contains(RegistrationType.Defaults.NODE_ID, Subsystem.Default.ALFRESCO));
        Assert.assertTrue(registry.contains(RegistrationType.Defaults.HASH, Subsystem.Default.ALFRESCO));

        Assert.assertFalse(registry.contains(RegistrationType.Defaults.LIST_ITEM, Subsystem.Default.ALFRESCO));
        Assert.assertFalse(registry.contains(RegistrationType.Defaults.NODE_ID, Subsystem.Default.CUSTOM));

        Assert.assertTrue(RegistrationType.Defaults.NODE_ID.isChainLink("ChainLink:NodeId"));
        Assert.assertFalse(RegistrationType.Defaults.NODE_ID.isChainLink("ChainLink:Site"));
        Assert.assertTrue(RegistrationType.Defaults.HASH.isChainLink(RegistrationType.Defaults.HASH.createChainLinkKey()));

        Assert.assertEquals("ChainLink:NodeId", Link.EXTERNAL_ID.newBuilder(RegistrationType.Defaults.NODE_ID).buildLinkKey());

        Assert.assertEquals(RegistrationType.Defaults.NODE_ID, registry.get(RegistrationType.Defaults.CHAIN_LINK_KEY + RegistrationType.Defaults.NODE_ID.getName()));

    }


}
