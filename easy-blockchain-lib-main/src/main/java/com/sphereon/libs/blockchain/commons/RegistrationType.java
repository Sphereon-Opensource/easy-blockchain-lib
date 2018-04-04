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

import com.sphereon.libs.blockchain.commons.links.Link;
import com.sphereon.libs.blockchain.commons.links.Subsystem;

import java.util.*;

import static com.sphereon.libs.blockchain.commons.RegistrationType.Defaults.CHAIN_LINK_KEY;

public interface RegistrationType {

    String getLabel();

    RegistrationType setLabel(String label);

    String getName();

    byte[] getNameInBytes();

    boolean isRegistered();

    boolean isRegistered(Subsystem subsystem);

    List<Subsystem> getSubsystems();

    RegistrationType register(Subsystem subsystem);

    String createChainLinkKey();

    boolean isChainLink(String key);

    interface Defaults {
        RegistrationType X509_THUMBPRINT = Impl.of("X509Thumbprint").setLabel("X.509 Thumbprint");
        RegistrationType X509_PUBLIC_KEY = Impl.of("X509PublicKey").setLabel("X.509 Public key");
        RegistrationType X509_DISTINGUISHED_NAME = Impl.of("X509DN").setLabel("X.509 DN");
        RegistrationType CRYPTO_KEYS_KEY_ID = Impl.of("CryptoKeysKeyId").setLabel("Crypto Keys KeyId");
        RegistrationType CRYPTO_KEYS_LIST = Impl.of("CryptoKeysList").setLabel("Crypto Keys List");


        RegistrationType CHAIN_TYPE = Impl.of("ChainType").setLabel("Chain type");
        RegistrationType CHAIN_LINK = Impl.of("ChainLink").setLabel("Chain link");
        RegistrationType HASH = Impl.of("Hash").setLabel("File/content hash");
        RegistrationType LIST = Impl.of("List");
        RegistrationType LIST_ITEM = Impl.of("ListItem");
        RegistrationType NODE_ID = Impl.of("NodeId");
        RegistrationType SITE = Impl.of("Site");
        RegistrationType CONTEXT = Impl.of("Context");
        RegistrationType ROOT = Impl.of("Root");
        RegistrationType URL = Impl.of("URL");
        RegistrationType REMARK = Impl.of("Remark");
        RegistrationType CASE_ID = Impl.of("CaseId").setLabel("Case Id");
        RegistrationType DOCUMENT_ID = Impl.of("DocumentId").setLabel("Document Id");
        RegistrationType GENERAL = Impl.of("General");

        RegistrationType X509_THUMBPRINT = Impl.of("X509Thumbprint").setLabel("X.509 Thumbprint");
        RegistrationType X509_PUBLIC_KEY = Impl.of("X509PublicKey").setLabel("X.509 Public key");
        RegistrationType X509_DISTINGUISHED_NAME = Impl.of("X509DN").setLabel("X.509 DN");
        RegistrationType CRYPTO_KEYS_KEY_ID = Impl.of("CryptoKeysKeyId").setLabel("Crypto Keys KeyId");
        RegistrationType CRYPTO_KEYS_LIST = Impl.of("CryptoKeysList").setLabel("Crypto Keys List");

        String CHAIN_LINK_KEY = "ChainLink" + ":";

    }

    class Impl implements RegistrationType {
        private final String name;
        private String label;
        private List<Subsystem> subsystems = new ArrayList<>();

        public static RegistrationType of(String name) {
            return new Impl(stripChainLinkKey(name));
        }

        public static RegistrationType of(byte[] name) {
            return of(new String(name));
        }

        protected Impl(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public byte[] getNameInBytes() {
            return getName() == null ? null : getName().getBytes();
        }

        @Override
        public boolean isRegistered() {
            return RegistrationTypeRegistry.getInstance().get(getName()) != null;
        }

        @Override
        public boolean isRegistered(Subsystem subsystem) {
            return isRegistered() && (subsystem == null || subsystems.contains(subsystem));
        }

        @Override
        public List<Subsystem> getSubsystems() {
            return Collections.unmodifiableList(subsystems);
        }

        @Override
        public RegistrationType register(Subsystem subsystem) {
            RegistrationTypeRegistry.getInstance().add(this, subsystem);
            subsystems.add(subsystem);

            return this;
        }

        public static String stripChainLinkKey(String input) {
            return Utils.String.isEmpty(input) ? input : input.replaceFirst(RegistrationType.Defaults.CHAIN_LINK_KEY, "");
        }

        @Override
        public String createChainLinkKey() {
            return Link.NONE.newBuilder(this).buildLinkKey();
        }

        @Override
        public boolean isChainLink(String key) {
            return from(key).contains(Defaults.CHAIN_LINK) && from(key).contains(this);
        }

        public static Set<RegistrationType> from(String input) {
            Set<RegistrationType> result = new HashSet<>();
            if (Utils.String.isEmpty(input)) {
                return result;
            }
            String key = input.toLowerCase().trim();
            if (key.startsWith(Defaults.CHAIN_LINK.getName())) {
                result.add(Defaults.CHAIN_LINK);
                key = key.replaceFirst(CHAIN_LINK_KEY, "").trim();
                key = key.replaceFirst(Defaults.CHAIN_LINK.getName(), "").trim();

            }
            for (RegistrationType registrationType : RegistrationTypeRegistry.getInstance().getAll()) {
                if (key.contains(registrationType.getName().toLowerCase())) {
                    result.add(registrationType);
                }
            }
            return result;
        }


     /*   public String getLabel(String input) {
            if (isChainLink(input)) {
                return getLabel() + " " + CHAIN_LINK.getLabel();
            }
            return getLabel();
        }*/

        public String getLabel() {
            if (label == null) {
                return getName();
            }
            return label;
        }

        public Impl setLabel(String label) {
            this.label = label;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (!(o instanceof Impl)) { return false; }

            Impl that = (Impl) o;

            if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) { return false; }
            return getLabel() != null ? getLabel().equals(that.getLabel()) : that.getLabel() == null;
        }

        @Override
        public int hashCode() {
            int result = getName() != null ? getName().hashCode() : 0;
            result = 31 * result + (getLabel() != null ? getLabel().hashCode() : 0);
            return result;
        }
    }
}
