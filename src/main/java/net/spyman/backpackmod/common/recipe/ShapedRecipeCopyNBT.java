package net.spyman.backpackmod.common.recipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.spyman.backpackmod.common.init.BackpackRecipes;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ShapedRecipeCopyNBT extends ShapedRecipe {

    private final int targetSlot;
    private final String group;

    public ShapedRecipeCopyNBT(Identifier id, String group, int width, int height, DefaultedList<Ingredient> ingredients, ItemStack output, int targetSlot) {
        super(id, group, width, height, ingredients, output);
        this.targetSlot = targetSlot;
        this.group = group;
    }

    @Override
    public ItemStack craft(CraftingInventory matrix) {
        if (this.targetSlot < 0 || this.targetSlot >= matrix.size()) {
            throw new IllegalArgumentException("ShapedRecipeCopyNBT: Wrong value for 'target_slot' key, specify the slot where is the itemstack source top copy NBT-Data.");
        }

        final ItemStack stack = matrix.getStack(targetSlot);
        if (!stack.isEmpty()) {
            final ItemStack out = super.craft(matrix);

            if (stack.hasNbt()) {
                out.getOrCreateNbt().copyFrom(stack.getNbt());
            }

            return out;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BackpackRecipes.BACKPACK_UPGRADE;
    }

    public int targetSlot() {
        return this.targetSlot;
    }

    public String group() {
        return this.group;
    }

    public static final class Serializer implements RecipeSerializer<ShapedRecipeCopyNBT> {

        @Override
        public ShapedRecipeCopyNBT read(Identifier identifier, JsonObject obj) {
            final String group = JsonHelper.getString(obj, "group", "");
            final Map<String, Ingredient> map = getComponents(JsonHelper.getObject(obj, "key"));
            final String[] pattern = combinePattern(getPattern(JsonHelper.getArray(obj, "pattern")));
            final int i = pattern[0].length();
            final int j = pattern.length;
            final DefaultedList<Ingredient> inputs = getIngredients(pattern, map, i, j);
            final ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(obj, "result"));
            return new ShapedRecipeCopyNBT(identifier, group, i, j, inputs, output, obj.get("target_slot").getAsInt());
        }

        @Override
        public ShapedRecipeCopyNBT read(Identifier identifier, PacketByteBuf buf) {
            final int i = buf.readVarInt();
            final int j = buf.readVarInt();
            final String string = buf.readString(32767);
            final DefaultedList<Ingredient> inputs = DefaultedList.ofSize(i * j, Ingredient.EMPTY);

            for (int k = 0; k < inputs.size(); ++k) {
                inputs.set(k, Ingredient.fromPacket(buf));
            }

            final ItemStack output = buf.readItemStack();
            return new ShapedRecipeCopyNBT(identifier, string, i, j, inputs, output, buf.readInt());
        }

        @Override
        public void write(PacketByteBuf buf, ShapedRecipeCopyNBT recipe) {
            buf.writeVarInt(recipe.getWidth());
            buf.writeVarInt(recipe.getHeight());
            buf.writeString(recipe.group());
            recipe.getIngredients().forEach(i -> i.write(buf));
            buf.writeItemStack(recipe.getOutput());
            buf.writeInt(recipe.targetSlot());
        }

        private static String[] getPattern(JsonArray json) {
            String[] strings = new String[json.size()];
            if (strings.length > 3) {
                throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
            } else if (strings.length == 0) {
                throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            } else {
                for (int i = 0; i < strings.length; ++i) {
                    String string = JsonHelper.asString(json.get(i), "pattern[" + i + "]");
                    if (string.length() > 3) {
                        throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                    }

                    if (i > 0 && strings[0].length() != string.length()) {
                        throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                    }

                    strings[i] = string;
                }

                return strings;
            }
        }

        public static Map<String, Ingredient> getComponents(JsonObject json) {
            Map<String, Ingredient> map = Maps.newHashMap();
            Iterator var2 = json.entrySet().iterator();

            while (var2.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry) var2.next();
                if (entry.getKey().length() != 1) {
                    throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
                }

                if (" ".equals(entry.getKey())) {
                    throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
                }

                map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
            }

            map.put(" ", Ingredient.EMPTY);
            return map;
        }

        public static DefaultedList<Ingredient> getIngredients(String[] pattern, Map<String, Ingredient> key, int width, int height) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
            Set<String> set = Sets.newHashSet(key.keySet());
            set.remove(" ");

            for (int i = 0; i < pattern.length; ++i) {
                for (int j = 0; j < pattern[i].length(); ++j) {
                    String string = pattern[i].substring(j, j + 1);
                    Ingredient ingredient = key.get(string);
                    if (ingredient == null) {
                        throw new JsonSyntaxException("Pattern references symbol '" + string + "' but it's not defined in the key");
                    }

                    set.remove(string);
                    defaultedList.set(j + width * i, ingredient);
                }
            }

            if (!set.isEmpty()) {
                throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
            } else {
                return defaultedList;
            }
        }

        public static int findNextIngredient(String pattern) {
            int i;
            for (i = 0; i < pattern.length() && pattern.charAt(i) == ' '; ++i) {
            }

            return i;
        }

        public static int findNextIngredientReverse(String pattern) {
            int i;
            for (i = pattern.length() - 1; i >= 0 && pattern.charAt(i) == ' '; --i) {
            }

            return i;
        }

        public static String[] combinePattern(String... lines) {
            int i = 2147483647;
            int j = 0;
            int k = 0;
            int l = 0;

            for (int m = 0; m < lines.length; ++m) {
                String string = lines[m];
                i = Math.min(i, findNextIngredient(string));
                int n = findNextIngredientReverse(string);
                j = Math.max(j, n);
                if (n < 0) {
                    if (k == m) {
                        ++k;
                    }

                    ++l;
                } else {
                    l = 0;
                }
            }

            if (lines.length == l) {
                return new String[0];
            } else {
                String[] strings = new String[lines.length - l - k];

                for (int o = 0; o < strings.length; ++o) {
                    strings[o] = lines[o + k].substring(i, j + 1);
                }

                return strings;
            }
        }
    }
}
