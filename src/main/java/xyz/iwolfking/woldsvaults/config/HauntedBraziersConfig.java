package xyz.iwolfking.woldsvaults.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.Config;
import iskallia.vault.config.entry.LevelEntryList;
import net.minecraft.resources.ResourceLocation;

public class HauntedBraziersConfig extends Config {
    @Expose
    private LevelEntryList<HauntedBraziersConfig.Entry> levels;

    public HauntedBraziersConfig() {
    }

    public String getName() {
        return "haunted_braziers";
    }

    public ResourceLocation getStackModifierPool(int level) {
        return this.levels.getForLevel(level).orElseThrow().stackModifierPool;
    }

    public ResourceLocation getOverStackModifierPool(int level) {
        return this.levels.getForLevel(level).orElseThrow().overStackModifierPool;
    }

    public ResourceLocation getOverStackLootTable(int level) {
        return this.levels.getForLevel(level).orElseThrow().overStackLootTable;
    }

    protected void reset() {
        this.levels = new LevelEntryList<>();
    }

    private static class Entry implements LevelEntryList.ILevelEntry {
        @Expose
        private final int level;
        @Expose
        private ResourceLocation stackModifierPool;
        @Expose
        private ResourceLocation overStackModifierPool;
        @Expose
        private ResourceLocation overStackLootTable;

        public Entry(int level, ResourceLocation stackModifierPool, ResourceLocation overStackModifierPool, ResourceLocation overStackLootTable) {
            this.level = level;
            this.stackModifierPool = stackModifierPool;
            this.overStackModifierPool = overStackModifierPool;
            this.overStackLootTable = overStackLootTable;
        }

        public int getLevel() {
            return this.level;
        }
    }
}
