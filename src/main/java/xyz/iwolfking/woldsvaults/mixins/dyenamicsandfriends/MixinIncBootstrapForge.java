package xyz.iwolfking.woldsvaults.mixins.dyenamicsandfriends;

import agency.highlysuspect.incorporeal.Inc;
import agency.highlysuspect.incorporeal.IncBlockEntityTypes;
import agency.highlysuspect.incorporeal.IncItems;
import agency.highlysuspect.incorporeal.block.entity.EnderSoulCoreBlockEntity;
import agency.highlysuspect.incorporeal.platform.IncBootstrapper;
import agency.highlysuspect.incorporeal.platform.forge.IncBootstrapForge;
import agency.highlysuspect.incorporeal.platform.forge.block.entity.EnderSoulCoreItemHandler;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.forge.CapabilityUtil;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "incorporeal")
        }
)
@Mixin(value = IncBootstrapForge.class, remap = false)
public abstract class MixinIncBootstrapForge implements IncBootstrapper {
    /**
     * @author iwolfking
     * @reason Fixes a client-side crash interaction with Dyenamics and Friends
     */
    @Overwrite
    public void registerCapabilities() {
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, e -> {
            AttachCapabilitiesEvent<?> event = (AttachCapabilitiesEvent<?>) e;
            BlockEntity be = (BlockEntity) event.getObject();

            if (be.getType() != null && IncBlockEntityTypes.SELF_MANA_RECEIVER_BLOCK_ENTITY_TYPES.contains(be.getType()) && be instanceof IManaReceiver receiver) {
                event.addCapability(Inc.id("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, receiver));
            }

            if (be.getType() != null &&IncBlockEntityTypes.SELF_WANDABLE_BLOCK_ENTITY_TYPES.contains(be.getType()) && be instanceof IWandable wandable) {
                event.addCapability(Inc.id("wandable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.WANDABLE, wandable));
            }

            if (be.getType() != null && be.getType() == IncBlockEntityTypes.ENDER_SOUL_CORE && be instanceof EnderSoulCoreBlockEntity esc) {
                event.addCapability(Inc.id("inventory"), CapabilityUtil.makeProvider(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, new EnderSoulCoreItemHandler(esc)));
            }

        });
        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, e -> {
            AttachCapabilitiesEvent<?> event = (AttachCapabilitiesEvent<?>) e;
            ItemStack stack = (ItemStack) event.getObject();
            Item item = stack.getItem();
            if (IncItems.COORD_BOUND_ITEM_MAKERS.containsKey(item)) {
                event.addCapability(Inc.id("coord_bound_item"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.COORD_BOUND_ITEM, IncItems.COORD_BOUND_ITEM_MAKERS.get(item).apply(stack)));
            }

            if (IncItems.MANA_ITEM_MAKERS.containsKey(item)) {
               event.addCapability(Inc.id("mana_item"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_ITEM, IncItems.MANA_ITEM_MAKERS.get(item).apply(stack)));
            }

        });
    }
}
