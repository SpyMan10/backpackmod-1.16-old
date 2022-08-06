package net.spyman.backpackmod.common.compat;

import net.kyrptonaught.quickshulker.api.QuickOpenableRegistry;
import net.kyrptonaught.quickshulker.api.QuickShulkerData;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;
import net.spyman.backpackmod.common.inventory.BackpackInventory;
import net.spyman.backpackmod.common.inventory.BackpackSlot;
import net.spyman.backpackmod.common.item.BackpackItem;
import net.spyman.backpackmod.common.item.EnderBackpackItem;

public class QuickshulkerCompat implements RegisterQuickShulker {

    @Override
    public void registerProviders() {
        new QuickOpenableRegistry.Builder()
                .setItem(BackpackItem.class)
                .supportsBundleing(true)
                .getBundleInv((player, stack) -> new BackpackInventory(stack, BackpackItem.getBPInvSize(stack)))
                .canBundleInsertItem((player, inventory, hostStack, insertStack) -> BackpackSlot.checkCanInsert(insertStack))
                .setOpenAction((player, stack) -> {
                    if (!player.isSneaking())
                        BackpackItem.openScreen(player, stack);
                })
                .canOpenInHand(false)//don't open if from hand. This allows the mod to normally open and retain the renaming function to occur.
                .register();

        new QuickOpenableRegistry.Builder(new QuickShulkerData.QuickEnderData())
                .setItem(EnderBackpackItem.class)
                .supportsBundleing(true)
                .setOpenAction(EnderBackpackItem::openScreen)
                .canOpenInHand(false)
                .register();
    }
}
