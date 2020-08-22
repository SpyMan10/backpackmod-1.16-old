package net.spyman.backpackmod.common.init;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.registry.Registry;
import net.spyman.backpackmod.common.recipe.BackpackUpgradeRecipe;

import static net.spyman.backpackmod.common.BackpackMod.identify;

public final class BackpackRecipes {

    public static final RecipeSerializer<BackpackUpgradeRecipe> IRON_UPGRADE = new SpecialRecipeSerializer<BackpackUpgradeRecipe>(BackpackUpgradeRecipe.IronUpgrade::new);
    public static final RecipeSerializer<BackpackUpgradeRecipe> GOLD_UPGRADE = new SpecialRecipeSerializer<BackpackUpgradeRecipe>(BackpackUpgradeRecipe.GoldUpgrade::new);
    public static final RecipeSerializer<BackpackUpgradeRecipe> DIAMOND_UPGRADE = new SpecialRecipeSerializer<BackpackUpgradeRecipe>(BackpackUpgradeRecipe.DiamondUpgrade::new);
    public static final RecipeSerializer<BackpackUpgradeRecipe> NETHERITE_UPGRADE = new SpecialRecipeSerializer<BackpackUpgradeRecipe>(BackpackUpgradeRecipe.NetheriteUpgrade::new);

    public static final void register() {
        Registry.register(Registry.RECIPE_SERIALIZER, identify("iron_upgrade"), IRON_UPGRADE);
        Registry.register(Registry.RECIPE_SERIALIZER, identify("gold_upgrade"), GOLD_UPGRADE);
        Registry.register(Registry.RECIPE_SERIALIZER, identify("diamond_upgrade"), DIAMOND_UPGRADE);
        Registry.register(Registry.RECIPE_SERIALIZER, identify("netherite_upgrade"), NETHERITE_UPGRADE);
    }
}
