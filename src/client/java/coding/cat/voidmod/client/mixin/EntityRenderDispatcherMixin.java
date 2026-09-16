package coding.cat.voidmod.client.mixin;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    @Inject(
            method = "render(Lnet/minecraft/entity/Entity;DDDFFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$hideAcrossLayers(Entity entity, double x, double y, double z, float yaw, float tickDelta,
                                          MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!(entity instanceof PlayerEntity target)) {
            return;
        }

        PlayerEntity viewer = MinecraftClient.getInstance().player;
        if (viewer == null || viewer == target) {
            return;
        }

        boolean viewerInLayer = ((VoidPlayerComponent) viewer.getComponent(VoidComponents.PLAYER)).inLayer;
        boolean targetInLayer = ((VoidPlayerComponent) target.getComponent(VoidComponents.PLAYER)).inLayer;

        if (viewerInLayer != targetInLayer) {
            ci.cancel();
        }
    }
}
