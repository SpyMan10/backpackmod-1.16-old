package net.spyman.backpackmod.client.gui.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spyman.backpackmod.common.inventory.BackpackScreenHandler;

public class BackpackScreen extends HandledScreen<BackpackScreenHandler> {

    public static final Identifier GENERIC_54 = new Identifier("textures/gui/container/generic_54.png");

    public BackpackScreen(BackpackScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = handler.inventory().width() * 18 + 17;
        this.backgroundHeight = (handler.inventory().height() + 4) * 18 + 41;
        this.playerInventoryTitleY = handler.inventory().height() * 18 + 20;
    }

    @Override
    public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.client.getTextureManager().bindTexture(GENERIC_54);
        matrices.push();
        matrices.translate(this.x, this.y, 0.0D);

        // Background storage texture pseudo-generation

        // Upper-left corner
        this.drawTexture(matrices, 0, 0, 0, 0, 7, 17);

        // Top
        drawTexture(matrices, 7, 0, handler.inventory().width() * 18, 17, 7, 0, 1, 17, 256, 256);

        // Upper-right corner
        this.drawTexture(matrices, 7 + handler.inventory().width() * 18, 0, 169, 0, 7, 17);

        // Left
        drawTexture(matrices, 0, 17, 7, (handler.inventory().height() + 4) * 18 + 22, 0, 17, 7, 1, 256, 256);

        // Lower-left corner
        this.drawTexture(matrices, 0, (handler.inventory().height() + 4) * 18 + 34, 0, 215, 7, 7);

        // Lower
        drawTexture(matrices, 7, (handler.inventory().height() + 4) * 18 + 34, handler.inventory().width() * 18, 7, 7, 215, 1, 7, 256, 256);

        // Lower-right corner
        this.drawTexture(matrices, (handler.inventory().width() * 18 + 7), (handler.inventory().height() + 4) * 18 + 34, 169, 215, 7, 7);

        // Right
        drawTexture(matrices, (handler.inventory().width() * 18 + 7), 17, 7, (handler.inventory().height() + 4) * 18 + 17, 169, 17, 7, 1, 256, 256);

        // Background fill
        fill(matrices, 7, 17, this.backgroundWidth - 10, this.backgroundHeight - 7, 0xFFC6C6C6);

        this.handler.slots.forEach(s -> {
            this.drawTexture(matrices, s.x - 1, s.y - 1, 7, 17, 18, 18);
        });

        matrices.pop();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
