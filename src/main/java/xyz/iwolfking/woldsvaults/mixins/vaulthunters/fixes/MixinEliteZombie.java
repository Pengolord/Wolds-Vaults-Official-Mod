package xyz.iwolfking.woldsvaults.mixins.vaulthunters.fixes;

import iskallia.vault.entity.entity.elite.EliteZombieEntity;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EliteZombieEntity.class, remap = false)
public class MixinEliteZombie {
    @Shadow @Final private static int NUMBER_OF_SPAWN_TRIES;
    @Unique
    private int woldsVaults$minionSpawnCounter = 0;

    @Inject(method = "spawnMinions", at = @At("HEAD"), cancellable = true)
    private void cancelSpawnAtMaxSpawnCount(ServerLevel serverLevel, CallbackInfo ci) {
        woldsVaults$minionSpawnCounter++;

        if(woldsVaults$minionSpawnCounter > NUMBER_OF_SPAWN_TRIES) {
            ci.cancel();
        }
    }
}
