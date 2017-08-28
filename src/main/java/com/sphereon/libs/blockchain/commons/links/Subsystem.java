package com.sphereon.libs.blockchain.commons.links;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface Subsystem {
    String getName();

    List<EntryType> getRegisteredEntryTypes();

    boolean isRegistered(EntryType entryType);

    boolean isRegistered();


    enum Defaults implements Subsystem {
        SHAREPOINT, ALFRESCO, FILE;

        private List<EntryType> entryTypes = new ArrayList<>();
        private boolean registered = false;

        protected Defaults register() {
            this.registered = true;
            return this;
        }

        protected Defaults register(EntryType entryType) {
            if (!entryTypes.contains(entryType)) {
                entryTypes.add(entryType);
            }
            return this;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public List<EntryType> getRegisteredEntryTypes() {
            return Collections.unmodifiableList(entryTypes);
        }

        @Override
        public boolean isRegistered(EntryType entryType) {
            return entryTypes.contains(entryType);
        }

        @Override
        public boolean isRegistered() {
            return registered;
        }
    }
}