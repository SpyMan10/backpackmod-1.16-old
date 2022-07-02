package net.spyman.backpackmod.common.config;

import com.google.gson.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.spyman.backpackmod.common.item.BackpackItem;

import java.lang.reflect.Type;

public class BackpackEntry {

    /**
     * Item registry name
     */
    private final String name;
    /**
     * Number of inventory width
     */
    private final int width;
    /**
     * Inventory height
     */
    private final int height;
    /**
     * Fireproof
     */
    private final boolean fireproof;
    /**
     * Rarity
     */
    private final Rarity rarity;

    private BackpackEntry(String name, int width, int height, boolean fireproof, Rarity rarity) {
        this.width = width;
        this.height = height;
        this.name = name;
        this.fireproof = fireproof;
        this.rarity = rarity;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public String name() {
        return this.name;
    }

    public boolean fireproof() {
        return this.fireproof;
    }

    public Rarity rarity() {
        return this.rarity;
    }

    public Item asItem() {
        final var settings = new FabricItemSettings().rarity(this.rarity);

        if (this.fireproof) {
            settings.fireproof();
        }

        return new BackpackItem(this.width, this.height, settings);
    }

    public static final BackpackEntry create(String name, int width, int height, boolean fireproof, Rarity rarity) {
        return new BackpackEntry(name, width, height, fireproof, rarity);
    }

    public static class Serializer implements JsonSerializer<BackpackEntry>, JsonDeserializer<BackpackEntry> {

        @Override
        public JsonElement serialize(BackpackEntry src, Type typeOfSrc, JsonSerializationContext context) {
            final var root = new JsonObject();
            root.addProperty("name", src.name());
            root.addProperty("fireproof", src.fireproof());
            root.addProperty("rarity", src.rarity().name().toLowerCase());

            final var inv = new JsonObject();
            inv.addProperty("width", src.width());
            inv.addProperty("height", src.height());

            root.add("inventory", inv);

            return root;
        }

        @Override
        public BackpackEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final var root = json.getAsJsonObject();
            if (root.has("name")) {
                final var name = root.get("name").getAsString();
                var fireproof = false;
                var rarity = Rarity.COMMON;

                if (root.has("fireproof")) {
                    fireproof = root.get("fireproof").getAsBoolean();
                }

                if (root.has("rarity")) {
                    rarity = Rarity.valueOf(root.get("rarity").getAsString().toUpperCase());
                }

                if (root.has("inventory")) {
                    final var inv = root.get("inventory").getAsJsonObject();

                    if (!inv.has("width")) {
                        throw new JsonSyntaxException("BackpackMod: Missing property 'width' in 'inventory' wich is required");
                    }

                    if (!inv.has("height")) {
                        throw new JsonSyntaxException("BackpackMod: Missing property 'height' in 'inventory' wich is required");
                    }

                    final int width = inv.get("width").getAsInt();
                    final int height = inv.get("height").getAsInt();

                    return BackpackEntry.create(name, width, height, fireproof, rarity);
                } else {
                    throw new JsonSyntaxException("BackpackMod: Missing property 'inventory' wich is requried");
                }
            } else {
                throw new JsonSyntaxException("BackpackMod: Missing property 'name' wich is required");
            }
        }
    }
}
