package xyz.iwolfking.woldsvaults.mixins.vaulthunters.custom;


import iskallia.vault.block.entity.DemagnetizerTileEntity;
import iskallia.vault.gear.attribute.type.VaultGearAttributeTypeMerger;
import iskallia.vault.gear.data.AttributeGearData;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.CuriosGearItem;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.gear.trinket.TrinketHelper;
import iskallia.vault.gear.trinket.effects.EnderAnchorTrinket;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.item.MagnetItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Optional;

@Mixin(value = MagnetItem.class, remap = false)
public abstract class MixinMagnetItem extends Item implements VaultGearItem, CuriosGearItem, ICurioItem {

    public MixinMagnetItem(Properties pProperties) {
        super(pProperties);
    }

    @Shadow
    public static Optional<ItemStack> getMagnet(LivingEntity entity) {
        return Optional.empty();
    }

    @Shadow
    public static void teleportToPlayer(Player player, List<? extends Entity> entities) {
    }

    @Shadow
    public static void moveToPlayer(Player player, List<? extends Entity> entities, float speed) {
    }


    /**
     * @author iwolfking
     * @reason Add Endergized Magnet Modifier
     */
    @Overwrite
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (event.player instanceof ServerPlayer player && player.level instanceof ServerLevel world) {
                getMagnet(event.player).ifPresent(stack -> {
                    if (stack.getItem() instanceof VaultGearItem gearItem && gearItem.isBroken(stack)) {
                        return;
                    }

                    if (!DemagnetizerTileEntity.hasDemagnetizerAround(event.player)) {
                        VaultGearData data = VaultGearData.read(stack);
                        float range = data.get(ModGearAttributes.RANGE, VaultGearAttributeTypeMerger.floatSum());
                        float speed = data.get(ModGearAttributes.VELOCITY, VaultGearAttributeTypeMerger.floatSum());
                        List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(range), (entity) -> {
                            return entity.distanceToSqr(player) <= range * range && !entity.getTags().contains("PreventMagnetMovement");
                        });
                        List<ExperienceOrb> orbs = world.getEntitiesOfClass(ExperienceOrb.class, player.getBoundingBox().inflate(range), (entity) -> {
                            return entity.distanceToSqr(player) <= range * range  && !entity.getTags().contains("PreventMagnetMovement");
                        });
                        TrinketHelper.getTrinkets(player, EnderAnchorTrinket.class).forEach((enderTrinket) -> {
                            if (enderTrinket.isUsable(player)) {
                                teleportToPlayer(player, items);
                                teleportToPlayer(player, orbs);
                            }
                        });
                        if(AttributeGearData.read(stack).get(xyz.iwolfking.woldsvaults.init.ModGearAttributes.MAGNET_ENDERGIZED, VaultGearAttributeTypeMerger.anyTrue())) {
                            teleportToPlayer(player, items);
                            teleportToPlayer(player, orbs);
                        }
                        moveToPlayer(player, items, speed);
                        moveToPlayer(player, orbs, speed);
                    }
                });
            }
        }
    }

}
