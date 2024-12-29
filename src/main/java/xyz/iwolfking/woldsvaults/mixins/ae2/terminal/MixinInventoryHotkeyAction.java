package xyz.iwolfking.woldsvaults.mixins.ae2.terminal;

import appeng.hotkeys.InventoryHotkeyAction;
import appeng.items.tools.powered.WirelessCraftingTerminalItem;
import appeng.items.tools.powered.WirelessTerminalItem;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Predicate;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "ae2")
        }
)
@Mixin(value = InventoryHotkeyAction.class, remap = false)
public abstract class MixinInventoryHotkeyAction {
    @Shadow @Final private Predicate<ItemStack> locatable;
    @Shadow @Final private InventoryHotkeyAction.Opener opener;

    /**
     * @author iwolfking
     * @reason  Add Curio support to Wireless AE Terminal
     */
    @Overwrite
    public boolean run(Player player) {
        NonNullList<ItemStack> items = player.getInventory().items;

        for (int i = 0; i < items.size(); ++i) {
            if (this.locatable.test(items.get(i)) && this.opener.open(player, i)) {
                return true;
            }
        }

        IItemHandlerModifiable handler = CuriosApi.getCuriosHelper().getEquippedCurios(player).resolve().get();

        for(int i =0; i < handler.getSlots(); i++) {
            ItemStack curioStack = handler.getStackInSlot(i);
            if(curioStack.getItem() instanceof WirelessTerminalItem || curioStack.getItem() instanceof WirelessCraftingTerminalItem) {
                if(opener.open(player, i)) {
                    return true;
                }
            }
        }

        return false;
    }
}
