package net.spyman.backpackmod.common.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.spyman.backpackmod.common.init.BackpackItems;
import net.spyman.backpackmod.common.init.BackpackRecipes;

import java.util.function.Supplier;

public class BackpackUpgradeRecipe extends SpecialCraftingRecipe {

    public static final char[] PATTERN = {'0', '1', '0', '1', '2', '1', '0', '1', '0'};

    private final RecipeSerializer<?> serializer;

    private final Supplier<Item> mat;
    private final Supplier<Item> target;
    private final Supplier<Item> result;

    private final DefaultedList<Ingredient> preview;

    public BackpackUpgradeRecipe(Identifier id, RecipeSerializer<?> serializer, Supplier<Item> mat, Supplier<Item> target, Supplier<Item> result) {
        super(id);
        this.serializer = serializer;
        this.mat = mat;
        this.target = target;
        this.result = result;
        this.preview = DefaultedList.ofSize(9, Ingredient.EMPTY);

        for (int i = 0; i < 9; i++) {
            switch (PATTERN[i]) {
                case '1':
                    this.preview.set(i, Ingredient.ofItems(mat.get()));
                    break;
                case '2':
                    this.preview.set(i, Ingredient.ofItems(target.get()));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        for (int i = 0; i < 9; i++) {
            final ItemStack s = inv.getStack(i);

            switch (PATTERN[i]) {
                case '0':
                    if (!s.isEmpty()) return false;
                    break;
                case '1':
                    if (this.mat.get() != s.getItem()) return false;
                    break;
                case '2':
                    if (this.target.get() != s.getItem()) return false;
                    break;
            }
        }

        return true;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        final ItemStack r = new ItemStack(this.result.get());
        final ItemStack bpk = inv.getStack(4);

        if (bpk.hasTag()) {
            r.getOrCreateTag().put("BackpackContent", bpk.getTag().getCompound("BackpackContent"));
        }

        return r;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 9;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Override
    public DefaultedList<Ingredient> getPreviewInputs() {
        return this.preview;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return false;
    }

    @Override
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(this.result.get());
    }

    @Override
    public ItemStack getOutput() {
        return new ItemStack(this.result.get());
    }

    public static final class IronUpgrade extends BackpackUpgradeRecipe {

        public IronUpgrade(Identifier id) {
            super(id, BackpackRecipes.IRON_UPGRADE, () -> Items.IRON_INGOT, () -> BackpackItems.LEATHER_BACKPACK, () -> BackpackItems.IRON_BACKPACK);
        }
    }

    public static final class GoldUpgrade extends BackpackUpgradeRecipe {

        public GoldUpgrade(Identifier id) {
            super(id, BackpackRecipes.GOLD_UPGRADE, () -> Items.GOLD_INGOT, () -> BackpackItems.IRON_BACKPACK, () -> BackpackItems.GOLD_BACKPACK);
        }
    }

    public static final class DiamondUpgrade extends BackpackUpgradeRecipe {

        public DiamondUpgrade(Identifier id) {
            super(id, BackpackRecipes.DIAMOND_UPGRADE, () -> Items.DIAMOND, () -> BackpackItems.GOLD_BACKPACK, () -> BackpackItems.DIAMOND_BACKPACK);
        }
    }

    public static final class NetheriteUpgrade extends BackpackUpgradeRecipe {

        public NetheriteUpgrade(Identifier id) {
            super(id, BackpackRecipes.IRON_UPGRADE, () -> Items.NETHERITE_SCRAP, () -> BackpackItems.DIAMOND_BACKPACK, () -> BackpackItems.NETHERITE_BACKPACK);
        }
    }
}
