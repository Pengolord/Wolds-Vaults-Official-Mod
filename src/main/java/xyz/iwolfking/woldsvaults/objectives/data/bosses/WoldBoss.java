package xyz.iwolfking.woldsvaults.objectives.data.bosses;

import iskallia.vault.entity.VaultBoss;
import iskallia.vault.entity.ai.AOEGoal;
import iskallia.vault.entity.ai.RegenAfterAWhile;
import iskallia.vault.entity.ai.TeleportGoal;
import iskallia.vault.entity.ai.TeleportRandomly;
import iskallia.vault.entity.ai.ThrowProjectilesGoal;
import iskallia.vault.entity.entity.EternalEntity;
import iskallia.vault.init.ModSounds;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class WoldBoss extends Zombie implements VaultBoss {

    public TeleportRandomly<WoldBoss> teleportTask = new TeleportRandomly(this, (entity, source, amount) -> {
        return !(source.getEntity() instanceof LivingEntity) ? 0.2 : 0.0;
    });
    public final ServerBossEvent bossInfo;
    public RegenAfterAWhile<WoldBoss> regenAfterAWhile;

    public WoldBoss(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);
        this.regenAfterAWhile = new RegenAfterAWhile<>(this);
    }

    @Override
    protected void dropFromLootTable(DamageSource damageSource, boolean attackedRecently) {
    }

    @Override
    protected void doUnderWaterConversion() {
    }

    @Override
    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        this.goalSelector.addGoal(1, TeleportGoal.builder(this).start((entity) -> {
            return entity.getTarget() != null && entity.tickCount % 60 == 0;
        }).to((entity) -> {
            return entity.getTarget().position().add((entity.random.nextDouble() - 0.5) * 8.0, entity.random.nextInt(16) - 8, (entity.random.nextDouble() - 0.5) * 8.0);
        }).then((entity) -> {
            entity.playSound(ModSounds.BOSS_TP_SFX, 1.0F, 1.0F);
        }).build());
        this.goalSelector.addGoal(1, new ThrowProjectilesGoal(this, 96, 10, BALLS));
        this.goalSelector.addGoal(1, new AOEGoal(this, (e) -> {
            return !(e instanceof VaultBoss);
        }));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, false));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100.0);
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!(source.getEntity() instanceof Player) && !(source.getEntity() instanceof EternalEntity) && source != DamageSource.OUT_OF_WORLD) {
            return false;
        } else if (!this.isInvulnerableTo(source) && source != DamageSource.FALL) {
            if (this.teleportTask.attackEntityFrom(source, amount)) {
                return true;
            } else {
                this.regenAfterAWhile.onDamageTaken();
                return super.hurt(source, amount);
            }
        } else {
            return false;
        }
    }

    public ServerBossEvent getServerBossInfo() {
        return this.bossInfo;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
            this.regenAfterAWhile.tick();
        }

    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return xyz.iwolfking.woldsvaults.init.ModSounds.WOLD_AMBIENT;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return xyz.iwolfking.woldsvaults.init.ModSounds.WOLD_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return xyz.iwolfking.woldsvaults.init.ModSounds.WOLD_DEATH;
    }

    public static final ThrowProjectilesGoal.Projectile BALLS = (world1, shooter) -> {
        return new Snowball(world1, shooter) {
            @Override
            protected void onHitEntity(EntityHitResult raycast) {
                Entity entity = raycast.getEntity();
                if (entity != shooter) {
                    int i = entity instanceof Blaze ? 6 : 3;
                    entity.hurt(DamageSource.indirectMobAttack(this, shooter), (float)i);
                }
            }
        };
    };
}
