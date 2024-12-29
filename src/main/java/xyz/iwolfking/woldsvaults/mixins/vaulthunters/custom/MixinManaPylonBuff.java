package xyz.iwolfking.woldsvaults.mixins.vaulthunters.custom;


import iskallia.vault.core.vault.pylon.ManaPylonBuff;
import iskallia.vault.core.vault.pylon.PylonBuff;
import iskallia.vault.init.ModAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ManaPylonBuff.class)
public class MixinManaPylonBuff extends PylonBuff<ManaPylonBuff.Config> {
    @Unique
    protected int woldsVaults$tick = 0;
    public MixinManaPylonBuff(ManaPylonBuff.Config config) {
        super(config);
    }

    @Override
    public boolean isDone() {
        return (super.isDone() || this.woldsVaults$tick >= 1200);
    }

    @Override
    public void onTick(MinecraftServer server) {
        super.onTick(server);
        getPlayer(server).ifPresent(player -> {
            AttributeModifier modifier = new AttributeModifier(this.uuid, "Pylon Buff", 1.0, AttributeModifier.Operation.ADDITION);
            AttributeInstance attribute = player.getAttribute(ModAttributes.MANA_REGEN);
            if (attribute != null && !attribute.hasModifier(modifier)) {
                attribute.addTransientModifier(modifier);
            }
        });
        this.woldsVaults$tick++;
    }

    @Override
    public void onRemove(MinecraftServer server) {
        getPlayer(server).ifPresent(player -> {
            AttributeInstance attribute = player.getAttribute(ModAttributes.MANA_REGEN);
            if (attribute != null) {
                attribute.removeModifier(this.uuid);
            }
        });
    }


    @Override
    public void write(CompoundTag object) {
        super.write(object);
        object.putInt("tick", this.woldsVaults$tick);
    }

    @Override
    public void read(CompoundTag object) {
        super.read(object);
        this.woldsVaults$tick = object.getInt("tick");
    }
}
