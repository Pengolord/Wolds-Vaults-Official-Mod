package xyz.iwolfking.woldsvaults.mixins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LightningBolt.class)
public abstract class MixinLightningBolt extends Entity {

    public MixinLightningBolt(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
    private void spawnFire(int pExtraIgnitions, CallbackInfo ci) {
        if(this.level.dimension().location().getNamespace().equals("the_vault")) {
            ci.cancel();
        }

    }
}
