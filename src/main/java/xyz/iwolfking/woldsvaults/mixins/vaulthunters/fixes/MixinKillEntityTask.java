package xyz.iwolfking.woldsvaults.mixins.vaulthunters.fixes;

import com.google.gson.JsonElement;
import iskallia.vault.core.event.CommonEvents;
import iskallia.vault.item.crystal.data.adapter.ISimpleAdapter;
import iskallia.vault.task.ConfiguredTask;
import iskallia.vault.task.KillEntityTask;
import iskallia.vault.task.ProgressConfiguredTask;
import iskallia.vault.task.TaskContext;
import iskallia.vault.task.counter.TaskCounter;
import iskallia.vault.task.source.EntityTaskSource;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.EventPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = KillEntityTask.class, remap = false)
public abstract class MixinKillEntityTask extends ProgressConfiguredTask<Integer, ConfiguredTask.Config> {
    public MixinKillEntityTask(Config config, ISimpleAdapter<? extends TaskCounter<Integer, ?>, ? super Tag, ? super JsonElement> adapter) {
        super(config, adapter);
    }

    /**
     * @author iwolfking
     * @reason Fix channeling not counting as player kill for bingo. I can't view bytecode at the moment but we can target lambda when I can
     */
    @Overwrite @Override
    public void  onAttach(TaskContext context) {
        CommonEvents.ENTITY_DROPS.register(this, EventPriority.HIGHEST, event -> {
            if (this.parent == null || this.parent.hasActiveChildren()) {
                Entity attacker = event.getSource().getEntity();
                if (attacker != null && !attacker.getLevel().isClientSide()) {
                    if (context.getSource() instanceof EntityTaskSource entitySource) {
                        if (attacker.getLevel() == event.getEntity().getLevel()) {
                            if (entitySource.matches(attacker)) {
                                if (((KillEntityTask.Config)this.getConfig()).filter.test(event.getEntity())) {
                                    this.counter.onAdd(1, context);
                                }
                            }
                        }
                    }
                }
            }
        });
        super.onAttach(context);
    }
}
