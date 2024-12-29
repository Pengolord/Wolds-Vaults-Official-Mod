package xyz.iwolfking.woldsvaults.mixins.advanced_peripherals;

import de.srendi.advancedperipherals.common.setup.Blocks;
import de.srendi.advancedperipherals.common.setup.Items;
import de.srendi.advancedperipherals.common.setup.Villagers;
import de.srendi.advancedperipherals.common.village.VillagerTrade;
import de.srendi.advancedperipherals.common.village.VillagerTrades;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "advanced_peripherals")
        }
)
@Mixin(value = VillagerTrades.class, remap = false)
public class MixinVillagerTrades {
    /**
     * @author iwolfking
     * @reason Purge Advanced Peripherals wandering trader trades
     */
    @Overwrite
    public static void registerWanderingTrade(WandererTradesEvent event) {
    }

    /**
     * @author iwolfking
     * @reason Edit Advanced Peripherals villager trades
     */
    @Overwrite
    public static void registerVillagerTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<net.minecraft.world.entity.npc.VillagerTrades.ItemListing>> trades = event.getTrades();
        if (event.getType() == Villagers.COMPUTER_SCIENTIST.get()) {
            trades.get(1).add((new VillagerTrade(VillagerTrade.Type.ITEM_FOR_EMERALD)).setItem(Blocks.CHAT_BOX.get()).setEmeraldPrice(2).setMaxUses(8).setXp(4));
            trades.get(1).add((new VillagerTrade(VillagerTrade.Type.EMERALD_FOR_ITEM)).setItem(Blocks.PERIPHERAL_CASING.get()).setEmeraldPrice(1).setMaxUses(8).setXp(2));
            trades.get(2).add((new VillagerTrade(VillagerTrade.Type.ITEM_FOR_EMERALD)).setItem(Blocks.REDSTONE_INTEGRATOR.get()).setEmeraldPrice(2).setMaxUses(2).setXp(8));
            trades.get(3).add((new VillagerTrade(VillagerTrade.Type.ITEM_FOR_EMERALD)).setItem(Items.COMPUTER_TOOL.get()).setEmeraldPrice(1).setMaxUses(4).setXp(8));
            trades.get(4).add((new VillagerTrade(VillagerTrade.Type.ITEM_FOR_EMERALD)).setItem(Blocks.ENERGY_DETECTOR.get()).setEmeraldPrice(3).setMaxUses(8).setXp(12));
            trades.get(4).add((new VillagerTrade(VillagerTrade.Type.ITEM_FOR_EMERALD)).setItem(Items.MEMORY_CARD.get()).setEmeraldPrice(1).setMaxUses(8).setXp(18));
        }

    }
}
