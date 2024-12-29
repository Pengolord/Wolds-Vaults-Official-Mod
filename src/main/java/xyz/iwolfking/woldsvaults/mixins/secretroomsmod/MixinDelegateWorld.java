package xyz.iwolfking.woldsvaults.mixins.secretroomsmod;

import com.wynprice.secretrooms.client.world.DelegateWorld;
import com.wynprice.secretrooms.server.blocks.SecretBaseBlock;
import com.wynprice.secretrooms.server.data.SecretData;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "secretroomsmod")
        }
)
@Mixin(value = DelegateWorld.class, remap = false)
public abstract class MixinDelegateWorld implements BlockAndTintGetter {
    @Shadow private BlockGetter world;

    /**
     * @author iwolfking
     * @reason Add null-check
     */
    @Overwrite
    public BlockState getBlockState(BlockPos pos) {
        if(this.world == null) {
            return null;
        }
        return SecretBaseBlock.getMirrorState(this.world, pos).orElseGet(() -> this.world.getBlockState(pos));
    }

    /**
     * @author iwolfking
     * @reason Add null-check
     */
    @Overwrite
    @Nullable
    public BlockEntity getBlockEntity(BlockPos pos) {
        if(this.world == null) {
            return null;
        }
        return SecretBaseBlock.getMirrorData(this.world, pos).map(SecretData::getTileEntityCache).orElseGet(() -> this.world.getBlockEntity(pos));
    }
}
