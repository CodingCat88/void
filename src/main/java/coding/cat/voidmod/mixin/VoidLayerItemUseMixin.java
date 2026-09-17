package coding.cat.voidmod.mixin;

import coding.cat.voidmod.events.VoidLayerRestrictions;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class VoidLayerItemUseMixin {

    @Inject(
            method = "interactItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockItemUse(
            ServerPlayerEntity player,
            World world,
            ItemStack stack,
            Hand hand,
            CallbackInfoReturnable<ActionResult> cir
    ) {
        if (VoidLayerRestrictions.isInLayer(player)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}