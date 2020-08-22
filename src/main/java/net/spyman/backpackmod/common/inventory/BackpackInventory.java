package net.spyman.backpackmod.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

public class BackpackInventory implements Inventory {

    private final int width;
    private final int height;

    private final DefaultedList<ItemStack> list;
    private final ItemStack container;

    public BackpackInventory(int width, int height, ItemStack container) {
        this.width = width;
        this.height = height;
        this.list = DefaultedList.ofSize(width * height, ItemStack.EMPTY);
        this.container = container;
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
        this.write();
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
        this.read();
    }

    @Override
    public void onClose(PlayerEntity player) {
        this.write();
    }

    public DefaultedList<ItemStack> list() {
        return this.list;
    }

    public void write() {
        if (this.container != null && !this.container.isEmpty()) {
            this.container.getOrCreateTag().put("BackpackContent", Inventories.toTag(new CompoundTag(), this.list, true));
        }
    }

    public void read() {
        if (this.container != null && !this.container.isEmpty()) {
            Inventories.fromTag(this.container.getOrCreateTag().getCompound("BackpackContent"), this.list);
        }
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public ItemStack container() {
        return this.container;
    }
}
