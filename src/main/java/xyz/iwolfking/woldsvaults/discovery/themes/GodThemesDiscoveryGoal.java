package xyz.iwolfking.woldsvaults.discovery.themes;

import iskallia.vault.core.vault.DiscoveryGoalsManager;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.influence.VaultGod;
import iskallia.vault.core.world.storage.VirtualWorld;
import iskallia.vault.discoverylogic.goal.base.InVaultDiscoveryGoal;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import xyz.iwolfking.vhapi.api.events.vault.VaultEvents;

public class GodThemesDiscoveryGoal extends InVaultDiscoveryGoal<GodThemesDiscoveryGoal> {

    private final VaultGod god;

    public GodThemesDiscoveryGoal(float targetProgress, VaultGod god) {
        super(targetProgress);
        this.god = god;
    }

    @Override
    public void initServer(DiscoveryGoalsManager discoveryGoalsManager, VirtualWorld virtualWorld, Vault vault) {
        VaultEvents.GOD_ALTAR_COMPLETED.register(discoveryGoalsManager, event -> {
            if(event.getPlayer() instanceof ServerPlayer sPlayer) {
                if(sPlayer.getLevel() == virtualWorld) {
                    if(event.getGod().equals(god)) {
                        this.progress(sPlayer, 1.0F);
                    }
                }
            }
        });
    }

}
