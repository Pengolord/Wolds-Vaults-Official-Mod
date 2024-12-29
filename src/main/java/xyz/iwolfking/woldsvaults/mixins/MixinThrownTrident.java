package xyz.iwolfking.woldsvaults.mixins;

import iskallia.vault.VaultMod;
import iskallia.vault.entity.entity.EffectCloudEntity;
import iskallia.vault.event.ActiveFlags;
import iskallia.vault.gear.attribute.type.VaultGearAttributeTypeMerger;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.snapshot.AttributeSnapshot;
import iskallia.vault.snapshot.AttributeSnapshotHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.iwolfking.woldsvaults.items.gear.VaultTridentItem;

import java.util.Random;

@Mixin(ThrownTrident.class)
public abstract class MixinThrownTrident extends AbstractArrow {
    @Shadow public int clientSideReturnTridentTickCount;
    @Shadow private ItemStack tridentItem;
    @Shadow private boolean dealtDamage;

    private static Random random = new Random();

    protected MixinThrownTrident(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickVaultTrident(CallbackInfo ci) {
        if(this.tridentItem != null && this.tridentItem.getItem() instanceof VaultTridentItem) {
            if (this.inGroundTime > 4) {
                this.dealtDamage = true;
            }

            Entity entity = this.getOwner();

            int i = VaultGearData.read(this.tridentItem).get(xyz.iwolfking.woldsvaults.init.ModGearAttributes.TRIDENT_LOYALTY, VaultGearAttributeTypeMerger.intSum());
            if (entity != null && i > 0 && (this.dealtDamage || this.isNoPhysics()) ) {
                if (!this.isAcceptibleReturnOwner()) {
                    if (!this.level.isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    }

                    this.discard();
                } else {
                    this.setNoPhysics(true);
                    Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                    this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * i, this.getZ());
                    if (this.level.isClientSide) {
                        this.yOld = this.getY();
                    }

                    double d0 = 0.05D * i;
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
                    if (this.clientSideReturnTridentTickCount == 0) {
                        this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                    }

                    ++this.clientSideReturnTridentTickCount;
                }
            }

            super.tick();
            ci.cancel();
        }

    }
    @Inject(method = "onHitEntity", at = @At("HEAD"), cancellable = true)
    private void onHitEntityWithVaultTrident(EntityHitResult pResult, CallbackInfo ci) {
        if(this.tridentItem.getItem() instanceof VaultTridentItem) {
            if(!(pResult.getEntity() instanceof LivingEntity)) {
                ci.cancel();
                return;
            }
            Entity entity = pResult.getEntity();
            VaultGearData data = VaultGearData.read(tridentItem);
            Double f = data.get(ModGearAttributes.ATTACK_DAMAGE, VaultGearAttributeTypeMerger.doubleSum());


            Entity entity1 = this.getOwner();
            if(entity1 instanceof Player player) {
                AttributeSnapshot snapshot = AttributeSnapshotHelper.getInstance().getSnapshot(player);
                MobType type = ((LivingEntity) entity).getMobType();
                float increasedDamage = 0.0F;
                if (!ActiveFlags.IS_AP_ATTACKING.isSet())
                       increasedDamage += snapshot.getAttributeValue(ModGearAttributes.DAMAGE_INCREASE, VaultGearAttributeTypeMerger.floatSum());
                if (type == MobType.UNDEAD) {
                    increasedDamage += snapshot.getAttributeValue(ModGearAttributes.DAMAGE_UNDEAD, VaultGearAttributeTypeMerger.floatSum());
                }
                if (type == MobType.ARTHROPOD) {
                    increasedDamage += snapshot.getAttributeValue(ModGearAttributes.DAMAGE_SPIDERS, VaultGearAttributeTypeMerger.floatSum());
                }
                if (type == MobType.ILLAGER) {
                    increasedDamage += snapshot.getAttributeValue(ModGearAttributes.DAMAGE_ILLAGERS, VaultGearAttributeTypeMerger.floatSum());
                }
                if (ModConfigs.ENTITY_GROUPS.isInGroup(VaultMod.id("mob_type/nether"), entity)) {
                    increasedDamage += snapshot.getAttributeValue(ModGearAttributes.DAMAGE_NETHER, VaultGearAttributeTypeMerger.floatSum());
                }
                if (ModConfigs.ENTITY_GROUPS.isInGroup(VaultMod.id("mob_type/champion"), entity)) {
                    increasedDamage += snapshot.getAttributeValue(ModGearAttributes.DAMAGE_CHAMPION, VaultGearAttributeTypeMerger.floatSum());
                }

                if (ModConfigs.ENTITY_GROUPS.isInGroup(VaultMod.id("mob_type/dungeon"), entity)) {
                    increasedDamage += snapshot.getAttributeValue(ModGearAttributes.DAMAGE_DUNGEON, VaultGearAttributeTypeMerger.floatSum());
                }

                if (ModConfigs.ENTITY_GROUPS.isInGroup(VaultMod.id("mob_type/tank"), entity)) {
                    increasedDamage += snapshot.getAttributeValue(ModGearAttributes.DAMAGE_TANK, VaultGearAttributeTypeMerger.floatSum());
                }

                if (ModConfigs.ENTITY_GROUPS.isInGroup(VaultMod.id("mob_type/horde"), entity)) {
                    increasedDamage += snapshot.getAttributeValue(ModGearAttributes.DAMAGE_HORDE, VaultGearAttributeTypeMerger.floatSum());
                }

                if (ModConfigs.ENTITY_GROUPS.isInGroup(VaultMod.id("mob_type/assassin"), entity)) {
                    increasedDamage += snapshot.getAttributeValue(ModGearAttributes.DAMAGE_ASSASSIN, VaultGearAttributeTypeMerger.floatSum());
                }

                if (ModConfigs.ENTITY_GROUPS.isInGroup(VaultMod.id("mob_type/dweller"), entity)) {
                    increasedDamage += snapshot.getAttributeValue(ModGearAttributes.DAMAGE_DWELLER, VaultGearAttributeTypeMerger.floatSum());
                }
                f += (f * (1.0F + increasedDamage));
            }


            DamageSource damagesource = DamageSource.trident(this, entity1 == null ? this : entity1);
            this.dealtDamage = true;
            SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
            if (entity.hurt(damagesource, f.floatValue())) {

                if (entity instanceof LivingEntity livingentity) {
                    if (entity1 instanceof LivingEntity livingentity1) {
                        EnchantmentHelper.doPostHurtEffects(livingentity, livingentity1);
                        EnchantmentHelper.doPostDamageEffects(livingentity1, livingentity);
                    }

                    this.doPostHurtEffects(livingentity);
                }
            }

            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
            float f1 = 1.0F;
            if (this.level instanceof ServerLevel && this.isVaultTridentChanneling()) {
                float channelChance = data.get(xyz.iwolfking.woldsvaults.init.ModGearAttributes.CHANNELING_CHANCE, VaultGearAttributeTypeMerger.floatSum());
                if(channelChance <= random.nextFloat() || channelChance == 0.0) {
                    ci.cancel();
                    return;
                }
                BlockPos blockpos = entity.blockPosition();
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level);
                if(lightningbolt != null) {
                    lightningbolt.setDamage(f.floatValue());
                }
                lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                lightningbolt.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer)entity1 : null);
                this.level.addFreshEntity(lightningbolt);
                soundevent = SoundEvents.TRIDENT_THUNDER;
                f1 = 5.0F;
                LivingEntity attacker = (LivingEntity) this.getOwner();

                AttributeSnapshot snapshot = AttributeSnapshotHelper.getInstance().getSnapshot(attacker);
                (snapshot.getAttributeValue(ModGearAttributes.EFFECT_CLOUD, VaultGearAttributeTypeMerger.asList())).forEach(cloud -> {

                    MobEffect effect = cloud.getPrimaryEffect();
                    if (effect == null) {
                        return;
                    }
                    EffectCloudEntity cloudEntity = new EffectCloudEntity(attacker.getLevel(), blockpos.getX(), blockpos.getY(), blockpos.getZ());
                    cloud.apply(cloudEntity);
                    cloudEntity.setOwner(attacker);
                    attacker.getLevel().addFreshEntity(cloudEntity);
                }
            );
            }

            this.playSound(soundevent, f1, 1.0F);
            ci.cancel();
        }
    }

    private boolean isVaultTridentChanneling() {
        VaultGearData data = VaultGearData.read(this.tridentItem);
        return data.get(xyz.iwolfking.woldsvaults.init.ModGearAttributes.TRIDENT_CHANNELING, VaultGearAttributeTypeMerger.anyTrue());
    }

    @Shadow protected abstract boolean isAcceptibleReturnOwner();
}
