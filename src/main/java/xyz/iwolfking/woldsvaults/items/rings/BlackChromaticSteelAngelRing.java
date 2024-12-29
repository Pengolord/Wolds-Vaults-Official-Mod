package xyz.iwolfking.woldsvaults.items.rings;

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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.Nullable;
import xyz.iwolfking.woldsvaults.WoldsVaults;
import xyz.iwolfking.woldsvaults.items.rings.lib.BlackChromaticSteelAngelRingInteraction;
import xyz.iwolfking.woldsvaults.items.rings.lib.ChromaticIronAngelRingInteraction;

import java.util.List;

@Mod.EventBusSubscriber(
        modid = WoldsVaults.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
@ObjectHolder("angelring")
public class BlackChromaticSteelAngelRing extends AngelRingItem {
    public BlackChromaticSteelAngelRing() {
        this.setRegistryName(WoldsVaults.id("black_chromatic_steel_angel_ring"));
    }

    @SubscribeEvent
    public static void sendImc(InterModEnqueueEvent event) {
        if (ExternalMods.CURIOS.isLoaded()) {
            ChromaticIronAngelRingInteraction.sendImc();
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
        return ExternalMods.CURIOS.isLoaded() ? BlackChromaticSteelAngelRingInteraction.initCapabilities(stack) : super.initCapabilities(stack, unused);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        return energy.getEnergyStored() < energy.getMaxEnergyStored();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY, null).map(e -> Math.min(13 * e.getEnergyStored() / e.getMaxEnergyStored(), 13)).orElse(0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag tooltipFlag) {
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        if (!Screen.hasShiftDown()) {
            tooltip.add((new TranslatableComponent("item.angelring.energetic_angel_ring.tooltip")).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add((new TranslatableComponent("item.angelring.energetic_angel_ring.desc0")).withStyle(ChatFormatting.GOLD));
            tooltip.add(new TextComponent("Black Chromatic Steel is more efficient and uses less energy to fly.").withStyle(ChatFormatting.GREEN));
            tooltip.add((new TranslatableComponent("item.angelring.energetic_angel_ring.desc1", energy.getEnergyStored(), energy.getMaxEnergyStored())).withStyle(ChatFormatting.GRAY));
        }

    }
}
