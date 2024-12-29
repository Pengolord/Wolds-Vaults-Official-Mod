package xyz.iwolfking.woldsvaults.models;

import iskallia.vault.VaultMod;
import iskallia.vault.dynamodel.DynamicModelProperties;
import iskallia.vault.dynamodel.model.item.PlainItemModel;
import iskallia.vault.dynamodel.registry.DynamicModelRegistry;

public class Jewels {
    public static final DynamicModelRegistry<PlainItemModel> REGISTRY = new DynamicModelRegistry<>();
    public static final PlainItemModel STELLA;

    static {
        STELLA = REGISTRY.register(new PlainItemModel(VaultMod.id("gear/jewel/stella"), "Eternal Stella")).properties(new DynamicModelProperties().allowTransmogrification().discoverOnRoll());
    }
}
