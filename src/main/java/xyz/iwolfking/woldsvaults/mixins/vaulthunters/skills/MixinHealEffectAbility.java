package xyz.iwolfking.woldsvaults.mixins.vaulthunters.skills;

import iskallia.vault.skill.ability.effect.HealEffectAbility;
import iskallia.vault.skill.ability.effect.spi.AbstractHealAbility;
import iskallia.vault.skill.ability.effect.spi.core.Ability;
import iskallia.vault.skill.base.SkillContext;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = HealEffectAbility.class, remap = false)
public class MixinHealEffectAbility extends AbstractHealAbility {
    @Shadow private HealEffectAbility.RemovalStrategy removalStrategy;

    /**
     * @author iwolfking
     * @reason Cleanse still heals for a quarter of the normal value.
     */
    @Overwrite @Override
    protected Ability.ActionResult doAction(SkillContext context) {
        return context.getSource().as(ServerPlayer.class).map((player) -> {
            float healed = this.getFlatLifeHealed();
            player.heal(healed);
            this.removalStrategy.apply(player, (HealEffectAbility) (Object)this);
            return ActionResult.successCooldownImmediate();
        }).orElse(ActionResult.fail());
    }
}
