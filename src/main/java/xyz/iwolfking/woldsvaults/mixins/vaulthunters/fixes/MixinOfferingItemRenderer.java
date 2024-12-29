package xyz.iwolfking.woldsvaults.mixins.vaulthunters.fixes;

import iskallia.vault.item.OfferingItem;
import iskallia.vault.item.OfferingItemRenderer;
import iskallia.vault.item.tool.SpecialItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = OfferingItemRenderer.class)
public abstract class MixinOfferingItemRenderer extends SpecialItemRenderer {

    @Redirect(method = "renderByItem", at = @At(value = "INVOKE", target = "Liskallia/vault/item/OfferingItem;getModifier(Lnet/minecraft/world/item/ItemStack;)Ljava/lang/String;", remap = false))
    public String renderByItem(ItemStack tag) {
        String modifier = OfferingItem.getModifier(tag);
        if(modifier.isEmpty()) {
            return "none";
        }

        return modifier;
    }
}
