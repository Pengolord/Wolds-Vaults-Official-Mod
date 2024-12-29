package xyz.iwolfking.woldsvaults.models.crystal;

import com.google.gson.JsonObject;
import iskallia.vault.client.util.color.ColorUtil;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.model.CoreCrystalModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.nbt.CompoundTag;

import java.awt.Color;
import java.util.Optional;
import java.util.Random;

public class UnhingedCrystalModel extends CoreCrystalModel {
    public UnhingedCrystalModel() {
    }

    public ModelResourceLocation getItemModel() {
        return new ModelResourceLocation("the_vault:crystal/core/unhinged#inventory");
    }

    public int getItemColor(float time) {
        return 10910157;
    }

    public int getBlockColor(CrystalData crystal, float time) {
        int second = (int)(time / 20.0F);
        float progress = time % 20.0F / 20.0F;
        return this.getColor(second, progress);
    }

    public int getColor(int second, float progress) {
        Random random = new Random(second - 1);
        random.nextLong();
        int previous = Color.getHSBColor(random.nextFloat(), random.nextFloat(), random.nextFloat()).getRGB();
        random = new Random(second);
        random.nextLong();
        int current = Color.getHSBColor(random.nextFloat(), random.nextFloat(), random.nextFloat()).getRGB();
        return ColorUtil.blendColors(previous, current, 1.0F - progress);
    }

    @Override
    public Optional<CompoundTag> writeNbt() {
        return Optional.of(new CompoundTag());
    }

    @Override
    public void readNbt(CompoundTag nbt) {
    }

    @Override
    public Optional<JsonObject> writeJson() {
        return Optional.of(new JsonObject());
    }

    @Override
    public void readJson(JsonObject json) {
    }
}
