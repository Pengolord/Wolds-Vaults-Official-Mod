package xyz.iwolfking.woldsvaults.abilities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import xyz.iwolfking.woldsvaults.abilities.spi.AbstractChainMinerAbility;

public class ChainMinerAbility extends AbstractChainMinerAbility {
    public ChainMinerAbility(int unlockLevel, int learnPointCost, int regretPointCost, int cooldownTicks, int blockLimit, int range) {
        super(unlockLevel, learnPointCost, regretPointCost, cooldownTicks, blockLimit, range);
    }

    public ChainMinerAbility() {
    }

    @Override
    protected ItemStack getVeinMiningItemProxy(Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).copy();
    }

    @Override
    public boolean shouldVoid(ServerLevel level, ServerPlayer player, BlockState target) {
        return false;
    }
}