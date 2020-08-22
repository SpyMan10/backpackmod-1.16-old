package net.spyman.backpackmod.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
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

    @Override
    public void onInitialize() {
        BackpackItems.register();
        BackpackRecipes.register();

        ServerSidePacketRegistry.INSTANCE.register(PACKET_RENAME_BACKPACK, (ctx, buf) -> {
            final boolean def = buf.readBoolean();
            final Hand hand = buf.readEnumConstant(Hand.class);
            final ItemStack stack = ctx.getPlayer().getStackInHand(hand);

            if (!stack.isEmpty() && stack.getItem() instanceof BackpackItem) {
                if (def) {
                    stack.removeCustomName();
                } else {
                    final String name = buf.readString(64);
                    stack.setCustomName(new LiteralText(name));
                }
            }
        });
    }

    public static final TranslatableText translate(String key, Object... params) {
        return new TranslatableText(MODID + "." + key, params);
    }

    public static final Identifier identify(String name) {
        return new Identifier(MODID, name);
    }
}
