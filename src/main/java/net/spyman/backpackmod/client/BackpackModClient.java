package net.spyman.backpackmod.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.spyman.backpackmod.client.gui.screen.BackpackScreen;
import net.spyman.backpackmod.common.BackpackMod;
import net.spyman.backpackmod.common.init.BackpackScreenHandlers;
import org.lwjgl.glfw.GLFW;

import static net.spyman.backpackmod.common.BackpackMod.MODID;

@Environment(EnvType.CLIENT)
public final class BackpackModClient implements ClientModInitializer {

    private static KeyBinding backpack;

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(BackpackScreenHandlers.BACKPACK_SCREEN_HANDLER, BackpackScreen::new);

        // Trinkets Compatibility -- Handling key binding for accessing backpack content
        // https://github.com/emilyalexandra/trinkets
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            backpack = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MODID + ".open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "category." + MODID + ".backpack"));

            ClientTickEvents.END_CLIENT_TICK.register(c -> {
                if (backpack.wasPressed()) {
                    ClientPlayNetworking.send(BackpackMod.PACKET_OPEN_BACKPACK, new PacketByteBuf(Unpooled.buffer()));
                }
            });
        }
    }
}
