package coding.cat.voidmod.chat;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class LayerChatHandler {

    private LayerChatHandler() {
    }

    public static void register() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            if (!needsSplitDelivery(sender)) {
                return true;
            }
            broadcast(sender, message.getContent());
            return false;
        });
    }

    private static boolean needsSplitDelivery(ServerPlayerEntity sender) {
        MinecraftServer server = sender.getServer();
        if (server == null) {
            return false;
        }

        boolean senderInLayer = ((VoidPlayerComponent) sender.getComponent(VoidComponents.PLAYER)).inLayer;
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            boolean playerInLayer = ((VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER)).inLayer;
            if (playerInLayer != senderInLayer) {
                return true;
            }
        }
        return false;
    }

    private static void broadcast(ServerPlayerEntity sender, Text content) {
        MinecraftServer server = sender.getServer();
        if (server == null) {
            return;
        }

        boolean senderInLayer = ((VoidPlayerComponent) sender.getComponent(VoidComponents.PLAYER)).inLayer;
        String plainContent = content.getString();
        String senderName = sender.getName().getString();

        for (ServerPlayerEntity recipient : server.getPlayerManager().getPlayerList()) {
            boolean recipientInLayer = ((VoidPlayerComponent) recipient.getComponent(VoidComponents.PLAYER)).inLayer;
            boolean sameSide = senderInLayer == recipientInLayer;

            Text name = sameSide
                    ? Text.literal(senderName)
                    : Text.literal(senderName).formatted(Formatting.OBFUSCATED);
            Text body = sameSide
                    ? Text.literal(plainContent)
                    : Text.literal(plainContent).formatted(Formatting.OBFUSCATED);

            Text fullMessage = Text.literal("<").append(name).append(Text.literal("> ")).append(body);
            recipient.sendMessage(fullMessage);
        }
    }
}
