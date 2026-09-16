package coding.cat.voidmod.client;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

public final class VoidShaderEffect {

    private static final ManagedShaderEffect VOID_LAYER_SHADER = ShaderEffectManager.getInstance()
            .manage(Identifier.of("void", "shaders/post/void_layer.json"));

    private VoidShaderEffect() {
    }

    public static void register() {
        ShaderEffectRenderCallback.EVENT.register(VoidShaderEffect::renderIfInLayer);
    }

    private static void renderIfInLayer(float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }

        boolean inLayer = ((VoidPlayerComponent) client.player.getComponent(VoidComponents.PLAYER)).inLayer;
        if (inLayer) {
            VOID_LAYER_SHADER.render(tickDelta);
        }
    }
}
