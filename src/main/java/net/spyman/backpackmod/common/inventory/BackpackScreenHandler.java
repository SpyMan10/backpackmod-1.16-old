package net.spyman.backpackmod.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import net.spyman.backpackmod.common.init.BackpackScreenHandlers;
import net.spyman.backpackmod.common.item.BackpackItem;

import static net.spyman.backpackmod.common.BackpackMod.identify;

public class BackpackScreenHandler extends ScreenHandler {

    public static final Identifier IDENTIFIER = identify("generic_container");
    private final BackpackInventory inv;

    public BackpackScreenHandler(PlayerInventory playerInv, int sync, BackpackInventory inv) {
        super(BackpackScreenHandlers.BACKPACK_SCREEN_HANDLER, sync);
        this.inv = inv;

        // Backpack inventory
        for (int n = 0; n < this.inv.height(); ++n) {
            for (int m = 0; m < this.inv.width(); ++m) {
                this.addSlot(new BackpackSlot(inv, m + n * this.inv.width(), 8 + m * 18, 18 + n * 18));
            }
        }

        // Player inventory
        for (int n = 0; n < 3; ++n) {
            for (int m = 0; m < 9; ++m) {
                this.addSlot(new Slot(playerInv, m + n * 9 + 9, 8 + (this.inv.width() * 18 - 162) / 2 + m * 18, 31 + (this.inv.height() + n) * 18));
            }
        }

        // Player hotbar
        for (int n = 0; n < 9; ++n) {
            this.addSlot(new Slot(playerInv, n, 8 + (this.inv.width() * 18 - 162) / 2 + n * 18, 89 + this.inv.height() * 18));
        }

        this.inv.onOpen(playerInv.player);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        var stack = player.getStackInHand(this.inv.hand());
        return !stack.isEmpty() && stack.getItem() instanceof BackpackItem;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        var stack = ItemStack.EMPTY;
        var slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            final var stack2 = slot.getStack();
            stack = stack2.copy();

            if (index < this.inv.size()) {
                if (!this.insertItem(stack2, this.inv.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(stack2, 0, this.inv.size(), false)) {
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
    public void onSlotClick(int i, int j, SlotActionType type, PlayerEntity player) {
        // Block drop action while backpack is opened
        if (type != SlotActionType.CLONE) {
            if (i >= 0 && player.getInventory().selectedSlot + 27 + this.inv.size() == i) {
                return;
            }
        }

        super.onSlotClick(i, j, type, player);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.inv.onClose(player);
    }

    public BackpackInventory inventory() {
        return this.inv;
    }
}
