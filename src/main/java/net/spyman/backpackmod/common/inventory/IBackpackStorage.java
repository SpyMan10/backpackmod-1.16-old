package net.spyman.backpackmod.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface IBackpackStorage {

    IBackpackStorage EMPTY = new IBackpackStorage() {
        @Override
        public void write(DefaultedList<ItemStack> stacks) {
            // Empty
        }

        @Override
        public void read(DefaultedList<ItemStack> stacks) {
            // Empty
        }
    };

    void write(DefaultedList<ItemStack> stacks);

    void read(DefaultedList<ItemStack> stacks);
}
