package com.sphereon.libs.blockchain.commons.links;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EntryTypeRegistry {
    private Map<String, EntryType> entryTypes = new HashMap<>();

    public EntryTypeRegistry add(EntryType entryType) {
        if (!contains(entryType)) {
            entryTypes.put(entryType.getName(), entryType);
        }
        return this;
    }

    public boolean contains(EntryType entryType) {
        return entryTypes.keySet().contains(entryType.getName());
    }

    public EntryType get(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        EntryType entryType = entryTypes.get(name);
        if (entryType == null) {
            for (Map.Entry<String, EntryType> entry : entryTypes.entrySet()) {
                if (name.equalsIgnoreCase(entry.getKey()) || name.equalsIgnoreCase(entry.getValue().getName())) {
                    entryType = entry.getValue();
                    break;
                }
            }
        }
        return entryType;
    }

    public EntryTypeRegistry initSubsystem(Subsystem subSystem) {
        return this;
    }

    public EntryTypeRegistry initDefaultSubsystems() {
        return this;
    }
}
