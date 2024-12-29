package xyz.iwolfking.woldsvaults.mixins.vaulthunters.custom.enum_extension;

import iskallia.vault.gear.attribute.VaultGearModifier;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import xyz.iwolfking.woldsvaults.api.gear.actions.CustomModifierCategoryTooltip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

@Mixin(value = VaultGearModifier.AffixCategory.class, remap = false)
public class MixinAffixCategoryEnum {
    @Shadow
    @Final
    @Mutable
    @SuppressWarnings("target")
    private static VaultGearModifier.AffixCategory[] $VALUES;

    private static final VaultGearModifier.AffixCategory UNUSUAL = enumExpansion$addVariant("UNUSUAL", "Unusual", CustomModifierCategoryTooltip::modifyUnusualTooltip);


    @Invoker("<init>")
    public static VaultGearModifier.AffixCategory enumExpansion$invokeInit(String internalName, int internalId, String descriptor, Function<MutableComponent, MutableComponent> modifierFormatter) {
        throw new AssertionError();
    }

    @Unique
    private static VaultGearModifier.AffixCategory enumExpansion$addVariant(String internalName, String descriptor, Function<MutableComponent, MutableComponent> modifierFormatter) {
        ArrayList<VaultGearModifier.AffixCategory> variants = new ArrayList<>(Arrays.asList(MixinAffixCategoryEnum.$VALUES));
        VaultGearModifier.AffixCategory type = enumExpansion$invokeInit(internalName, variants.get(variants.size() - 1).ordinal() + 1, descriptor, modifierFormatter);
        variants.add(type);
        MixinAffixCategoryEnum.$VALUES = variants.toArray(new VaultGearModifier.AffixCategory[0]);
        return type;
    }
}
