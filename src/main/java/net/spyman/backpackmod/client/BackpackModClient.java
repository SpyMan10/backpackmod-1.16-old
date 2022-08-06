package net.spyman.backpackmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.spyman.backpackmod.client.gui.screen.BackpackScreen;
import net.spyman.backpackmod.common.init.BackpackScreenHandlers;

@Environment(EnvType.CLIENT)
public final class BackpackModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(BackpackScreenHandlers.BACKPACK_SCREEN_HANDLER, BackpackScreen::new);
    }
}
