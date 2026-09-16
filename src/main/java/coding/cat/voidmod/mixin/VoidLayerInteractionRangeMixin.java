package coding.cat.voidmod.mixin;

import coding.cat.voidmod.events.VoidLayerRestrictions;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class VoidLayerInteractionRangeMixin {

    @Inject(
            method = "getBlockInteractionRange",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockInteractionRange(
            CallbackInfoReturnable<Double> cir
    ) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (VoidLayerRestrictions.isInLayer(player)) {
            cir.setReturnValue(0.0);
        }
    }

    @Inject(
            method = "getEntityInteractionRange",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$entityInteractionRange(
            CallbackInfoReturnable<Double> cir
    ) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (VoidLayerRestrictions.isInLayer(player)) {
            cir.setReturnValue(0.0);
        }
    }
}