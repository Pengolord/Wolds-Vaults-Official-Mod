package xyz.iwolfking.woldsvaults.mixins.itemborders.tomsstorage;

import com.anthonyhilyard.itemborders.ItemBorders;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tom.storagemod.gui.ContainerStorageTerminal;
import com.tom.storagemod.gui.GuiStorageTerminalBase;
import com.tom.storagemod.network.IDataReceiver;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "itemborders"),
                @Condition(type = Condition.Type.MOD, value = "toms_storage")
        }
)
@Mixin(value = GuiStorageTerminalBase.class, remap = false)
public abstract class MixinGuiStorageTerminalBase<T extends ContainerStorageTerminal> extends AbstractContainerScreen<T> implements IDataReceiver {

    public MixinGuiStorageTerminalBase(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Shadow public abstract Font getFont();

    @Inject(method = "renderItemInGui", at = @At("TAIL"))
    public final void renderItemInGuiBorder(PoseStack st, ItemStack stack, int x, int y, int mouseX, int mouseY, boolean hasBg, int color, boolean tooltip, String[] extraInfo, CallbackInfo ci) {
        ItemBorders.renderBorder(st, stack, x, y);
    }
}
