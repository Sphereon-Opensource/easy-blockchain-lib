package com.sphereon.libs.blockchain.commons;

public enum ChainType {
    FILE_CHAIN("File chain"), SETTINGS_CHAIN("Settings chain"), PROOF_CHAIN("Proof chain"), METADATA_CHAIN("Metadata chain");


    private final String label;


    ChainType(String label) {
        this.label = label;
    }


    public String getLabel() {
        return label;
    }


    @Override
    public String toString() {
        return label;
    }
}
