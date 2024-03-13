package xyz.iwolfking.woldsvaults.mixins;


import com.github.alexthe666.alexsmobs.entity.EntityTiger;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityTiger$AIMelee")
public abstract class MixinEntityTiger {

    @Shadow private EntityTiger tiger;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"))
    private boolean overrideEffects(LivingEntity instance, MobEffectInstance p_21165_) {
        return false;
    }

}
