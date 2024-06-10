package xyz.iwolfking.woldsvaults.blocks.tiles;

import iskallia.vault.block.entity.ScavengerAltarTileEntity;
import iskallia.vault.world.data.ServerVaults;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DecoScavengerAltarEntity extends ScavengerAltarTileEntity {

    public DecoScavengerAltarEntity(BlockPos pos, BlockState state) {
        super(xyz.iwolfking.woldsvaults.init.ModBlocks.DECO_SCAVENGER_ALTAR_ENTITY_BLOCK_ENTITY_TYPE, pos, state);
        this.ticksToConsume = 40;
        this.consuming = false;
    }

    public static void tickClient(Level world, BlockPos pos, BlockState state, DecoScavengerAltarEntity tile) {
        ScavengerAltarTileEntity.tickClient(world, pos, state, tile);
    }


    public static void tickServer(Level world, BlockPos pos, BlockState state, DecoScavengerAltarEntity tile) {
        if(ServerVaults.get(world).isPresent()) {
            return;
        }
        ScavengerAltarTileEntity.tickServer(world, pos, state, tile);
    }

}