package xyz.iwolfking.woldsvaults.mixins.vaulthunters.skills;

import iskallia.vault.init.ModAbilityLabelBindings;
import iskallia.vault.skill.ability.component.AbilityLabelFormatters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.iwolfking.woldsvaults.abilities.ChainMinerAbility;
import xyz.iwolfking.woldsvaults.abilities.ColossusAbility;
import xyz.iwolfking.woldsvaults.abilities.LevitateAbility;
import xyz.iwolfking.woldsvaults.abilities.SneakyGetawayAbility;

import java.util.Map;

@Mixin(value = ModAbilityLabelBindings.class,remap = false)
public abstract class MixinModAbilityLabelBindings {


    @Inject(method = "register()V",at = @At("TAIL"))
    private static void registerDescriptions(CallbackInfo ci) {
        ModAbilityLabelBindings.register(ColossusAbility.class, Map.of(
                "additionalResistance",
                ability -> AbilityLabelFormatters.percentRounded(ability.getAdditionalResistance()),
                "size",
                ability -> AbilityLabelFormatters.percentRounded(ability.getSize()),
                "cooldown",
                ability -> AbilityLabelFormatters.ticks(ability.getCooldownTicks()),
                "manaCost",
                ability -> AbilityLabelFormatters.integer((int) ability.getManaCost()),
                "duration",
                ability -> AbilityLabelFormatters.ticks(ability.getDurationTicks())

        ));
        ModAbilityLabelBindings.register(SneakyGetawayAbility.class, Map.of(
                "speed",
                ability -> AbilityLabelFormatters.percentRounded(ability.getSpeedPercentAdded()),
                "size",
                ability -> AbilityLabelFormatters.percentRounded(ability.getSize()),
                "cooldown",
                ability -> AbilityLabelFormatters.ticks(ability.getCooldownTicks()),
                "manaCost",
                ability -> AbilityLabelFormatters.integer((int) ability.getManaCost()),
                "duration",
                ability -> AbilityLabelFormatters.ticks(ability.getDurationTicks())

        ));
        ModAbilityLabelBindings.register(ChainMinerAbility.class, Map.of(
                "blocks",
                ability -> AbilityLabelFormatters.integer(ability.getUnmodifiedBlockLimit()),
                "distance",
                ability -> AbilityLabelFormatters.integer(ability.getRange())

        ));
        ModAbilityLabelBindings.register(LevitateAbility.class, Map.of(
                "levitateSpeed",
                ability -> AbilityLabelFormatters.decimal(ability.getLevitateSpeed())

        ));
    }
}
