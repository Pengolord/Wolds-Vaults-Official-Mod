package xyz.iwolfking.woldsvaults.config.recipes.augment;

import iskallia.vault.config.recipe.ForgeRecipeType;
import iskallia.vault.container.oversized.OverSizedItemStack;
import iskallia.vault.core.data.key.ThemeKey;
import iskallia.vault.gear.crafting.recipe.VaultForgeRecipe;
import iskallia.vault.item.AugmentItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.woldsvaults.data.discovery.ClientThemeDiscoveryData;
import xyz.iwolfking.woldsvaults.data.discovery.DiscoveredThemesData;
import xyz.iwolfking.woldsvaults.init.ModConfigs;

import java.util.List;
import java.util.Optional;

public class AugmentForgeRecipe extends VaultForgeRecipe {

    public AugmentForgeRecipe(ResourceLocation id, ItemStack output, List<ItemStack> inputs) {
        super(ForgeRecipeType.valueOf("AUGMENT"), id, output, inputs);
    }

    public AugmentForgeRecipe(Object o, Object o1) {
        super(ForgeRecipeType.valueOf("AUGMENT"), (ResourceLocation) o, (ItemStack) o1);
    }

    @Override
    public ItemStack createOutput(List<OverSizedItemStack> consumed, ServerPlayer crafter, int vaultLevel) {
        return super.createOutput(consumed, crafter, vaultLevel);
    }

    @Override
    public void addCraftingDisplayTooltip(ItemStack result, List<Component> out) {
        Optional<ThemeKey> themeKey = AugmentItem.getTheme(result);
        themeKey.ifPresent(key -> out.add(new TextComponent("Theme: ").append(new TextComponent(key.getName()).withStyle(Style.EMPTY.withColor(key.getColor())))));
        themeKey.ifPresent(key -> {
            if(ModConfigs.THEME_TOOLTIPS.tooltips.containsKey(key.getId()) && Screen.hasShiftDown()) {
                out.add(new TextComponent(ModConfigs.THEME_TOOLTIPS.tooltips.get(key.getId())));
            }
        });
    }

    @Override
    public boolean canCraft(Player player) {
        if (player instanceof ServerPlayer sPlayer) {
            DiscoveredThemesData themesData = DiscoveredThemesData.get(sPlayer.getLevel());
            return themesData.hasDiscovered(sPlayer, this.getId());
        } else {
            return ClientThemeDiscoveryData.getDiscoveredThemes().contains(this.getId());
        }
    }
}
