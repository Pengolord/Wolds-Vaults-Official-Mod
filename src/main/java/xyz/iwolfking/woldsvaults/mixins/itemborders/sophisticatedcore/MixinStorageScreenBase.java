package xyz.iwolfking.woldsvaults.mixins.itemborders.sophisticatedcore;

import com.anthonyhilyard.itemborders.ItemBorders;
import com.mojang.blaze3d.vertex.PoseStack;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.InventoryScrollPanel;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "itemborders"),
                @Condition(type = Condition.Type.MOD, value = "sophisticatedcore")
        }
)
@Mixin(value = StorageScreenBase.class)
public abstract class MixinStorageScreenBase<S extends StorageContainerMenuBase<?>> extends AbstractContainerScreen<S> implements InventoryScrollPanel.IInventoryScreen  {

    public MixinStorageScreenBase(S pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Inject(method = "renderSlot(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/inventory/Slot;)V", at = @At("HEAD"))
    protected void renderSlot(PoseStack matrixStack, Slot slot, CallbackInfo ci) {
        ItemBorders.renderBorder(matrixStack, slot);
    }
}
