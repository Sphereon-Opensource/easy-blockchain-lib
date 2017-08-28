package com.sphereon.libs.blockchain.commons.links;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

public enum Link {
    NONE("%s"), CONTEXT("/%s"), CHAIN_ID("/%s/chains/%s", CONTEXT), ENTRY_ID("/%s/chains/%s/entries/%s", CHAIN_ID), EXTERNAL_ID("/%s/chains/%s/entries/%s/externalids/%s", ENTRY_ID), CONTENT("/%s/chains/%s/entries/%s/content", ENTRY_ID);

    private final Link parent;
    private List<Link> children = new ArrayList<>();

    private String template;

    Link(String template) {
        this.template = template;
        this.parent = null;
    }

    Link(String template, Link parent) {
        this.template = template;
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
    }
    protected String getTemplate() {
        return template;
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

    public Builder newBuilder() {
        return new Builder().start(this);
    }

    public class Builder {
        private SortedMap<Link, String> parts;

        private Builder() {
            reset();
        }

        public Builder reset() {
            this.parts = new TreeMap<>();
            return this;
        }

        public Builder start(Link link) {
            return new Builder();
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

        public String build() {
            if (CollectionUtils.isEmpty(parts)) {
                throw new RuntimeException("Cannot build link with no parts");
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
