package net.spyman.backpackmod.common.init;

import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.spyman.backpackmod.common.config.BackpackCfgFile;
import net.spyman.backpackmod.common.item.BackpackItem;
import net.spyman.backpackmod.common.item.EnderBackpackItem;

import static net.spyman.backpackmod.common.BackpackMod.GROUP;
import static net.spyman.backpackmod.common.BackpackMod.identify;

public final class BackpackItems {

    public static final void register() {
        BackpackCfgFile.config().backpacks().forEach(e -> Registry.register(Registry.ITEM, identify(e.name()), e.asItem()));

        if (BackpackCfgFile.config().enderBackpackEnable()) {
            Registry.register(Registry.ITEM, identify("ender_backpack"), new EnderBackpackItem(new Item.Settings().group(GROUP)));
        }
    }
}
