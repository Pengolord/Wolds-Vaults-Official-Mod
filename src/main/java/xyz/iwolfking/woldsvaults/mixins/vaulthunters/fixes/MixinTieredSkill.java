package xyz.iwolfking.woldsvaults.mixins.vaulthunters.fixes;

import iskallia.vault.skill.base.LearnableSkill;
import iskallia.vault.skill.base.TieredSkill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = TieredSkill.class, remap = false)
public abstract class MixinTieredSkill {
    @Shadow private int tier;
    @Shadow private List<LearnableSkill> tiers;

    @Shadow public abstract int getMaxLearnableTier();

    @Shadow private int maxLearnableTier;

    /**
     * @author iwolfking
     * @reason Fix crash when a skill has more points than can be learned.
     */
    @Overwrite
    public int getSpentLearnPoints(int tier) {
        int points = 0;

        if(tier > getMaxLearnableTier()) {
            tier = getMaxLearnableTier();
        }

        for (int i = 0; i < tier; ++i) {
            points += this.tiers.get(i).getLearnPointCost();
        }



        return points;
    }
}
