package xyz.iwolfking.woldsvaults.mixins.cloudstorage;

import com.github.alexthe668.cloudstorage.entity.BalloonBuddyEntity;
import com.github.alexthe668.cloudstorage.entity.BalloonFace;
import com.github.alexthe668.cloudstorage.entity.BalloonFlyer;
import com.github.alexthe668.cloudstorage.entity.LivingBalloon;
import iskallia.vault.world.data.ServerVaults;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
@Restriction(
        require = {
                @Condition(type = Condition.Type.MOD, value = "cloudstorage")
        }
)
@Mixin(value = BalloonBuddyEntity.class, remap = false)
public abstract class MixinBalloonBuddyEntity extends TamableAnimal implements LivingBalloon, BalloonFlyer {

    @Shadow public abstract BalloonFace getPersonality();

    protected MixinBalloonBuddyEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    public void tick() {
        if(ServerVaults.get(this.getUUID()).isPresent() && this.getPersonality() == BalloonFace.SCARY) {
            return;
        }
        super.tick();
    }
}
