package net.spyman.backpackmod.client.gui.screen;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.spyman.backpackmod.common.BackpackMod;
import org.lwjgl.glfw.GLFW;

public class BackpackRenameScreen extends Screen {

    private final Hand hand;
    private final Text placeHolder;
    private final OrderedText orderedTextTitle;
    private final TranslatableText maxCharsMsg;
    private TextFieldWidget textField;

    public BackpackRenameScreen(Hand hand, Text placeHolder) {
        super(BackpackMod.translate("screen.title.rename"));
        this.hand = hand;
        this.orderedTextTitle = this.getTitle().asOrderedText();
        this.placeHolder = placeHolder;
        this.maxCharsMsg = BackpackMod.translate("screen.rename.max.char");
    }

    @Override
    protected void init() {
        super.init();
        this.textField = new TextFieldWidget(this.textRenderer, (this.width - 195) / 2, (this.height - 65) / 2, 195, 20, this.placeHolder);
        this.textField.setMaxLength(64);
        this.addChild(this.textField);
        this.textField.setText(this.placeHolder.asString());

        this.addButton(new ButtonWidget((this.width - 195) / 2, 45 + (this.height - 65) / 2, 95, 20, BackpackMod.translate("screen.button.default.name"), b -> {
            b.playDownSound(MinecraftClient.getInstance().getSoundManager());
            this.sendChange(true);
        }));
        this.addButton(new ButtonWidget(100 + (this.width - 195) / 2, 45 + (this.height - 65) / 2, 95, 20, BackpackMod.translate("screen.button.rename"), b -> {
            b.playDownSound(MinecraftClient.getInstance().getSoundManager());
            this.sendChange(false);
        }));

        this.setInitialFocus(this.textField);
    }

    public void sendChange(boolean def) {
        final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(def);
        buf.writeEnumConstant(hand);

        if (!def) {
            buf.writeString(this.textField.getText().trim());
        }

        ClientSidePacketRegistry.INSTANCE.sendToServer(BackpackMod.PACKET_RENAME_BACKPACK, buf);
        this.onClose();
    }

    @Override
    public void tick() {
        super.tick();
        this.textField.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            this.sendChange(false);
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.textField.render(matrices, mouseX, mouseY, delta);
        this.textRenderer.draw(matrices, this.orderedTextTitle, (this.width - this.textRenderer.getWidth(this.orderedTextTitle)) / 2, 10, 0xFFFFFFFF);
        this.textRenderer.draw(matrices, this.maxCharsMsg, (this.width - this.textRenderer.getWidth(this.maxCharsMsg)) / 2, ((this.height - 65) / 2) - this.textRenderer.fontHeight - 5, 0xFFFFACAC);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
