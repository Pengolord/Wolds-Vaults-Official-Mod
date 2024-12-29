package xyz.iwolfking.woldsvaults.mixins.vaulthunters.fixes;

import iskallia.vault.VaultMod;
import iskallia.vault.config.gear.VaultGearTierConfig;
import iskallia.vault.gear.GearScoreHelper;
import iskallia.vault.gear.VaultGearRarity;
import iskallia.vault.gear.attribute.VaultGearModifier;
import iskallia.vault.gear.attribute.config.ConfigurableAttributeGenerator;
import iskallia.vault.gear.data.AttributeGearData;
import iskallia.vault.gear.data.GearDataCache;
import iskallia.vault.gear.data.VaultGearData;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = GearScoreHelper.class, remap = false)
public class MixinGearScoreHelper {
    /**
     * @author iwolfking
     * @reason Add additional weight considerations
     */
    @Overwrite
    public static int getWeight(ItemStack stack) {
        AttributeGearData attributeData = AttributeGearData.read(stack);
        if (attributeData instanceof VaultGearData gearData) {
            GearDataCache cache = GearDataCache.of(stack);
            int weight = 0;
            if (cache.hasModifierOfCategory(VaultGearModifier.AffixCategory.LEGENDARY)) {
                weight += 1000000;
            }

            VaultGearRarity rarity = gearData.getRarity();
            weight += rarity.ordinal() * 10000;
            VaultGearTierConfig cfg = VaultGearTierConfig.getConfig(stack).orElse(null);
            if (cfg == null) {
                return weight;
            } else {
                int level = gearData.getItemLevel();
                List<VaultGearModifier<?>> modifiers = new ArrayList<>();
                modifiers.addAll(gearData.getModifiers(VaultGearModifier.AffixType.PREFIX));
                modifiers.addAll(gearData.getModifiers(VaultGearModifier.AffixType.SUFFIX));
                List<Float> rangePercentage = new ArrayList<>();
                List<Integer> weightsToAdd = new ArrayList<>();
                modifiers.forEach(mod -> {
                    if(mod.getAttribute().getRegistryName().equals(VaultMod.id("hammer_size"))) {
                        weightsToAdd.add(50000);
                    }
                    if(mod.getAttribute().getRegistryName().equals(VaultMod.id("soulbound"))) {
                        weightsToAdd.add(25000);
                    }
                    VaultGearTierConfig.ModifierConfigRange range = cfg.getTierConfigRange(mod, level);
                    ConfigurableAttributeGenerator generator = mod.getAttribute().getGenerator();
                    generator.getRollPercentage(mod.getValue(), range.allTierConfigs()).ifPresent(value -> {
                        if (value instanceof Float f) {
                            rangePercentage.add(f);
                        }

                    });
                });
                if (!rangePercentage.isEmpty()) {
                    float avg = (float) rangePercentage.stream().mapToDouble(Float::doubleValue).average().orElse(1.0);
                    weight += (int) (avg * 1000.0F);
                }
                for(Integer value : weightsToAdd) {
                    weight += value;
                }

                return weight;
            }
        } else {
            return 0;
        }
    }
}
