package xyz.iwolfking.woldsvaults.mixins.vaulthunters.fixes.bettercombat;

import iskallia.vault.event.GearAttributeEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = GearAttributeEvents.class, remap = false)
public class MixinGearAttributeEvents {

    @ModifyConstant(method = "triggerChainAttack", constant = @Constant(floatValue = 1.0F, ordinal = 0))
    private static float adjustAttackScaleRequirementChaining(float constant) {
        return 0.5F;
    }

    @ModifyConstant(method = "triggerKnockbackAttack", constant = @Constant(floatValue = 1.0F, ordinal = 0))
    private static float adjustAttackScaleRequirementShocking(float constant) {
        return 0.5F;
    }

    @ModifyConstant(method = "triggerStunAttack", constant = @Constant(floatValue = 1.0F, ordinal = 0))
    private static float adjustAttackScaleRequirementStunning(float constant) {
        return 0.5F;
    }

    @ModifyConstant(method = "triggerAoEAttack", constant = @Constant(floatValue = 1.0F, ordinal = 0))
    private static float adjustAttackScaleRequirementCleave(float constant) {
        return 0.5F;
    }
}
