package coding.cat.voidmod.mixin;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import net.minecraft.network.packet.c2s.play.ChatCommandSignedC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    private boolean voidmod$isInLayer() {
        ServerPlayNetworkHandler handler =
                (ServerPlayNetworkHandler) (Object) this;

        ServerPlayerEntity player = handler.player;

        VoidPlayerComponent component =
                (VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER);

        return component.inLayer;
    }

    // Block block breaking, dropping items, swapping hands, etc.
    @Inject(
            method = "onPlayerAction",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockPlayerAction(
            PlayerActionC2SPacket packet,
            CallbackInfo ci
    ) {
        if (voidmod$isInLayer()) {
            ci.cancel();
        }
    }

    // Block block interaction and placement.
    @Inject(
            method = "onPlayerInteractBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockBlockInteraction(
            PlayerInteractBlockC2SPacket packet,
            CallbackInfo ci
    ) {
        if (voidmod$isInLayer()) {
            ci.cancel();
        }
    }

    // Block entity interaction / attacks.
    @Inject(
            method = "onPlayerInteractEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockEntityInteraction(
            PlayerInteractEntityC2SPacket packet,
            CallbackInfo ci
    ) {
        if (voidmod$isInLayer()) {
            ci.cancel();
        }
    }

    // Block item use, including eating and drinking.
    @Inject(
            method = "onPlayerInteractItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockItemUse(
            PlayerInteractItemC2SPacket packet,
            CallbackInfo ci
    ) {
        if (voidmod$isInLayer()) {
            ci.cancel();
        }
    }

    // Block inventory/container clicks.
    @Inject(
            method = "onClickSlot",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockInventoryClick(
            ClickSlotC2SPacket packet,
            CallbackInfo ci
    ) {
        if (voidmod$isInLayer()) {
            ci.cancel();
        }
    }

    // Block changing the selected hotbar slot.
    @Inject(
            method = "onUpdateSelectedSlot",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockSelectedSlot(
            UpdateSelectedSlotC2SPacket packet,
            CallbackInfo ci
    ) {
        if (voidmod$isInLayer()) {
            ci.cancel();
        }
    }

    // Block normal chat.
    @Inject(
            method = "onChatMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockChat(
            ChatMessageC2SPacket packet,
            CallbackInfo ci
    ) {
        if (voidmod$isInLayer()) {
            ci.cancel();
        }
    }

    // Block slash commands.
    @Inject(
            method = "onCommandExecution",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockCommands(
            CommandExecutionC2SPacket packet,
            CallbackInfo ci
    ) {
        if (voidmod$isInLayer()) {
            ci.cancel();
        }
    }

    // Block signed chat commands.
    @Inject(
            method = "onChatCommandSigned",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockSignedCommands(
            ChatCommandSignedC2SPacket packet,
            CallbackInfo ci
    ) {
        if (voidmod$isInLayer()) {
            ci.cancel();
        }
    }

    // Block hand swings so the player cannot perform an action
    // that only sends a swing packet.
    @Inject(
            method = "onHandSwing",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockHandSwing(
            HandSwingC2SPacket packet,
            CallbackInfo ci
    ) {
        if (voidmod$isInLayer()) {
            ci.cancel();
        }
    }
}