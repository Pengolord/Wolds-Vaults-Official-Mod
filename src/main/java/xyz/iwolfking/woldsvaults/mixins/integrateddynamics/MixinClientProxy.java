package xyz.iwolfking.woldsvaults.mixins.integrateddynamics;

import iskallia.vault.core.vault.ClientVaults;
import iskallia.vault.world.data.ServerVaults;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.ClientRegistry;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.inventory.ItemLocation;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;
import org.cyclops.integratedterminals.item.ItemTerminalStoragePortable;
import org.cyclops.integratedterminals.network.packet.TerminalStorageIngredientItemOpenGenericPacket;
import org.cyclops.integratedterminals.proxy.ClientProxy;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "integrateddynamics")
        }
)
@Mixin(value = ClientProxy.class, remap = false)
public abstract class MixinClientProxy extends ClientProxyComponent {
    @Shadow @Final public static KeyMapping TERMINAL_TAB_NEXT;
    @Shadow @Final public static KeyMapping TERMINAL_TAB_PREVIOUS;
    @Shadow @Final public static KeyMapping TERMINAL_CRAFTINGGRID_CLEARPLAYER;
    @Shadow @Final public static KeyMapping TERMINAL_CRAFTINGGRID_CLEARSTORAGE;
    @Shadow @Final public static KeyMapping TERMINAL_CRAFTINGGRID_BALANCE;
    @Shadow @Final public static KeyMapping TERMINAL_STORAGE_PORTABLE_OPEN;

    public MixinClientProxy(CommonProxyComponent commonProxyComponent) {
        super(commonProxyComponent);
    }

    /**
     * @author iwolfking
     * @reason Disable ID Terminal Keybind in Vaults
     */
    @Overwrite @Override
    public void registerKeyBindings(IKeyRegistry keyRegistry) {
        ClientRegistry.registerKeyBinding(TERMINAL_TAB_NEXT);
        ClientRegistry.registerKeyBinding(TERMINAL_TAB_PREVIOUS);
        ClientRegistry.registerKeyBinding(TERMINAL_CRAFTINGGRID_CLEARPLAYER);
        ClientRegistry.registerKeyBinding(TERMINAL_CRAFTINGGRID_CLEARSTORAGE);
        ClientRegistry.registerKeyBinding(TERMINAL_CRAFTINGGRID_BALANCE);
        ClientRegistry.registerKeyBinding(TERMINAL_STORAGE_PORTABLE_OPEN);
        keyRegistry.addKeyHandler(TERMINAL_STORAGE_PORTABLE_OPEN, (kb) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if(ClientVaults.getActive().isPresent() || ServerVaults.get(player.getLevel()).isPresent()) {
                return;
            }
            ItemLocation found = null;
            PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);

            while (it.hasNext() && found == null) {
                ItemLocation pair = it.nextIndexed();
                if (pair.getItemStack(player).getItem() instanceof ItemTerminalStoragePortable) {
                    found = pair;
                }
            }

            if (found != null) {
                TerminalStorageIngredientItemOpenGenericPacket.send(found);
            }

        });
    }
}
