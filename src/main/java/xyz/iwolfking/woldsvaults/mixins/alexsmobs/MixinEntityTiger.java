package xyz.iwolfking.woldsvaults.mixins.alexsmobs;


import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "alexsmobs")
        }
)
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityTiger$AIMelee")
public abstract class MixinEntityTiger {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"))
    private boolean overrideEffects(LivingEntity instance, MobEffectInstance pEffectInstance) {
        return false;
    }

}
