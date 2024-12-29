package xyz.iwolfking.woldsvaults.models;

import iskallia.vault.VaultMod;
import iskallia.vault.dynamodel.DynamicModelProperties;
import iskallia.vault.dynamodel.model.item.HandHeldModel;
import iskallia.vault.dynamodel.model.item.PlainItemModel;
import iskallia.vault.init.ModDynamicModels;

public class AdditionalModels {

    public static final HandHeldModel LEVIATHAN_AXE;
    public static final PlainItemModel BLOODSEEKING_MAGNET;
    public static final PlainItemModel OTHERWORLDLY_MAGNET;
    public static final PlainItemModel HEART_MAGNET;
    public static final PlainItemModel TREASURE_MAGNET;
    public static final HandHeldModel EVERFROST;
    public static final HandHeldModel EVERFLAME;
    public static final HandHeldModel HEXBLADE;

    static {
       LEVIATHAN_AXE = ModDynamicModels.Axes.REGISTRY.register(new HandHeldModel(VaultMod.id("gear/axe/leviathan"), "Leviathan")).properties(new DynamicModelProperties());
       BLOODSEEKING_MAGNET = ModDynamicModels.Magnets.REGISTRY_MAGNETS.register(new PlainItemModel(VaultMod.id("magnets/bloody_magnet"), "Bloodseeking Magnet")).properties(new DynamicModelProperties());
       OTHERWORLDLY_MAGNET = ModDynamicModels.Magnets.REGISTRY_MAGNETS.register(new PlainItemModel(VaultMod.id("magnets/ender_magnet"), "Otherworldly Magnet")).properties(new DynamicModelProperties());
       HEART_MAGNET = ModDynamicModels.Magnets.REGISTRY_MAGNETS.register(new PlainItemModel(VaultMod.id("magnets/heart_magnet"), "Heart Magnet")).properties(new DynamicModelProperties());
       TREASURE_MAGNET = ModDynamicModels.Magnets.REGISTRY_MAGNETS.register(new PlainItemModel(VaultMod.id("magnets/treasure_magnet"), "Treasure Seeker's Magnet")).properties(new DynamicModelProperties());
       EVERFROST = ModDynamicModels.Swords.REGISTRY.register(new HandHeldModel(VaultMod.id("gear/sword/everfrost"), "Everfrost")).properties(new DynamicModelProperties());
       EVERFLAME = ModDynamicModels.Swords.REGISTRY.register(new HandHeldModel(VaultMod.id("gear/sword/everflame"), "Everflame")).properties(new DynamicModelProperties());
       HEXBLADE = ModDynamicModels.Swords.REGISTRY.register(new HandHeldModel(VaultMod.id("gear/sword/hexblade"), "Hexblade")).properties(new DynamicModelProperties());
    }
}
