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


    private static int countSlashes(String input) {
        int count = 0;
        if (!StringUtils.isEmpty(input)) {
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == '/') {
                    count++;
                }
            }
        }
        return count;
    }

    public static Parser parser() {
        return new Parser();
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


        public Builder add(SortedMap<Link, String> parts) {
            if (CollectionUtils.isEmpty(parts)) {
                return this;
            }
            for (Map.Entry<Link, String> entry : parts.entrySet()) {
                add(entry.getKey(), entry.getValue());
            }
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

        public byte[] buildLinkKeyasBytes() {
            return buildLinkKey().getBytes();
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

        public byte[] buildTargetLinkasBytes() {
            return buildTargetLink().getBytes();
        }

        public SortedMap<Link, String> getParts() {
            return new TreeMap<>(parts);
        }


    }


    public static class Parser {
        public RegistrationType linkKeyType(String input) {
            String val = linkKeyValue(input);
            if (StringUtils.isEmpty(val)) {
                if (isLinkKey(input)) {
                    // Create on the fly
                    return RegistrationType.Impl.of(input);
                }
                return null;
            }
            Set<RegistrationType> registrations = RegistrationType.Impl.from(input);

            return registrations.size() == 0 ? null : (RegistrationType) registrations.toArray()[registrations.size()];
        }

        public String linkKeyValueOrInput(String input) {
            String linkKeyValue = linkKeyValue(input);
            if (StringUtils.isEmpty(linkKeyValue)) {
                linkKeyValue = input;
            }
            return linkKeyValue;
        }

        public String linkKeyValue(String input) {
            if (!isLinkKey(input)) {
                return null;
            }
            return RegistrationType.Impl.stripChainLinkKey(input);
        }

        public boolean isLinkKey(String input) {
            return !StringUtils.isEmpty(input) && input.startsWith(RegistrationType.Defaults.CHAIN_LINK_KEY);
        }

        public Link targetLinkType(String input) {
            return targetLinkParts(input).lastKey();
        }

        public String targetLinkPart(String input, Link link) {
            return targetLinkParts(input).get(link);
        }

        public SortedMap<Link, String> targetLinkParts(String input) {
            SortedMap<Link, String> parsed = new TreeMap<>();
            if (StringUtils.isEmpty(input) || !input.startsWith("/")) {
                parsed.put(NONE, input);
                return parsed;
            }
            int inputSlashes = countSlashes(input);
            for (Link link : Link.values()) {
                int linkSlashes = countSlashes(link.getTemplate());
                if (link == NONE) {
                    if (inputSlashes == 0) {
                        parsed.put(NONE, input);
                        break;
                    }
                } else if (linkSlashes <= inputSlashes) {
                    if (!input.endsWith("/content") && link.getTemplate().endsWith("/content")) {
                        continue;
                    }

                    String result = input;
                    for (int i = 0; i < linkSlashes; i++) {
                        result = result.replaceFirst("[^/]*/", "");
                    }
                    parsed.put(link, result.replaceAll("/[^/]*", ""));
                }
            }


            return parsed;
        }
    }
}
