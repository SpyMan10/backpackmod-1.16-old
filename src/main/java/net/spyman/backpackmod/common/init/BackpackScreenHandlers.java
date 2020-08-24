package net.spyman.backpackmod.common.init;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Hand;
import net.spyman.backpackmod.common.inventory.BackpackInventory;
import net.spyman.backpackmod.common.inventory.BackpackScreenHandler;

public final class BackpackScreenHandlers {

    public static final ScreenHandlerType<BackpackScreenHandler> BACKPACK_SCREEN_HANDLER;

    static {
        BACKPACK_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(BackpackScreenHandler.IDENTIFIER, (i, pinv, buf) -> {
            return new BackpackScreenHandler(pinv, i, new BackpackInventory(buf.readInt(), buf.readInt(), pinv.player.getStackInHand(buf.readEnumConstant(Hand.class))));
        });
    }
}
