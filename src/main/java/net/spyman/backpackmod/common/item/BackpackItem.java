package net.spyman.backpackmod.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.spyman.backpackmod.client.gui.screen.BackpackRenameScreen;
import net.spyman.backpackmod.common.BackpackMod;
import net.spyman.backpackmod.common.inventory.BackpackInventory;
import net.spyman.backpackmod.common.inventory.BackpackScreenHandler;

import java.util.List;

import static net.spyman.backpackmod.common.BackpackMod.translate;

public class BackpackItem extends Item {

    private final int width;
    private final int height;

    public BackpackItem(int width, int height, Settings settings) {
        super(settings.group(BackpackMod.GROUP).maxCount(1));
        this.width = width;
        this.height = height;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        final ItemStack stack = user.getStackInHand(hand);

        if (!user.isSneaking()) {
            if (!world.isClient()) {
                openScreen(user, hand);
            } else {
                user.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
            }

            return TypedActionResult.consume(stack);
        } else if (world.isClient()) {
            openRenameScreen(hand, stack.getName());
            return TypedActionResult.pass(stack);
        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(translate("tooltip.size", this.width, this.height, this.width * this.height).formatted(Formatting.WHITE));
        tooltip.add(translate("tooltip.how.to.rename").formatted(Formatting.GRAY, Formatting.ITALIC));

        if (this.isFireproof()) {
            tooltip.add(translate("tooltip.fireproof").formatted(Formatting.GOLD));
        }
    }

    public static final void openScreen(PlayerEntity user, Hand hand) {
        final var stack = user.getStackInHand(hand);
        final var bp = (BackpackItem) user.getStackInHand(hand).getItem();

        user.openHandledScreen(new ExtendedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return stack.getName();
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new BackpackScreenHandler(inv, syncId, new BackpackInventory(bp.width, bp.height, hand));
            }

            @Override
            public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf buf) {
                buf.writeInt(bp.width);
                buf.writeInt(bp.height);
                buf.writeEnumConstant(hand);
            }
        });
    }

    @Environment(EnvType.CLIENT)
    public static final void openRenameScreen(Hand hand, Text name) {
        MinecraftClient.getInstance().setScreen(new BackpackRenameScreen(hand, name));
    }
}
