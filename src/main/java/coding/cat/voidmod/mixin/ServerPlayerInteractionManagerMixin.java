package coding.cat.voidmod.mixin;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    private boolean voidmod$isInLayer(ServerPlayerEntity player) {
        VoidPlayerComponent component =
                (VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER);

        return component.inLayer;
    }

    @Inject(
            method = "interactBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockInteraction(
            ServerPlayerEntity player,
            World world,
            ItemStack stack,
            Hand hand,
            BlockHitResult hitResult,
            CallbackInfoReturnable<ActionResult> cir
    ) {
        if (voidmod$isInLayer(player)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

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
        if (voidmod$isInLayer(player)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}