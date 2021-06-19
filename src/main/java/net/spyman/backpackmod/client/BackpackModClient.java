package net.spyman.backpackmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.options.KeyBinding;
import net.spyman.backpackmod.client.gui.screen.BackpackScreen;
import net.spyman.backpackmod.common.init.BackpackScreenHandlers;

@Environment(EnvType.CLIENT)
public final class BackpackModClient implements ClientModInitializer {

    private static KeyBinding backpack;

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(BackpackScreenHandlers.BACKPACK_SCREEN_HANDLER, BackpackScreen::new);
    }
}
