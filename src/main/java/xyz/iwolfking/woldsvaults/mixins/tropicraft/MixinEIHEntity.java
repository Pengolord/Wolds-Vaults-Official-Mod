package xyz.iwolfking.woldsvaults.mixins.tropicraft;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.hostile.TropicraftCreatureEntity;
import net.tropicraft.core.common.entity.neutral.EIHEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "tropicraft")
        }
)
@Mixin(value = EIHEntity.class, remap = false)
public abstract class MixinEIHEntity extends TropicraftCreatureEntity {


    public MixinEIHEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
    }

    /**
     * @author iwolfking
     * @reason Make EIH take damage from anything
     */
    @Overwrite @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount);
    }
}
