package xyz.iwolfking.woldsvaults.mixins.vaulthunters.accessors;

import iskallia.vault.block.ScavengerAltarBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ScavengerAltarBlock.class)
public abstract class MixinScavengerAltar extends Block implements EntityBlock {

    public MixinScavengerAltar(Properties pProperties) {
        super(pProperties);
    }
}
