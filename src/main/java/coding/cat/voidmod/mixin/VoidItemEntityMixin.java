package coding.cat.voidmod.mixin;

import coding.cat.voidmod.events.VoidLayerRestrictions;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class VoidItemEntityMixin {

    @Inject(
            method = "onPlayerCollision",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockPickup(
            PlayerEntity player,
            CallbackInfo ci
    ) {
        if (VoidLayerRestrictions.isInLayer(player)) {
            ci.cancel();
        }
    }
}