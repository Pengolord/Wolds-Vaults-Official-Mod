package xyz.iwolfking.woldsvaults.api.helper;

import iskallia.vault.VaultMod;
import iskallia.vault.config.gear.VaultGearTierConfig;
import iskallia.vault.gear.attribute.VaultGearModifier;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.modification.GearModification;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.util.MiscUtils;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WoldGearModifierHelper {
    public static GearModification.Result addUnusualModifier(ItemStack stack, long worldGameTime, Random random) {
        VaultGearData data = VaultGearData.read(stack);
        VaultGearTierConfig cfg = VaultGearTierConfig.getConfig(stack).orElse(null);
        if (cfg == null) {
            VaultMod.LOGGER.error("Unknown VaultGear: " + stack);
            return GearModification.Result.errorUnmodifiable();
        } else if (!data.isModifiable()) {
            return GearModification.Result.errorUnmodifiable();
        } else {
            int itemLevel = data.getItemLevel();
            int prefixes = data.getFirstValue(ModGearAttributes.PREFIXES).orElse(0);
            int suffixes = data.getFirstValue(ModGearAttributes.SUFFIXES).orElse(0);
            prefixes -= data.getModifiers(VaultGearModifier.AffixType.PREFIX).size();
            suffixes -= data.getModifiers(VaultGearModifier.AffixType.SUFFIX).size();
            boolean hasUnusual = false;
            for(VaultGearModifier<?> modifier : data.getModifiers(VaultGearModifier.AffixType.PREFIX)) {
                hasUnusual = modifier.getCategories().contains(VaultGearModifier.AffixCategory.valueOf("UNUSUAL"));
                if(hasUnusual) {
                    break;
                }
            }

            if(hasUnusual) {
                return GearModification.Result.makeActionError("full");
            }

            for(VaultGearModifier<?> modifier : data.getModifiers(VaultGearModifier.AffixType.SUFFIX)) {
                hasUnusual = modifier.getCategories().contains(VaultGearModifier.AffixCategory.valueOf("UNUSUAL"));
                if(hasUnusual) {
                    break;
                }
            }

            if(hasUnusual) {
                return GearModification.Result.makeActionError("full");
            }

            if (prefixes <= 0 && suffixes <= 0) {
                return GearModification.Result.makeActionError("full");
            } else {
                List<VaultGearModifier.AffixType> types = new ArrayList<>();
                if (prefixes > 0) {
                    types.add(VaultGearModifier.AffixType.PREFIX);
                }

                if (suffixes > 0) {
                    types.add(VaultGearModifier.AffixType.SUFFIX);
                }



                VaultGearModifier.AffixType type = MiscUtils.getRandomEntry(types, random);
                if(type == null) {
                    return GearModification.Result.errorInternal();
                }

                VaultGearTierConfig.ModifierAffixTagGroup modifierAffixTagGroup = VaultGearTierConfig.ModifierAffixTagGroup.valueOf("UNUSUAL_PREFIX");


                if(type.equals(VaultGearModifier.AffixType.PREFIX)) {
                    return cfg.getRandomModifier(modifierAffixTagGroup, itemLevel, random,data.getExistingModifierGroups(VaultGearData.Type.EXPLICIT_MODIFIERS)).map(modifier -> {
                        data.getAllModifierAffixes().forEach(VaultGearModifier::resetGameTimeAdded);
                        modifier.setGameTimeAdded(worldGameTime);
                        modifier.addCategory(VaultGearModifier.AffixCategory.valueOf("UNUSUAL"));
                        data.addModifier(type, modifier);
                        data.write(stack);
                        return GearModification.Result.makeSuccess();
                    }).orElse(GearModification.Result.makeActionError("no_modifiers"));
                }

                else if(type.equals(VaultGearModifier.AffixType.SUFFIX)) {
                    return cfg.getRandomModifier(VaultGearTierConfig.ModifierAffixTagGroup.valueOf("UNUSUAL_SUFFIX"), itemLevel, random, data.getExistingModifierGroups(VaultGearData.Type.EXPLICIT_MODIFIERS)).map(modifier -> {
                        data.getAllModifierAffixes().forEach(VaultGearModifier::resetGameTimeAdded);
                        modifier.setGameTimeAdded(worldGameTime);
                        modifier.addCategory(VaultGearModifier.AffixCategory.valueOf("UNUSUAL"));
                        data.addModifier(type, modifier);
                        data.write(stack);
                        return GearModification.Result.makeSuccess();
                    }).orElse(GearModification.Result.makeActionError("no_modifiers"));
                }

                return GearModification.Result.errorInternal();

            }
        }
    }

    public static GearModification.Result removeRandomModifierAlways(ItemStack stack, Random random) {
        VaultGearData data = VaultGearData.read(stack);
            List<VaultGearModifier<?>> affixes = new ArrayList<>();
            affixes.addAll(data.getModifiers(VaultGearModifier.AffixType.PREFIX));
            affixes.addAll(data.getModifiers(VaultGearModifier.AffixType.SUFFIX));
            affixes.removeIf(modifier -> !modifier.hasNoCategoryMatching(VaultGearModifier.AffixCategory::cannotBeModifiedByArtisanFoci));
            if (affixes.isEmpty()) {
                return GearModification.Result.makeActionError("no_modifiers");
            } else {
                VaultGearModifier<?> randomMod = MiscUtils.getRandomEntry(affixes, random);
                data.removeModifier(randomMod);
                data.write(stack);
                return GearModification.Result.makeSuccess();
            }
    }
}
