package net.spyman.backpackmod.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.spyman.backpackmod.common.config.BackpackCfgFile;
import net.spyman.backpackmod.common.init.BackpackItems;
import net.spyman.backpackmod.common.init.BackpackRecipes;
import net.spyman.backpackmod.common.init.BackpackScreenHandlers;
import net.spyman.backpackmod.common.item.BackpackItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BackpackMod implements ModInitializer {

    public static final String NAME = "BackpackMod";
    public static final String MODID = NAME.toLowerCase();

    public static final ItemGroup GROUP = FabricItemGroupBuilder.create(identify("group")).icon(() -> new ItemStack(Items.APPLE)).build();

    public static final Identifier PACKET_RENAME_BACKPACK = identify("packet_rename_backpack");

    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    @Override
    public void onInitialize() {
        // initialize / load configuration file
        BackpackCfgFile.instance().init();
        BackpackCfgFile.instance().load();

        BackpackItems.register();
        BackpackRecipes.register();
        BackpackScreenHandlers.register();

        // Backpack custom name packet
        // [CustomName(Client)] ==> [ItemStack(Server)]
        ServerPlayNetworking.registerGlobalReceiver(PACKET_RENAME_BACKPACK, (server, player, handler, buf, responseSender) -> {
            final boolean def = buf.readBoolean();
            final Hand hand = buf.readEnumConstant(Hand.class);
            final ItemStack stack = player.getStackInHand(hand);

            if (!stack.isEmpty() && stack.getItem() instanceof BackpackItem) {
                if (def) {
                    stack.removeCustomName();
                } else {
                    final String name = buf.readString(32);
                    stack.setCustomName(Text.of(name));
                }
            }
        });
    }

    public static final MutableText translate(String key, Object... params) {
        return Text.translatable(MODID + "." + key, params);
    }

    public static final Identifier identify(String name) {
        return new Identifier(MODID, name);
    }
}
