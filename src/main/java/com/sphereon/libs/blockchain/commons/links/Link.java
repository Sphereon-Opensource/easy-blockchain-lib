package com.sphereon.libs.blockchain.commons.links;

import com.sphereon.libs.blockchain.commons.RegistrationType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

public enum Link {
    NONE(BlockhainType.NONE, "%s"),
    CONTEXT(BlockhainType.CONTEXT, "/%s"),
    CHAIN_ID(BlockhainType.CHAIN, "/%s/chains/%s", CONTEXT),
    ENTRY_ID(BlockhainType.ENTRY, "/%s/chains/%s/entries/%s", CHAIN_ID),
    EXTERNAL_ID(BlockhainType.EXTERNAL_ID, "/%s/chains/%s/entries/%s/externalids/%s", ENTRY_ID),
    CONTENT(BlockhainType.CONTENT, "/%s/chains/%s/entries/%s/content", ENTRY_ID);

    private final Link parent;
    private final BlockhainType type;
    private List<Link> children = new ArrayList<>();

    private String template;

    Link(BlockhainType type, String template) {
        this.type = type;
        this.template = template;
        this.parent = null;
    }

    Link(BlockhainType type, String template, Link parent) {
        this.type = type;
        this.template = template;
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
    }

    protected String getTemplate() {
        return template;
    }

    public BlockhainType getType() {
        return type;
    }

    public Link getParent() {
        return parent;
    }

    public boolean hasParent() {
        return getParent() != null;
    }

    public List<Link> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public boolean hasChildren() {
        return !CollectionUtils.isEmpty(children);
    }

    public Builder newBuilder(RegistrationType type) {
        return new Builder(this, type);
    }

    public class Builder {
        private SortedMap<Link, String> parts;
        private final RegistrationType type;
        private final Link link;

        private Builder(Link link, RegistrationType type) {
            reset();
            this.link = link;
            this.type = type;
        }

        public Builder reset() {
            this.parts = new TreeMap<>();
            return this;
        }

        public Builder none(String value) {
            add(NONE, value);
            return this;
        }

        public Builder context(String value) {
            add(CONTEXT, value);
            return this;
        }

        public Builder chainId(String value) {
            add(CHAIN_ID, value);
            return this;
        }

        public Builder entryId(String value) {
            add(ENTRY_ID, value);
            return this;
        }

        public Builder externalId(int index) {
            add(EXTERNAL_ID, "" + index);
            return this;
        }

        public Builder content() {
            add(CONTENT, "content");
            return this;
        }


        public Builder add(Link link, String value) {
            if (link == EXTERNAL_ID && !StringUtils.isNumeric(value)) {
                throw new RuntimeException("External ID link has to be identified with a zero based number index instead of : " + value);
            } else if (link == CONTENT && !(StringUtils.isEmpty(value) || "content".equalsIgnoreCase(value))) {
                throw new RuntimeException("Content link has to be identified with a null or empty value instead of : " + value);
            } else if (StringUtils.isEmpty(value)) {
                throw new RuntimeException("Link " + link + " has to contain a value for builder");
            }
            if (link == CONTENT) {
                value = "content";
            }
            parts.put(link, value);
            return this;
        }

        public RegistrationType getType() {
            return type;
        }

        public String getLinkTypeName() {
            if (getType() == null) {
                return null;
            }
            return getType().getName();
        }

        public String buildLinkKey() {
            if (getType() != null && !RegistrationType.Defaults.CHAIN_LINK.equals(getType())) {
                return RegistrationType.Defaults.CHAIN_LINK.getName() + ":" + getType().getName();
            } else {
                return RegistrationType.Defaults.CHAIN_LINK.getName();
            }
        }

        public String buildTargetLink() {
            if (CollectionUtils.isEmpty(parts)) {
                throw new RuntimeException("Cannot build Target link with no parts");
            }
            Link last = parts.lastKey();
            Link current = last;
            int size = 1;
            while (current.hasParent()) {
                size++;
                current = current.getParent();
            }
            if (size != parts.size()) {
                throw new RuntimeException(String.format("Registered # parts %d is not equal to required # parts %d", parts.size(), size));
            }
            String[] values = parts.values().toArray(new String[0]);
            return String.format(last.template, values);
        }

        public SortedMap<Link, String> getParts() {
            return new TreeMap<>(parts);
        }


    }
}
