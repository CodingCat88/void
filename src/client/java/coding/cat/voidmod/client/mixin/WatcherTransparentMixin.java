package coding.cat.voidmod.client.mixin;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import coding.cat.voidmod.entity.e.WatcherEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class WatcherTransparentMixin {

    @Unique
    private static final float OUTSIDE_LAYER_ALPHA = 0.25F;

    @Inject(
            method = "render(Lnet/minecraft/entity/Entity;DDDFFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD")
    )
    private void voidmod$beforeWatcherRender(Entity entity, double x, double y, double z, float yaw, float tickDelta,
                                             MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!(entity instanceof WatcherEntity)) {
            return;
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alphaForLocalViewer());
    }

    @Inject(
            method = "render(Lnet/minecraft/entity/Entity;DDDFFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("TAIL")
    )
    private void voidmod$afterWatcherRender(Entity entity, double x, double y, double z, float yaw, float tickDelta,
                                            MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!(entity instanceof WatcherEntity)) {
            return;
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Unique
    private static float alphaForLocalViewer() {
        PlayerEntity viewer = MinecraftClient.getInstance().player;
        if (viewer == null) {
            return 1.0F;
        }
        boolean viewerInLayer = ((VoidPlayerComponent) viewer.getComponent(VoidComponents.PLAYER)).inLayer;
        return viewerInLayer ? 1.0F : OUTSIDE_LAYER_ALPHA;
    }
}
