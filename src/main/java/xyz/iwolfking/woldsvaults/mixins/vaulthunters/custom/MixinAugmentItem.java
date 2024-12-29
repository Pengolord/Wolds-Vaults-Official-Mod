package xyz.iwolfking.woldsvaults.mixins.vaulthunters.custom;

import iskallia.vault.core.data.key.ThemeKey;
import iskallia.vault.item.AugmentItem;
import iskallia.vault.item.gear.DataTransferItem;
import iskallia.vault.item.gear.VaultLevelItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.iwolfking.woldsvaults.init.ModConfigs;

import java.util.List;
import java.util.Optional;

@Mixin(value = AugmentItem.class, remap = false)
public abstract class MixinAugmentItem extends Item implements VaultLevelItem, DataTransferItem {

    public MixinAugmentItem(Properties pProperties) {
        super(pProperties);
    }

    @Shadow
    public static Optional<ThemeKey> getTheme(ItemStack stack) {
        return null;
    }

    /**
     * @author iwolfking
     * @reason Add Theme tooltips
     */
    @Inject(method = "appendHoverText", at = @At("TAIL"), remap = true)
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag advanced, CallbackInfo ci) {
        Optional<ThemeKey> theme = getTheme(stack);
        if(theme == null)
            return;
        theme.ifPresent(key -> {
            if(ModConfigs.THEME_TOOLTIPS.tooltips.containsKey(key.getId()) && Screen.hasShiftDown()) {
                tooltip.add(new TextComponent(ModConfigs.THEME_TOOLTIPS.tooltips.get(key.getId())));
            }
        });
    }
}
