package net.spyman.backpackmod.common.config;

import com.google.gson.*;
import net.minecraft.util.Rarity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class BackpackConfig {

    private boolean enderBackpackEnable;
    private List<BackpackEntry> backpacks;
    private String nbtTagName;

    public BackpackConfig() {
        this.backpacks = new ArrayList<BackpackEntry>();
        this.nbtTagName = "BackpackContent";
    }

    public void defaultConfig() {
        this.backpacks.clear();
        this.enderBackpackEnable = true;
        this.nbtTagName = "BackpackContent";
        this.backpacks.add(BackpackEntry.create("leather_backpack", 9, 2, false, Rarity.COMMON));
        this.backpacks.add(BackpackEntry.create("copper_backpack", 9, 3, false, Rarity.COMMON));
        this.backpacks.add(BackpackEntry.create("iron_backpack", 9, 4, false, Rarity.COMMON));
        this.backpacks.add(BackpackEntry.create("amethyst_backpack", 9, 5, false, Rarity.UNCOMMON));
        this.backpacks.add(BackpackEntry.create("gold_backpack", 9, 6, false, Rarity.UNCOMMON));
        this.backpacks.add(BackpackEntry.create("diamond_backpack", 11, 7, false, Rarity.RARE));
        this.backpacks.add(BackpackEntry.create("netherite_backpack", 13, 9, true, Rarity.EPIC));
    }

    public boolean enderBackpackEnable() {
        return this.enderBackpackEnable;
    }

    public void enderBackpackEnable(boolean enderBackpackEnable) {
        this.enderBackpackEnable = enderBackpackEnable;
    }

    public List<BackpackEntry> backpacks() {
        return this.backpacks;
    }

    public void backpacks(List<BackpackEntry> backpacks) {
        this.backpacks = backpacks;
    }

    public String nbtTagName() {
        return this.nbtTagName;
    }

    public void nbtTagName(String nbtTagName) {
        this.nbtTagName = nbtTagName;
    }

    public static class Serializer implements JsonSerializer<BackpackConfig>, JsonDeserializer<BackpackConfig> {

        @Override
        public JsonElement serialize(BackpackConfig src, Type typeOfSrc, JsonSerializationContext context) {
            final var root = new JsonObject();
            final var general = new JsonObject();
            general.addProperty("enableEnderBackpack", src.enderBackpackEnable());
            general.addProperty("nbtTagName", src.nbtTagName());
            root.add("general", general);

            final var backpacks = new JsonArray();
            src.backpacks().forEach(entry -> backpacks.add(context.serialize(entry)));
            root.add("backpacks", backpacks);

            return root;
        }

        @Override
        public BackpackConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final var cfg = new BackpackConfig();
            final var root = json.getAsJsonObject();
            final var general = root.get("general").getAsJsonObject();

            cfg.enderBackpackEnable(general.get("enableEnderBackpack").getAsBoolean());

            final var backpacks = root.get("backpacks").getAsJsonArray();
            backpacks.forEach(e -> cfg.backpacks().add(context.deserialize(e, BackpackEntry.class)));

            return cfg;
        }
    }
}
