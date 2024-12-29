package xyz.iwolfking.woldsvaults.mixins.ae2.terminal;


import appeng.api.implementations.menuobjects.IMenuItem;
import appeng.api.implementations.menuobjects.ItemMenuHost;
import appeng.core.AELog;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import top.theillusivec4.curios.api.CuriosApi;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "ae2")
        }
)
@Mixin(targets = "appeng.menu.locator.MenuItemLocator", remap = false)
public class MixinMenuItemLocator {
    @Shadow @Final private int itemIndex;
    @Shadow @Final private @Nullable BlockPos blockPos;

    /**
     * @author iwolfking
     * @reason Add Curios support to AE wireless terminals
     */
    @Overwrite
    public <T> @Nullable T locate(Player player, Class<T> hostInterface) {
        IItemHandlerModifiable handler = CuriosApi.getCuriosHelper().getEquippedCurios(player).resolve().get();
        ItemStack curioStack = handler.getStackInSlot(itemIndex);
        if(!curioStack.isEmpty()) {
            Item var5 = curioStack.getItem();
            if (var5 instanceof IMenuItem guiItem) {
                ItemMenuHost menuHost = guiItem.getMenuHost(player, this.itemIndex, curioStack, this.blockPos);
                if (hostInterface.isInstance(menuHost)) {
                    return hostInterface.cast(menuHost);
                }

                if (menuHost != null) {
                    AELog.warn("Item in slot %d of %s did not create a compatible menu of type %s: %s", this.itemIndex, player, hostInterface, menuHost);
                }

                return null;
            }
        }

        ItemStack it = player.getInventory().getItem(this.itemIndex);
        if (!it.isEmpty()) {
            if (it.getItem() instanceof IMenuItem guiItem) {
                ItemMenuHost menuHost = guiItem.getMenuHost(player, this.itemIndex, it, this.blockPos);
                if (hostInterface.isInstance(menuHost)) {
                    return hostInterface.cast(menuHost);
                }

                if (menuHost != null) {
                    AELog.warn("Item in slot %d of %s did not create a compatible menu of type %s: %s", this.itemIndex, player, hostInterface, menuHost);
                }

                return null;
            }
        }


        AELog.warn("Item in slot %d of %s is not an IMenuItem: %s", this.itemIndex, player, it);
        return null;
    }
}
