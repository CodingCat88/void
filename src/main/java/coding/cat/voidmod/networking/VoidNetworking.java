package coding.cat.voidmod.networking;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import coding.cat.voidmod.events.LayerKeybindEvent;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class VoidNetworking {

    private VoidNetworking() {
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(ExitLayerPayload.ID, ExitLayerPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ExitLayerPayload.ID, (payload, context) ->
                context.server().execute(() -> {
                    ServerPlayerEntity player = context.player();
                    VoidPlayerComponent component = (VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER);
                    if (!component.inLayer) {
                        // Not actually in the Layer - ignore rather than trust the client blindly.
                        return;
                    }

                    if (!LayerKeybindEvent.CAN_EXIT_LAYER.invoker().canExit(player)) {
                        player.sendMessage(Text.literal("You can't run"), true);
                        return;
                    }

                    component.inLayer = false;
                    VoidComponents.PLAYER.sync(player);
                }));
    }
}
