package xyz.iwolfking.woldsvaults.mixins.vaulthunters.custom;

import iskallia.vault.core.data.key.ThemeKey;
import iskallia.vault.core.vault.VaultRegistry;
import iskallia.vault.item.crystal.theme.ValueCrystalTheme;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xyz.iwolfking.woldsvaults.init.ModConfigs;

import java.util.List;
import java.util.Optional;

@Mixin(value = ValueCrystalTheme.class, remap = false)
public abstract class MixinCrystalTheme {
    @Shadow private ResourceLocation id;

    @Shadow public abstract Optional<Integer> getColor();

    /**
     * @author iwolfking
     * @reason Add theme description on shift
     */
    @Overwrite
    public void addText(List<Component> tooltip, int minIndex, TooltipFlag flag, float time) {
        ThemeKey theme = VaultRegistry.THEME.getKey(this.id);
        if (theme == null) {
            tooltip.add((new TextComponent("Theme: ")).append((new TextComponent("Unknown")).withStyle(ChatFormatting.RED)));
        } else {
            tooltip.add(new TextComponent("Theme: ").append(new TextComponent(theme.getName()).withStyle(Style.EMPTY.withColor(this.getColor().orElseThrow()))));
            if(Screen.hasShiftDown()) {
                if(ModConfigs.THEME_TOOLTIPS.tooltips.containsKey(theme.getId()) && Screen.hasShiftDown()) {
                    tooltip.add(new TextComponent(ModConfigs.THEME_TOOLTIPS.tooltips.get(theme.getId())));
                }
            }
        }
    }
}
