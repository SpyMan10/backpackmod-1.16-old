package net.spyman.backpackmod.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import net.spyman.backpackmod.common.init.BackpackScreenHandlers;

import static net.spyman.backpackmod.common.BackpackMod.identify;

public class BackpackScreenHandler extends ScreenHandler {

    public static final Identifier IDENTIFIER = identify("generic_container");
    private final BackpackInventory inventory;

    public BackpackScreenHandler(PlayerInventory playerInv, int sync, BackpackInventory inventory) {
        super(BackpackScreenHandlers.BACKPACK_SCREEN_HANDLER, sync);
        this.inventory = inventory;

        // Backpack inventory
        for (int n = 0; n < this.inventory.height(); ++n) {
            for (int m = 0; m < this.inventory.width(); ++m) {
                this.addSlot(new BackpackSlot(inventory, m + n * this.inventory.width(), 8 + m * 18, 18 + n * 18));
            }
        }

        // Player inventory
        for (int n = 0; n < 3; ++n) {
            for (int m = 0; m < 9; ++m) {
                this.addSlot(new Slot(playerInv, m + n * 9 + 9, 8 + (this.inventory.width() * 18 - 162) / 2 + m * 18, 31 + (this.inventory.height() + n) * 18));
            }
        }

        // Player hotbar
        for (int n = 0; n < 9; ++n) {
            this.addSlot(new Slot(playerInv, n, 8 + (this.inventory.width() * 18 - 162) / 2 + n * 18, 89 + this.inventory.height() * 18));
        }

        this.inventory.onOpen(playerInv.player);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            final ItemStack stack2 = slot.getStack();
            stack = stack2.copy();

            if (index < this.inventory.size()) {
                if (!this.insertItem(stack2, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(stack2, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (stack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return stack;
    }

    @Override
    public ItemStack onSlotClick(int i, int j, SlotActionType actionType, PlayerEntity playerEntity) {
        if (i >= 0 && this.slots.get(i).getStack() == this.inventory.container()) {
            return ItemStack.EMPTY;
        }

        return super.onSlotClick(i, j, actionType, playerEntity);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.inventory.onClose(player);
    }

    public BackpackInventory inventory() {
        return this.inventory;
    }
}
