package net.spyman.backpackmod.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;

import java.util.UUID;

public class BackpackInventory implements Inventory {

    private final int width;
    private final int height;
    private final DefaultedList<ItemStack> list;
    // ItemStack where nbt-data will be written
    private final Hand hand;

    private final UUID uuid;

    // Inventory stacks storage interface
    private IBackpackStorage storage;

    public BackpackInventory(int width, int height, Hand hand, UUID uuid) {
        this.width = width;
        this.height = height;
        this.list = DefaultedList.ofSize(width * height, ItemStack.EMPTY);
        this.hand = hand;
        this.uuid = uuid;
        this.storage = IBackpackStorage.EMPTY;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public Hand hand() {
        return this.hand;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public IBackpackStorage storage() {
        return this.storage;
    }

    public void storage(IBackpackStorage storage) {
        this.storage = storage;
    }

    @Override
    public int size() {
        return this.width * this.height;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.list) {
            if (!stack.isEmpty()) return false;
        }

        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.list.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.list, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        final ItemStack stack = this.list.remove(slot);
        this.setStack(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.list.set(slot, stack);
    }

    @Override
    public void markDirty() {
        this.storage.write(this.list);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.list.size(); i++) {
            this.removeStack(i);
        }
    }

    @Override
    public void onOpen(PlayerEntity player) {
        this.storage.read(this.list);
    }

    @Override
    public void onClose(PlayerEntity player) {
        // Unused
    }

    public DefaultedList<ItemStack> list() {
        return this.list;
    }
}
