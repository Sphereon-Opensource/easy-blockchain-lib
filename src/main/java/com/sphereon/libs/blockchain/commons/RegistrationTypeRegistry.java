package com.sphereon.libs.blockchain.commons;

import com.sphereon.libs.blockchain.commons.links.Subsystem;
import org.apache.commons.lang3.StringUtils;

import java.util.*;


public class RegistrationTypeRegistry {
    private Map<String, RegistrationType> registrationTypes = new HashMap<>();

    public RegistrationTypeRegistry add(RegistrationType registrationType, Subsystem subsystem) {
        if (!registrationTypes.keySet().contains(registrationType.getName())) {
            registrationTypes.put(registrationType.getName(), registrationType);
        }
//        registrationTypes.get(registrationType.getName()).registerSubsytem(subsystem);
        return this;
    }

    public boolean contains(RegistrationType registrationType, Subsystem subsystem) {
        return registrationTypes.keySet().contains(registrationType.getName()) && registrationTypes.get(registrationType.getName()).isRegistered(subsystem);
    }

    public RegistrationType get(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        RegistrationType registrationType = registrationTypes.get(name);
        if (registrationType == null) {
            for (Map.Entry<String, RegistrationType> entry : registrationTypes.entrySet()) {
                if (name.equalsIgnoreCase(entry.getKey()) || name.equalsIgnoreCase(entry.getValue().getName())) {
                    registrationType = entry.getValue();
                    break;
                }
            }
        }
        return registrationType;
    }

    public Collection<RegistrationType> getAll() {
        return registrationTypes.values();
    }

    public Collection<RegistrationType> getAll(Subsystem subsystem) {
        List<RegistrationType> bySubsytem = new ArrayList<>();
        for (RegistrationType registrationType : getAll()) {
            if (registrationType.isRegistered(subsystem) && !bySubsytem.contains(subsystem)) {
                bySubsytem.add(registrationType);
            }
        }
        return bySubsytem;
    }

    public RegistrationTypeRegistry initSubsystem(Subsystem subSystem) {
        subSystem.registerDefaultRegistrations();
        return this;
    }

    public RegistrationTypeRegistry initDefaultSubsystems() {
        initSubsystem(Subsystem.Default.ALFRESCO);
        initSubsystem(Subsystem.Default.SHAREPOINT);
        initSubsystem(Subsystem.Default.FILE);
        initSubsystem(Subsystem.Default.CUSTOM);
        return this;
    }


    private static volatile RegistrationTypeRegistry instance;

    private RegistrationTypeRegistry() {
    }

    public static RegistrationTypeRegistry getInstance() {
        /*
        We use double checked locking and a non final instance, since we do not know beforehand whether we operate
        within a Sring context or not.  We also provide configuration support for the Spring singleton scope
        */
        if (instance == null) {
            synchronized (RegistrationTypeRegistry.class) {
                if (instance == null) {
                    RegistrationTypeRegistry.instance = new RegistrationTypeRegistry();
                }
            }
        }
        return instance;
    }
}
