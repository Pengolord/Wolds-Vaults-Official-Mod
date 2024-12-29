package xyz.iwolfking.woldsvaults.mixins.occultism;

import com.github.klikli_dev.occultism.common.entity.familiar.FamiliarEntity;
import com.github.klikli_dev.occultism.common.entity.familiar.IFamiliar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FamiliarEntity.class, remap = false)
public abstract class MixinFamiliarUtils extends PathfinderMob implements IFamiliar {

    protected MixinFamiliarUtils(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "getOwner", at= @At("HEAD"), cancellable = true)
    private void cancelFamiliarInVaults(CallbackInfoReturnable<LivingEntity> cir) {
        if(this.level.dimension().location().getNamespace().equals("the_vault")) {
            cir.setReturnValue(null);
        }
    }

}
