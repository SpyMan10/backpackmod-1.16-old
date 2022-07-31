package net.spyman.backpackmod.common.init;

import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.spyman.backpackmod.common.config.BackpackCfgFile;
import net.spyman.backpackmod.common.item.BackpackItem;
import net.spyman.backpackmod.common.item.EnderBackpackItem;

import java.util.ArrayList;

import static net.spyman.backpackmod.common.BackpackMod.GROUP;
import static net.spyman.backpackmod.common.BackpackMod.identify;

public final class BackpackItems {

    public static final TagKey<Item> BACKPACK_TAGS = TagKey.of(Registry.ITEM_KEY, identify("backpack"));

    public static final void register() {
        var list = new ArrayList<String>();

        BackpackCfgFile.config().backpacks().forEach(e -> {
            var id = identify(e.name());
            Registry.register(Registry.ITEM, id, e.asItem());
            list.add(id.toString());
        });

        if (BackpackCfgFile.config().enderBackpackEnable()) {
            Registry.register(Registry.ITEM, identify("ender_backpack"), new EnderBackpackItem(new Item.Settings().group(GROUP)));
        }
    }
}
