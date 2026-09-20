package coding.cat.voidmod.client;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import coding.cat.voidmod.networking.ExitLayerPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class LayerExitKeybind {

    private static final KeyBinding EXIT_LAYER_KEY = new KeyBinding(
            "key.void.exit_layer",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_BACKSLASH,
            "key.categories.void"
    );

    private LayerExitKeybind() {
    }

    public static void register() {
        KeyBindingHelper.registerKeyBinding(EXIT_LAYER_KEY);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (EXIT_LAYER_KEY.wasPressed()) {
                requestExit(client);
            }
        });
    }

    private static void requestExit(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        boolean inLayer = ((VoidPlayerComponent) client.player.getComponent(VoidComponents.PLAYER)).inLayer;
        if (inLayer) {
            ClientPlayNetworking.send(new ExitLayerPayload());
        }
    }
}
