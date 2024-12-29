package xyz.iwolfking.woldsvaults.mixins.ensorcellation;

import cofh.ensorcellation.enchantment.VitalityEnchantment;
import cofh.ensorcellation.event.CommonEvents;
import cofh.ensorcellation.init.EnsorcEnchantments;
import cofh.lib.util.Constants;
import cofh.lib.util.Utils;
import iskallia.vault.block.VaultChestBlock;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "ensorcellation")
        }
)
@Mixin(value = CommonEvents.class, remap = false)
public class MixinCommonEvents {


    /**
     * @author iwolfking
     * @reason Cap vitality.
     */
    @Overwrite
    @SubscribeEvent
    public static void handleLivingEquipmentChangeEvent(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntityLiving();

        int encVitality = Utils.getMaxEquippedEnchantmentLevel(entity, EnsorcEnchantments.VITALITY.get());
        AttributeInstance healthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttr != null) {
            healthAttr.removeModifier(Constants.UUID_ENCH_VITALITY_HEALTH);
            if (encVitality > 0) {
                healthAttr.addTransientModifier(new AttributeModifier(Constants.UUID_ENCH_VITALITY_HEALTH, "vitality", (Math.min(encVitality, 3) * VitalityEnchantment.health), AttributeModifier.Operation.ADDITION));
            }
        }

    }

    /**
     * @author iwolfking
     * @reason Disable XP Boost
     */
    @Overwrite
    @SubscribeEvent
    public static void handleLivingExperienceDropEvent(LivingExperienceDropEvent event) {
        if (!event.isCanceled()) {
            Player player = event.getAttackingPlayer();
            if (player != null) {
                int encFool = Utils.getMaxEquippedEnchantmentLevel(player, EnsorcEnchantments.CURSE_FOOL.get());
                if (encFool > 0) {
                    event.setDroppedExperience(0);
                    event.setCanceled(true);
                }
            }

        }
    }

    /**
     * @author iwolfking
     * @reason Fix air affinity
     */
    @Overwrite
    @SubscribeEvent
    public static void handleBreakSpeedEvent(PlayerEvent.BreakSpeed event) {
        if (!event.isCanceled()) {
            Player player = event.getPlayer();
            int encAirAffinity = Utils.getMaxEquippedEnchantmentLevel(player, EnsorcEnchantments.AIR_AFFINITY.get());
            if (encAirAffinity > 0 && !player.isOnGround()) {
                if(event.getState().getBlock() instanceof VaultChestBlock) {
                    return;
                }
                event.setNewSpeed(Math.max(event.getNewSpeed(), event.getOriginalSpeed() * 5.0F));
            }

        }
    }
}
