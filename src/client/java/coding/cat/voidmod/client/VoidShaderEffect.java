package coding.cat.voidmod.client;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

public final class VoidShaderEffect {

    private static final ManagedShaderEffect VOID_LAYER_SHADER = ShaderEffectManager.getInstance()
            .manage(Identifier.of("void", "shaders/post/void_layer.json"));


    private static final long startTimeNanos = System.nanoTime();

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
        if (!inLayer) {
            return;
        }

        float elapsedSeconds = (System.nanoTime() - startTimeNanos) / 1_000_000_000.0f;
        VOID_LAYER_SHADER.setUniformValue("SwirlTime", elapsedSeconds);

        Window window = client.getWindow();
        VOID_LAYER_SHADER.setUniformValue("ViewPort", 0, 0, window.getFramebufferWidth(), window.getFramebufferHeight());

        VOID_LAYER_SHADER.render(tickDelta);
    }
}
