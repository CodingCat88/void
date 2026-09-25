package coding.cat.voidmod.client;

import coding.cat.voidmod.entity.VoidEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public final class VoidEntityRenderers {

    private VoidEntityRenderers() {
    }

    public static void register() {
        EntityRendererRegistry.register(VoidEntities.VOID_WATCHER, WatcherEntityRenderer::new);
    }
}
