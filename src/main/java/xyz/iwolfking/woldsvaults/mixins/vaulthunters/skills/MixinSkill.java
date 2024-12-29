package xyz.iwolfking.woldsvaults.mixins.vaulthunters.skills;

import iskallia.vault.core.data.adapter.basic.TypeSupplierAdapter;
import iskallia.vault.skill.base.Skill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.iwolfking.woldsvaults.abilities.ChainMinerAbility;
import xyz.iwolfking.woldsvaults.abilities.ColossusAbility;
import xyz.iwolfking.woldsvaults.abilities.LevitateAbility;
import xyz.iwolfking.woldsvaults.abilities.SneakyGetawayAbility;
import xyz.iwolfking.woldsvaults.expertises.CraftsmanExpertise;

@Mixin(value = Skill.Adapter.class, remap = false)
public class MixinSkill extends TypeSupplierAdapter<Skill> {


    public MixinSkill(String key, boolean nullable) {
        super(key, nullable);
    }

    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void addSkills(CallbackInfo ci) {
        this.register("craftsman", CraftsmanExpertise.class, CraftsmanExpertise::new);
        this.register("colossus", ColossusAbility.class,ColossusAbility::new);
        this.register("sneaky_getaway", SneakyGetawayAbility.class,SneakyGetawayAbility::new);
        this.register("vein_miner_chain", ChainMinerAbility.class, ChainMinerAbility::new);
        this.register("levitate", LevitateAbility.class,LevitateAbility::new);
    }
}
