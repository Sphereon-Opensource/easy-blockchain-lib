package com.sphereon.libs.blockchain.commons;

import com.sphereon.libs.blockchain.commons.links.Link;
import com.sphereon.libs.blockchain.commons.links.Subsystem;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public interface RegistrationType {
    String getName();

    boolean isRegistered();

    boolean isRegistered(Subsystem subsystem);

    List<Subsystem> getSubsystems();

    RegistrationType register(Subsystem subsystem);

    String createChainLinkKey();

    boolean isChainLink(String key);

    interface Defaults {
        RegistrationType CHAIN_LINK = Impl.of("ChainLink");
        RegistrationType HASH = Impl.of("Hash");
        RegistrationType LIST = Impl.of("List");
        RegistrationType LIST_ITEM = Impl.of("ListItem");
        RegistrationType NODE_ID = Impl.of("NodeId");
        RegistrationType SITE = Impl.of("Site");
        RegistrationType CONTEXT = Impl.of("Context");
        RegistrationType ROOT = Impl.of("Root");
        RegistrationType URL = Impl.of("URL");
        RegistrationType REMARK = Impl.of("Remark");
        RegistrationType CASE_ID = Impl.of("CaseId");
        RegistrationType DOCUMENT_ID = Impl.of("DocumentId");
        RegistrationType GENERAL = Impl.of("General");

    }

    class Impl implements RegistrationType {
        private final String name;
        private List<Subsystem> subsystems = new ArrayList<>();

        public static RegistrationType of(String name) {
            return new Impl(name);
        }

        protected Impl(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
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
            if (StringUtils.isEmpty(input)) {
                return result;
            }
            String key = input.toLowerCase().trim();
            if (key.startsWith(Defaults.CHAIN_LINK.getName())) {
                result.add(Defaults.CHAIN_LINK);
                key = key.replaceFirst(Defaults.CHAIN_LINK.getName() + ":", "").trim();
                key = key.replaceFirst(Defaults.CHAIN_LINK.getName(), "").trim();

            }
            for (RegistrationType registrationType : RegistrationTypeRegistry.getInstance().getAll()) {
                if (key.contains(registrationType.getName().toLowerCase())) {
                    result.add(registrationType);
                }
            }
            return result;
        }
    }
}
