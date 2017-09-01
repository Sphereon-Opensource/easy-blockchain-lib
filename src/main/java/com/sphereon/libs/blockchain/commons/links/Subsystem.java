package com.sphereon.libs.blockchain.commons.links;

import com.sphereon.libs.blockchain.commons.RegistrationType;
import com.sphereon.libs.blockchain.commons.RegistrationTypeRegistry;

import java.util.ArrayList;
import java.util.List;

public interface Subsystem {
    String getName();

    Impl registerDefaultRegistrations();

    List<RegistrationType> getRegisteredEntryTypes();

    boolean isRegistered(RegistrationType registrationType);

    boolean isRegistered();


    interface Default {
        public static final Subsystem ALFRESCO = Impl.of("Alfreco").defaults(RegistrationType.Defaults.CHAIN_LINK, RegistrationType.Defaults.HASH, RegistrationType.Defaults.NODE_ID, RegistrationType.Defaults.SITE);
        public static final Subsystem SHAREPOINT = Impl.of("Sharepoint").defaults(RegistrationType.Defaults.CHAIN_LINK, RegistrationType.Defaults.HASH, RegistrationType.Defaults.LIST, RegistrationType.Defaults.LIST_ITEM, RegistrationType.Defaults.SITE);
        public static final Subsystem FILE = Impl.of("File").defaults(RegistrationType.Defaults.CHAIN_LINK, RegistrationType.Defaults.HASH, RegistrationType.Defaults.DOCUMENT_ID, RegistrationType.Defaults.CONTEXT, RegistrationType.Defaults.REMARK, RegistrationType.Defaults.ROOT);
        public static final Subsystem CUSTOM = Impl.of("Custom").defaults(RegistrationType.Defaults.CHAIN_LINK, RegistrationType.Defaults.ROOT, RegistrationType.Defaults.CONTEXT, RegistrationType.Defaults.GENERAL, RegistrationType.Defaults.URL);
    }

    class Impl implements Subsystem {
        private final String name;
        private List<RegistrationType> registrationTypes = new ArrayList<>();
        private RegistrationType[] defaults = new RegistrationType[]{};

        public static Impl of(String name) {
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
        public Impl registerDefaultRegistrations() {
            for (RegistrationType registrationType : defaults) {
                register(registrationType);
            }
            return this;
        }

        public Impl register(RegistrationType registrationType) {
            RegistrationTypeRegistry.getInstance().add(registrationType, this);
            if (!registrationTypes.contains(registrationType)) {
                registrationTypes.add(registrationType);
            }
            registrationType.register(this);
            return this;
        }

        protected Impl defaults(RegistrationType... defaults) {
            this.defaults = defaults;
            return this;
        }

        @Override
        public List<RegistrationType> getRegisteredEntryTypes() {
            return registrationTypes;
        }

        @Override
        public boolean isRegistered(RegistrationType registrationType) {
            return getRegisteredEntryTypes().contains(registrationType);
        }

        @Override
        public boolean isRegistered() {
            return !getRegisteredEntryTypes().isEmpty();
        }
    }
}