package net.spyman.backpackmod.common.init;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import net.spyman.backpackmod.common.inventory.BackpackInventory;
import net.spyman.backpackmod.common.inventory.BackpackScreenHandler;

public final class BackpackScreenHandlers {

    public final static ScreenHandlerType<BackpackScreenHandler> BACKPACK_SCREEN_HANDLER = new ExtendedScreenHandlerType<>((i, pinv, buf) -> {
        return new BackpackScreenHandler(pinv, i, new BackpackInventory(buf.readInt(), buf.readInt(), null));
    });

    public static final void register() {
        Registry.register(Registry.SCREEN_HANDLER, BackpackScreenHandler.IDENTIFIER, BACKPACK_SCREEN_HANDLER);
    }
}
