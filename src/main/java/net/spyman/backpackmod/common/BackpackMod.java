package net.spyman.backpackmod.common;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.spyman.backpackmod.common.init.BackpackItems;
import net.spyman.backpackmod.common.init.BackpackRecipes;
import net.spyman.backpackmod.common.item.BackpackItem;

public final class BackpackMod implements ModInitializer {

    public static final String MODID = "backpackmod";

    public static final ItemGroup GROUP = FabricItemGroupBuilder.create(identify("group")).icon(() -> new ItemStack(BackpackItems.LEATHER_BACKPACK)).build();

    public static final Identifier PACKET_RENAME_BACKPACK = identify("packet_rename_backpack");
    public static final Identifier PACKET_OPEN_BACKPACK = identify("packet_open_backpack");

    @Override
    public void onInitialize() {
        BackpackItems.register();
        BackpackRecipes.register();

        ServerPlayNetworking.registerGlobalReceiver(PACKET_RENAME_BACKPACK, (server, player, handler, buf, responseSender) -> {
            final boolean def = buf.readBoolean();
            final Hand hand = buf.readEnumConstant(Hand.class);
            final ItemStack stack = player.getStackInHand(hand);

            if (!stack.isEmpty() && stack.getItem() instanceof BackpackItem) {
                if (def) {
                    stack.removeCustomName();
                } else {
                    final String name = buf.readString(32);
                    stack.setCustomName(new LiteralText(name));
                }
            }
        });

        // Trinkets Compatibility -- Trinkets backpack (chest) slot registration
        // https://github.com/emilyalexandra/trinkets
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            TrinketSlots.addSlot(SlotGroups.CHEST, Slots.BACKPACK, new Identifier("trinkets", "textures/item/empty_trinket_slot_backpack.png"));

            ServerPlayNetworking.registerGlobalReceiver(PACKET_OPEN_BACKPACK, (server, player, handler, buf, responseSender) -> {
                if (!player.getEntityWorld().isClient()) {
                    final Inventory inv = TrinketsApi.getTrinketsInventory(player);

                    // That work because only one backpack could be stored in Trinkets slots
                    for (int i = 0; i < inv.size(); i++) {
                        final ItemStack stack = inv.getStack(i);

                        if (stack.getItem() instanceof BackpackItem) {
                            BackpackItem.openScreen(player, stack);
                            break;
                        }
                    }
                }
            });
        }
    }

    public static final TranslatableText translate(String key, Object... params) {
        return new TranslatableText(MODID + "." + key, params);
    }

    public static final Identifier identify(String name) {
        return new Identifier(MODID, name);
    }
}
