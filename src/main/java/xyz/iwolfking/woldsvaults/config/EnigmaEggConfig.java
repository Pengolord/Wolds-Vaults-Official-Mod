package xyz.iwolfking.woldsvaults.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.Config;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.world.item.Items;

public class EnigmaEggConfig extends Config {
    @Expose
    public iskallia.vault.util.data.WeightedList<ProductEntry> POOL = new WeightedList<>();


    public String getName() {
        return "enigma_egg";
    }


    protected void reset() {
        this.POOL.add(new ProductEntry(Items.APPLE, 8, null), 3);
        this.POOL.add(new ProductEntry(Items.GOLDEN_APPLE, 1, null), 1);
    }
}
