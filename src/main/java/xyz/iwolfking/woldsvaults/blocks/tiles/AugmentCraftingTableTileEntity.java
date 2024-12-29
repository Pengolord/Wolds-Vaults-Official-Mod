package xyz.iwolfking.woldsvaults.blocks.tiles;

import iskallia.vault.block.entity.base.ForgeRecipeTileEntity;
import iskallia.vault.config.recipe.ForgeRecipeType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xyz.iwolfking.woldsvaults.blocks.containers.AugmentCraftingTableContainer;
import xyz.iwolfking.woldsvaults.init.ModBlocks;

public class AugmentCraftingTableTileEntity extends ForgeRecipeTileEntity implements MenuProvider {

    public AugmentCraftingTableTileEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlocks.AUGMENT_CRAFTING_TABLE_ENTITY, pWorldPosition, pBlockState, 6, ForgeRecipeType.valueOf("AUGMENT"));
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Augment Assembly Pedestal");
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new AugmentCraftingTableContainer(i, this.getLevel(), this.getBlockPos(), inventory);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new AugmentCraftingTableContainer(i, this.getLevel(), this.getBlockPos(), inventory);
    }
}
