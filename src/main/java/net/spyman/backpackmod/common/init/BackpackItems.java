package net.spyman.backpackmod.common.init;

import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.spyman.backpackmod.common.item.BackpackItem;
import net.spyman.backpackmod.common.item.EnderBackpackItem;

import static net.spyman.backpackmod.common.BackpackMod.GROUP;
import static net.spyman.backpackmod.common.BackpackMod.identify;

public final class BackpackItems {

    public static final Item LEATHER_BACKPACK = new BackpackItem(9, 2, new Item.Settings().group(GROUP).rarity(Rarity.COMMON));
    public static final Item COPPER_BACKPACK = new BackpackItem(9, 3, new Item.Settings().group(GROUP).rarity(Rarity.COMMON));
    public static final Item IRON_BACKPACK = new BackpackItem(9, 4, new Item.Settings().group(GROUP).rarity(Rarity.COMMON));
    public static final Item AMETHYST_BACKPACK = new BackpackItem(9, 5, new Item.Settings().group(GROUP).rarity(Rarity.UNCOMMON));
    public static final Item GOLD_BACKPACK = new BackpackItem(9, 6, new Item.Settings().group(GROUP).rarity(Rarity.UNCOMMON));
    public static final Item DIAMOND_BACKPACK = new BackpackItem(11, 7, new Item.Settings().group(GROUP).rarity(Rarity.RARE));
    public static final Item NETHERITE_BACKPACK = new BackpackItem(13, 9, new Item.Settings().group(GROUP).rarity(Rarity.EPIC).fireproof());

    public static final Item ENDER_BACKPACK = new EnderBackpackItem(new Item.Settings().group(GROUP));

    public static final void register() {
        Registry.register(Registry.ITEM, identify("leather_backpack"), LEATHER_BACKPACK);
        Registry.register(Registry.ITEM, identify("copper_backpack"), COPPER_BACKPACK);
        Registry.register(Registry.ITEM, identify("iron_backpack"), IRON_BACKPACK);
        Registry.register(Registry.ITEM, identify("amethyst_backpack"), AMETHYST_BACKPACK);
        Registry.register(Registry.ITEM, identify("gold_backpack"), GOLD_BACKPACK);
        Registry.register(Registry.ITEM, identify("diamond_backpack"), DIAMOND_BACKPACK);
        Registry.register(Registry.ITEM, identify("netherite_backpack"), NETHERITE_BACKPACK);

        Registry.register(Registry.ITEM, identify("ender_backpack"), ENDER_BACKPACK);
    }
}
