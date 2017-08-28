package com.sphereon.libs.blockchain.commons.links;

import java.util.ArrayList;
import java.util.List;

public interface EntryType {
    String getName();

    List<Link> getLinks();

    boolean isLinkType(Link link);

    boolean isRegistered();

    boolean isRegisted(Subsystem subsystem);

    List<Subsystem> getSubsystems();

    enum Defaults implements EntryType {
        HASH("Hash", new Link[]{Link.CHAIN_ID, Link.ENTRY_ID}),
        SP_LIST_ITEM_ID("ListItemId", new Link[] {Link.CHAIN_ID});

        private final String name;
        private boolean isLinkType;
        private boolean registered = false;
        private List<Link> links = new ArrayList<>();
        private List<Subsystem> subsystems = new ArrayList<>();

        Defaults(String name, Link[] targets) {
            this.name = name;
            this.isLinkType = isLinkType;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public List<Link> getLinks() {
            return null;
        }

        @Override
        public boolean isLinkType(Link link) {
            return false;
        }

        @Override
        public boolean isRegistered() {
            return false;
        }

        @Override
        public boolean isRegisted(Subsystem subsystem) {
            return false;
        }

        @Override
        public List<Subsystem> getSubsystems() {
            return null;
        }

        protected Defaults register() {
            this.registered = true;
            return this;
        }


    }


}
