package xyz.iwolfking.woldsvaults.items;

import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.gear.attribute.VaultGearAttributeRegistry;
import iskallia.vault.gear.reader.VaultGearModifierReader;
import iskallia.vault.item.BasicItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ToolModifierNullifyingItem extends BasicItem {


    public ToolModifierNullifyingItem(ResourceLocation id, Properties properties) {
        super(id, properties);
    }

    @Nullable
    public static VaultGearAttribute<?> getModifierTag(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ToolModifierNullifyingItem) {
            String tagStr = stack.getOrCreateTag().getString("modifier");
            return VaultGearAttributeRegistry.getAttribute(ResourceLocation.tryParse(tagStr));
        } else {
            return null;
        }
    }

    public static void setModifierTag(ItemStack stack, String tag) {
        if (!stack.isEmpty() && stack.getItem() instanceof ToolModifierNullifyingItem) {
            stack.getOrCreateTag().putString("modifier", tag);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, worldIn, tooltip, flag);
            VaultGearAttribute<?> attr = getModifierTag(stack);
            if (attr != null) {
                    VaultGearModifierReader<?> reader = attr.getReader();
                    MutableComponent text = (new TextComponent("Combine with a Vault Tool in an Anvil to remove ")).withStyle(ChatFormatting.GRAY).append((new TextComponent(reader.getModifierName())).withStyle(reader.getColoredTextStyle()).append(new TextComponent(" from it.").withStyle(ChatFormatting.GRAY)));
                    tooltip.add(text);
            }
    }
}
