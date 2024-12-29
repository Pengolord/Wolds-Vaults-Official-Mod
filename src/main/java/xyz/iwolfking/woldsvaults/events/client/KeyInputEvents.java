package xyz.iwolfking.woldsvaults.events.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.iwolfking.woldsvaults.WoldsVaults;
import xyz.iwolfking.woldsvaults.init.client.ModKeybinds;

@Mod.EventBusSubscriber(modid = WoldsVaults.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyInputEvents {
    public static boolean isFeatherFixEnabled = true;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ModKeybinds.isFeatherFixed.consumeClick()) {
            isFeatherFixEnabled = !isFeatherFixEnabled;

            String state = isFeatherFixEnabled ? "ON" : "OFF";
            var player = Minecraft.getInstance().player;
            if (player != null){
                player.displayClientMessage(new TextComponent("Toggled Feather Fix: " + state).withStyle(ChatFormatting.YELLOW), true);
            }
        }
    }
}
