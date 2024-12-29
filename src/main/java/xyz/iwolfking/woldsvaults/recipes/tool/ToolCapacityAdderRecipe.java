package xyz.iwolfking.woldsvaults.recipes.tool;

import iskallia.vault.gear.data.ToolGearData;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.item.crystal.recipe.AnvilContext;
import iskallia.vault.item.crystal.recipe.VanillaAnvilRecipe;
import iskallia.vault.item.tool.ToolItem;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.woldsvaults.config.forge.WoldsVaultsConfig;
import xyz.iwolfking.woldsvaults.init.ModItems;

public class ToolCapacityAdderRecipe extends VanillaAnvilRecipe {


    @Override
    public boolean onSimpleCraft(AnvilContext context) {

        ItemStack primary = context.getInput()[0];
        ItemStack secondary = context.getInput()[1];
        if (primary.getItem() instanceof ToolItem && secondary.getItem() == ModItems.RESONATING_REINFORCEMENT) {
            ItemStack output = primary.copy();
            VaultGearData gear = ToolGearData.read(output);
            int toolCapacity = 0;
            int normalMaxCapacity = 0;
            if(gear.getFirstValue(ModGearAttributes.TOOL_CAPACITY).isPresent()) {
                toolCapacity = gear.getFirstValue(ModGearAttributes.TOOL_CAPACITY).get();
            }
            else {
                return false;
            }

            if(gear.getFirstValue(ModGearAttributes.TOOL_MATERIAL).isPresent()) {
                normalMaxCapacity = gear.getFirstValue(ModGearAttributes.TOOL_MATERIAL).get().getCapacity();
            }
            else {
                return false;
            }

            if(toolCapacity < (normalMaxCapacity + WoldsVaultsConfig.COMMON.crystalReinforcementMaxCapacityAdded.get())) {
                gear.createOrReplaceAttributeValue(ModGearAttributes.TOOL_CAPACITY, toolCapacity + 10);
            }
            else {
                return false;
            }

            gear.write(output);
            context.setOutput(output);

            context.onTake(context.getTake().append(() -> {
                context.getInput()[0].shrink(1);
                context.getInput()[1].shrink(1);
            }));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRegisterJEI(IRecipeRegistration iRecipeRegistration) {

    }
}
