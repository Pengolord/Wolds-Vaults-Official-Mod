package xyz.iwolfking.woldsvaults.mixins.vaulthunters.testing;

import iskallia.vault.core.Version;
import iskallia.vault.core.data.key.VersionedKey;
import iskallia.vault.core.data.key.registry.KeyRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;

@Mixin(value = KeyRegistry.class, remap = false)
public abstract class MixinKeyRegistry<K extends VersionedKey<? extends K, ? extends T>, T> {
    @Shadow protected List<K> keys;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getIndex(ResourceLocation id, Version version) {
        return this.getIndex(this.getKey(id), version);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getIndex(K key, Version version) {
        keys.forEach(k -> {
            System.out.println(k.getId());
        });
        for(Version mapversion : indexCache.keySet()) {
            for(ResourceLocation mapKey : indexCache.get(mapversion).keySet()) {
                System.out.println(mapKey);
                System.out.println(indexCache.get(mapversion).get(mapKey));
            }
        }
        this.ensureCacheIsPresent(version);
        return this.indexCache.get(version).getOrDefault(key.getId(), -1);
    }

    @Shadow public abstract K getKey(ResourceLocation id);

    @Shadow protected abstract void ensureCacheIsPresent(Version version);

    @Shadow protected Map<Version, Map<ResourceLocation, Integer>> indexCache;
}
