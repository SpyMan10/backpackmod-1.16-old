package net.spyman.backpackmod.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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

    public BackpackInventory(int width, int height, Hand hand, UUID uuid) {
        this.width = width;
        this.height = height;
        this.list = DefaultedList.ofSize(width * height, ItemStack.EMPTY);
        this.hand = hand;
        this.uuid = uuid;
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
        // Unused
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
        this.read(player.getStackInHand(this.hand));
    }

    @Override
    public void onClose(PlayerEntity player) {
        this.write(player.getStackInHand(this.hand));
    }

    public DefaultedList<ItemStack> list() {
        return this.list;
    }

    public void write(ItemStack container) {
        if (container != null && !container.isEmpty()) {
            container.getOrCreateNbt().put("BackpackContent", Inventories.writeNbt(new NbtCompound(), this.list, true));
        }
    }

    public void read(ItemStack container) {
        if (container != null && !container.isEmpty()) {
            Inventories.readNbt(container.getOrCreateNbt().getCompound("BackpackContent"), this.list);
        }
    }
}