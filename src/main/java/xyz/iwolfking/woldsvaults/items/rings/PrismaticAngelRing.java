package xyz.iwolfking.woldsvaults.items.rings;

import dev.denismasterherobrine.angelring.config.Configuration;
import dev.denismasterherobrine.angelring.utils.ExternalMods;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ObjectHolder;
import xyz.iwolfking.woldsvaults.WoldsVaults;
import xyz.iwolfking.woldsvaults.items.rings.lib.PrismaticAngelRingInteraction;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(
        modid = WoldsVaults.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
@ObjectHolder("angelring")
public class PrismaticAngelRing extends AngelRingItem {
    public PrismaticAngelRing() {
        super();
        this.setRegistryName(WoldsVaults.id("prismatic_angel_ring"));
    }

    @SubscribeEvent
    public static void sendImc(InterModEnqueueEvent event) {
        if (ExternalMods.CURIOS.isLoaded()) {
            PrismaticAngelRingInteraction.sendImc();
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
        return ExternalMods.CURIOS.isLoaded() ? PrismaticAngelRingInteraction.initCapabilities() : super.initCapabilities(stack, unused);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (!Screen.hasShiftDown()) {
            tooltip.add((new TranslatableComponent("item.angelring.itemring.tooltip")).withStyle(ChatFormatting.GRAY));
        }

        if (Screen.hasShiftDown() && Configuration.XPCost.get() != 0) {
            tooltip.add(new TextComponent("Prismatic Angel Ring costs nothing to fly!").withStyle(ChatFormatting.GREEN));
        }

    }
}
