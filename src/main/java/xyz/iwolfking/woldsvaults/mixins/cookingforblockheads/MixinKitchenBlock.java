package xyz.iwolfking.woldsvaults.mixins.cookingforblockheads;


import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.tile.IMutableNameable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "cookingforblockheads")
        }
)
@Mixin(BlockKitchen.class)
public class MixinKitchenBlock {

    /**
     * @author iwolfking
     * @reason Patch nbt chunk reset with renamed Spice Rack
     */
    @Overwrite
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            if (level.getBlockEntity(pos) instanceof IMutableNameable blockEntity) {
                blockEntity.setCustomName(itemStack.getHoverName());
            }
        }

    }
}
