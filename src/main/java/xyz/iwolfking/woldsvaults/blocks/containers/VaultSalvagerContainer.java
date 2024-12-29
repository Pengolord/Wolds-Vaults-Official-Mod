package xyz.iwolfking.woldsvaults.blocks.containers;

import iskallia.vault.container.base.SimpleSidedContainer;
import iskallia.vault.container.oversized.OverSizedTabSlot;
import iskallia.vault.container.slot.TabSlot;
import iskallia.vault.container.spi.AbstractElementContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xyz.iwolfking.woldsvaults.blocks.tiles.VaultSalvagerTileEntity;
import xyz.iwolfking.woldsvaults.init.ModContainers;

public class VaultSalvagerContainer extends AbstractElementContainer {
    private final VaultSalvagerTileEntity tileEntity;
    private final BlockPos tilePos;

    public VaultSalvagerContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory) {
        super(ModContainers.VAULT_SALVAGER_CONTAINER, windowId, playerInventory.player);
        this.tilePos = pos;
        if (world.getBlockEntity(this.tilePos) instanceof VaultSalvagerTileEntity recyclerTileEntity) {
            this.tileEntity = recyclerTileEntity;
            this.initSlots(playerInventory);
        } else {
            this.tileEntity = null;
        }

    }

    private void initSlots(Inventory playerInventory) {
        int hotbarSlot;
        for(hotbarSlot = 0; hotbarSlot < 3; ++hotbarSlot) {
            for(int column = 0; column < 9; ++column) {
                this.addSlot(new TabSlot(playerInventory, column + hotbarSlot * 9 + 9, 8 + column * 18, 82 + hotbarSlot * 18));
            }
        }

        for(hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new TabSlot(playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 140));
        }

        SimpleSidedContainer ct = this.tileEntity.getInventory();
        this.addSlot(new Slot(ct, 0, 40, 50) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return tileEntity.isValidInput(stack);
            }
        });
        VaultSalvagerTileEntity.SalvagerInventory salvagerInventory = this.tileEntity.getInventory();
        for (int column = 1; column < 10; column++) {
            addSlot(new OverSizedTabSlot(salvagerInventory, column, 8 + (column-1) * 18, 22));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            if (index >= 0 && index < 36 && this.moveItemStackTo(slotStack, 36, 37, false)) {
                return itemstack;
            }

            if (index >= 0 && index < 27) {
                if (!this.moveItemStackTo(slotStack, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 27 && index < 36) {
                if (!this.moveItemStackTo(slotStack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }

        return itemstack;
    }

    public BlockPos getTilePos() {
        return this.tilePos;
    }

    public VaultSalvagerTileEntity getTileEntity() {
        return this.tileEntity;
    }

    public boolean stillValid(Player player) {
        return this.tileEntity != null && this.tileEntity.stillValid(this.player);
    }
}
