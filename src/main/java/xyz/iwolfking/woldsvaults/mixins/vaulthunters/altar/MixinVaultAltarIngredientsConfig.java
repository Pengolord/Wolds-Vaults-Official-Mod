package xyz.iwolfking.woldsvaults.mixins.vaulthunters.altar;

import iskallia.vault.altar.RequiredItems;
import iskallia.vault.config.Config;
import iskallia.vault.config.altar.VaultAltarIngredientsConfig;
import iskallia.vault.config.altar.entry.AltarIngredientEntry;
import iskallia.vault.config.entry.LevelEntryMap;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.data.PlayerResearchesData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = VaultAltarIngredientsConfig.class, remap = false)
public class MixinVaultAltarIngredientsConfig {

    @Shadow private LevelEntryMap<Map<String, WeightedList<AltarIngredientEntry>>> LEVELS;


    /**
     * @author iwolfking
     * @reason Add modded items into pool for the player
     */

    @Unique
    private ServerPlayer woldsVaults$capturedPlayer;

    private static Map<String, String> namespaceToModNameMap = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, String>("powah", "Powah"),
            new AbstractMap.SimpleEntry<String, String>("refinedstorage", "Refined Storage"),
            new AbstractMap.SimpleEntry<String, String>("ae2", "Applied Energistics"),
            new AbstractMap.SimpleEntry<String, String>("botania", "Botania"),
            new AbstractMap.SimpleEntry<String, String>("integrateddynamics", "Integrated Dynamics"),
            new AbstractMap.SimpleEntry<String, String>("mekanism", "Mekanism"),
            new AbstractMap.SimpleEntry<String, String>("moremekanismprocessing", "Mekanism"),
            new AbstractMap.SimpleEntry<String, String>("immersiveengineering", "Immersive Engineering"),
            new AbstractMap.SimpleEntry<String, String>("thermal", "Thermal Expansion"),
            new AbstractMap.SimpleEntry<String, String>("create", "Create"),
            new AbstractMap.SimpleEntry<String, String>("createaddition", "Create Crafts and Additions"),
            new AbstractMap.SimpleEntry<String, String>("industrialforegoing", "Industrial Foregoing"),
            new AbstractMap.SimpleEntry<String, String>("hostilenetworks", "Hostile Neural Networks"),
            new AbstractMap.SimpleEntry<String, String>("mysticalagriculture", "Mystical Agriculture"),
            new AbstractMap.SimpleEntry<String, String>("fluxnetworks", "Flux Networks"),
            new AbstractMap.SimpleEntry<String, String>("botanicalmachinery", "Botanical Machinery")


    );
    @Unique
    private Map<String, AltarIngredientEntry> woldsVaults$getEntriesWithModFilter(int vaultLevel, ServerPlayer player) {
        Optional<Map<String, WeightedList<AltarIngredientEntry>>> pool = this.LEVELS.getForLevel(vaultLevel);
        if (pool.isEmpty()) {
            throw new IllegalArgumentException("No entry found for the given level: " + vaultLevel);
        } else {
            Map<String, WeightedList<AltarIngredientEntry>> map = (Map) pool.get();
            Map<String, AltarIngredientEntry> recipe = new HashMap();
            PlayerResearchesData researchData = PlayerResearchesData.get(player.getLevel());
            ResearchTree playerResearchTree = researchData.getResearches(player);
            map.forEach((k, v) -> {
                List<AltarIngredientEntry> entriesToRemove = new ArrayList<>();
                v.forEach((altarIngredientEntry, number) -> {
                    List<ItemStack> itemsToRemove = new ArrayList<>();
                    for(ItemStack itemStack : altarIngredientEntry.getItems()) {
                        if (namespaceToModNameMap.containsKey(Objects.requireNonNull(itemStack.getItem().getRegistryName()).getNamespace())) {
                            if (!playerResearchTree.isResearched(namespaceToModNameMap.get(itemStack.getItem().getRegistryName().getNamespace()))) {
                                itemsToRemove.add(itemStack);
                            }
                            else {
                                break;
                            }
                        }
                    }
                    altarIngredientEntry.getItems().removeAll(itemsToRemove);
                    if(altarIngredientEntry.getItems().isEmpty()) {
                        entriesToRemove.add(altarIngredientEntry);
                    }
                } );
                entriesToRemove.forEach(v::removeEntry);
                recipe.put(k, (AltarIngredientEntry) v.getRandom(Config.rand));
            });
            return recipe;
        }
    }

    @Redirect(method = "getIngredients", at = @At(value = "INVOKE", target = "Liskallia/vault/config/altar/VaultAltarIngredientsConfig;getEntries(I)Ljava/util/Map;"))
    private Map<String, AltarIngredientEntry> modifyGetEntries(VaultAltarIngredientsConfig instance, int vaultLevel) {
        return woldsVaults$getEntriesWithModFilter(vaultLevel, woldsVaults$capturedPlayer);
    }

    @Inject(method = "getIngredients", at = @At("HEAD"))
    private void getPlayerFromGetIngredients(ServerPlayer player, BlockPos pos, CallbackInfoReturnable<List<RequiredItems>> cir) {
        woldsVaults$capturedPlayer = player;
    }
}