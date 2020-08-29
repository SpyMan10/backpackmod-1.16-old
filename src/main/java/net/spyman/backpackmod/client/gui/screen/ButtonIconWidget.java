package net.spyman.backpackmod.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ButtonIconWidget extends ButtonWidget {

    private final Identifier icon;
    private final int ix;
    private final int iy;
    private final ITooltipRendererCallback cb;

    public ButtonIconWidget(int x, int y, Text hover, PressAction onPress, Identifier icon, int ix, int iy, ITooltipRendererCallback cb) {
        super(x, y, 20, 20, hover, onPress);
        this.icon = icon;
        this.ix = ix;
        this.iy = iy;
        this.cb = cb;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        final MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        client.getTextureManager().bindTexture(WIDGETS_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawTexture(matrices, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        this.drawTexture(matrices, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBg(matrices, client, mouseX, mouseY);

        MinecraftClient.getInstance().getTextureManager().bindTexture(this.icon);
        this.drawTexture(matrices, this.x + 2, this.y + 2, this.ix, this.iy, 16, 16);

        if (mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height) {
            this.cb.render(matrices, this.getMessage(), mouseX, mouseY);
        }
    }

    @FunctionalInterface
    public interface ITooltipRendererCallback {

        void render(MatrixStack matstack, Text text, int mouseX, int mouseY);
    }
}
