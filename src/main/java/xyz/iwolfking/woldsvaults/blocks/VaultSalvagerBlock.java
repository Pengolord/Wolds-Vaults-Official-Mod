package xyz.iwolfking.woldsvaults.blocks;

import iskallia.vault.util.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import xyz.iwolfking.woldsvaults.blocks.tiles.VaultSalvagerTileEntity;
import xyz.iwolfking.woldsvaults.init.ModBlocks;

import javax.annotation.Nullable;

public class VaultSalvagerBlock extends Block implements EntityBlock {
    public VaultSalvagerBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE).strength(0.5F).lightLevel(state -> 13).noOcclusion());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else if (player instanceof ServerPlayer sPlayer) {
            if (level.getBlockEntity(pos) instanceof VaultSalvagerTileEntity recyclerTileEntity) {
                NetworkHooks.openGui(sPlayer, recyclerTileEntity, buffer -> buffer.writeBlockPos(pos));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    @Nullable
    public <A extends BlockEntity> BlockEntityTicker<A> getTicker(Level world, BlockState state, BlockEntityType<A> type) {
        return BlockHelper.getTicker(type, xyz.iwolfking.woldsvaults.init.ModBlocks.VAULT_SALVAGER_ENTITY, VaultSalvagerTileEntity::tick);
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof VaultSalvagerTileEntity recycler) {
                Containers.dropContents(pLevel, pPos, recycler.getInventory());
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }

    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlocks.VAULT_SALVAGER_ENTITY.create(pos, state);
    }
}
