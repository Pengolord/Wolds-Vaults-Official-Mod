package xyz.iwolfking.woldsvaults.mixins.vaulthunters.custom;

import com.llamalad7.mixinextras.sugar.Local;
import iskallia.vault.block.entity.CrystalWorkbenchTileEntity;
import iskallia.vault.container.CrystalWorkbenchContainer;
import iskallia.vault.container.oversized.OverSizedSlotContainer;
import iskallia.vault.container.slot.TabSlot;
import iskallia.vault.item.InfusedCatalystItem;
import iskallia.vault.item.InscriptionItem;
import iskallia.vault.item.gear.CharmItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.iwolfking.woldsvaults.items.LayoutModificationItem;

@Mixin(value = CrystalWorkbenchContainer.class, remap = false)
public abstract class MixinCrystalWorkbenchContainer  extends OverSizedSlotContainer {

    @Shadow @Final private CrystalWorkbenchTileEntity entity;

    protected MixinCrystalWorkbenchContainer(MenuType<?> menuType, int id, Player player) {
        super(menuType, id, player);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Liskallia/vault/container/CrystalWorkbenchContainer;addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;", ordinal = 3, remap = true))
    private Slot addManipulator(CrystalWorkbenchContainer instance, Slot slot, @Local(ordinal = 1) int slotIndex, @Local(ordinal = 0) int finalSlotIndex) {
        return this.addSlot(new TabSlot(this.entity.getIngredients(), slotIndex * 3 + finalSlotIndex, -999 + finalSlotIndex * 18, 50 + slotIndex * 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof InfusedCatalystItem || stack.getItem() instanceof InscriptionItem || stack.getItem() instanceof CharmItem || stack.getItem() instanceof LayoutModificationItem;
            }
        });
    }


}
