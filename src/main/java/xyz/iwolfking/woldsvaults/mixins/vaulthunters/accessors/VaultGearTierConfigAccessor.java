package xyz.iwolfking.woldsvaults.mixins.vaulthunters.accessors;

import iskallia.vault.config.gear.VaultGearTierConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = VaultGearTierConfig.class, remap = false)
public interface VaultGearTierConfigAccessor {
    @Accessor("modifierGroup")
    Map<VaultGearTierConfig.ModifierAffixTagGroup, VaultGearTierConfig.AttributeGroup> getModifierGroup();
}
