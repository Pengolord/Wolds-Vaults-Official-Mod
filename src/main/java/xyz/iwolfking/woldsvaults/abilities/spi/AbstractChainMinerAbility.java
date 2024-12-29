package xyz.iwolfking.woldsvaults.abilities.spi;

import com.google.gson.JsonObject;
import iskallia.vault.core.data.adapter.Adapters;
import iskallia.vault.core.net.BitBuffer;
import iskallia.vault.event.ActiveFlags;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.item.tool.ToolItem;
import iskallia.vault.skill.ability.effect.spi.core.Ability;
import iskallia.vault.skill.ability.effect.spi.core.HoldAbility;
import iskallia.vault.skill.tree.AbilityTree;
import iskallia.vault.util.calc.AreaOfEffectHelper;
import iskallia.vault.world.data.PlayerAbilitiesData;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.iwolfking.woldsvaults.WoldsVaults;
import xyz.iwolfking.woldsvaults.util.ChainBreakHandler;

@EventBusSubscriber(
        modid = WoldsVaults.MOD_ID,
        bus = Bus.FORGE
)
public abstract class AbstractChainMinerAbility extends HoldAbility {
    private final ChainBreakHandler chainBreakHandler = new ChainBreakHandler() {
        @Override
        protected int getBlockLimit(Player player) {
            return AbstractChainMinerAbility.this.getBlockLimit(player);
        }

        @Override
        protected int getRange() {
            return AbstractChainMinerAbility.this.getRange();
        }

        @Override
        protected ItemStack getMiningItemProxy(Player player) {
            return AbstractChainMinerAbility.this.getVeinMiningItemProxy(player);
        }

        @Override
        protected boolean shouldVoid(ServerLevel level, ServerPlayer player, BlockState blockState) {
            return AbstractChainMinerAbility.this.shouldVoid(level, player, blockState);
        }
    };
    private int blockLimit;
    private int range;

    protected AbstractChainMinerAbility() {
    }

    public AbstractChainMinerAbility(int unlockLevel, int learnPointCost, int regretPointCost, int cooldownTicks, int blockLimit, int range) {
        super(unlockLevel, learnPointCost, regretPointCost, cooldownTicks);
        this.blockLimit = blockLimit;
        this.range = range;
    }

    public int getUnmodifiedBlockLimit() {
        return this.blockLimit;
    }

    public int getBlockLimit(Player player) {
        int blocks = this.getUnmodifiedBlockLimit();
        return AreaOfEffectHelper.adjustAreaOfEffectRound(player, this, blocks);
    }

    public int getRange() {
        return this.range;
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void onBlockMined(BreakEvent event) {
        if (!event.getWorld().isClientSide()
                && !(event.getPlayer() instanceof FakePlayer)
                && event.getPlayer() instanceof ServerPlayer player
                && event.getWorld() instanceof ServerLevel level) {
            if (event.getPlayer().getMainHandItem().getItem() instanceof ToolItem) {
                VaultGearData gearData = VaultGearData.read(event.getPlayer().getMainHandItem());
                if (gearData.hasAttribute(ModGearAttributes.HAMMERING)) {
                    return;
                }
            }

            AbilityTree abilities = PlayerAbilitiesData.get(level).getAbilities(player);

            for (AbstractChainMinerAbility ability : abilities.getAll(AbstractChainMinerAbility.class, Ability::isActive)) {
                if (!ability.isItemDenied(player.getItemInHand(InteractionHand.MAIN_HAND))) {
                    abilities.getSelectedAbility().ifPresent(selected -> {
                        if (selected.getClass() == ability.getClass()) {
                            ActiveFlags.IS_AOE_MINING.runIfNotSet(() -> {
                                BlockPos pos = event.getPos();
                                BlockState blockState = level.getBlockState(pos);
                                if (ability.chainBreakHandler.areaDig(level, player, pos, blockState.getBlock())) {
                                    event.setCanceled(true);
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    private boolean isItemDenied(ItemStack itemStack) {
        return ModConfigs.ABILITIES_VEIN_MINER_DENY_CONFIG.isItemDenied(itemStack);
    }

    protected abstract ItemStack getVeinMiningItemProxy(Player var1);

    public abstract boolean shouldVoid(ServerLevel var1, ServerPlayer var2, BlockState var3);

    @Override
    public void writeBits(BitBuffer buffer) {
        super.writeBits(buffer);
        Adapters.INT_SEGMENTED_7.writeBits(Integer.valueOf(this.blockLimit), buffer);
        Adapters.INT_SEGMENTED_7.writeBits(Integer.valueOf(this.range), buffer);
    }

    @Override
    public void readBits(BitBuffer buffer) {
        super.readBits(buffer);
        this.blockLimit = Adapters.INT_SEGMENTED_7.readBits(buffer).orElseThrow();
        this.range = Adapters.INT_SEGMENTED_7.readBits(buffer).orElseThrow();
    }

    @Override
    public Optional<CompoundTag> writeNbt() {
        return super.writeNbt().map(nbt -> {
            Adapters.INT.writeNbt(Integer.valueOf(this.blockLimit)).ifPresent(tag -> nbt.put("blockLimit", tag));
            Adapters.INT.writeNbt(Integer.valueOf(this.range)).ifPresent(tag -> nbt.put("range", tag));
            return (CompoundTag)nbt;
        });
    }

    @Override
    public void readNbt(CompoundTag nbt) {
        super.readNbt(nbt);
        this.blockLimit = Adapters.INT.readNbt(nbt.get("blockLimit")).orElse(0);
        this.range = Adapters.INT.readNbt(nbt.get("range")).orElse(1);
    }

    @Override
    public Optional<JsonObject> writeJson() {
        return super.writeJson().map(json -> {
            Adapters.INT.writeJson(Integer.valueOf(this.blockLimit)).ifPresent(element -> json.add("blockLimit", element));
            Adapters.INT.writeJson(Integer.valueOf(this.range)).ifPresent(element -> json.add("range", element));
            return (JsonObject)json;
        });
    }

    @Override
    public void readJson(JsonObject json) {
        super.readJson(json);
        this.blockLimit = Adapters.INT.readJson(json.get("blockLimit")).orElse(0);
        this.range = Adapters.INT.readJson(json.get("range")).orElse(1);
    }
}