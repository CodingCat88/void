package coding.cat.voidmod.mixin;

import coding.cat.voidmod.events.VoidLayerRestrictions;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class VoidLayerPlayerMixin {

    @Inject(
            method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockDrop(
            ItemStack stack,
            boolean throwRandomly,
            boolean retainOwnership,
            CallbackInfoReturnable<ItemEntity> cir
    ) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (VoidLayerRestrictions.isInLayer(player)) {
            cir.setReturnValue(null);
        }
    }
}