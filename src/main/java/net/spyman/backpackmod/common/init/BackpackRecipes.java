package net.spyman.backpackmod.common.init;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.registry.Registry;
import net.spyman.backpackmod.common.recipe.ShapedRecipeCopyNBT;

import static net.spyman.backpackmod.common.BackpackMod.identify;

public final class BackpackRecipes {

    public static final RecipeSerializer<ShapedRecipeCopyNBT> BACKPACK_UPGRADE = new ShapedRecipeCopyNBT.Serializer();

    public static final void register() {
        Registry.register(Registry.RECIPE_SERIALIZER, identify("shaped_copy_nbt"), BACKPACK_UPGRADE);
    }
}
