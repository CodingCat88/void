package coding.cat.voidmod.client.mixin;

import coding.cat.voidmod.events.VoidLayerRestrictions;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class VoidLayerClientPlayerMixin {

    @Inject(
            method = "dropSelectedItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockDrop(
            boolean entireStack,
            CallbackInfoReturnable<Boolean> cir
    ) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

        if (VoidLayerRestrictions.isInLayer(player)) {
            cir.setReturnValue(false);
        }
    }
}