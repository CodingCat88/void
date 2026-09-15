package coding.cat.voidmod.events;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class VoidLayerRestrictions {

    public static void register() {

        // Prevent breaking blocks
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            VoidPlayerComponent component =
                    (VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER);

            if (component.inLayer) {
                return false;
            }

            return true;
        });

        // Prevent placing/interacting with blocks
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            VoidPlayerComponent component =
                    (VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER);

            if (component.inLayer) {
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });

        // Prevent using items
        UseItemCallback.EVENT.register((player, world, hand) -> {
            VoidPlayerComponent component =
                    (VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER);

            if (component.inLayer) {
                return TypedActionResult.fail(player.getStackInHand(hand));
            }

            return TypedActionResult.pass(player.getStackInHand(hand));
        });

        // Prevent interacting with entities
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            VoidPlayerComponent component =
                    (VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER);

            if (component.inLayer) {
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });

        // Prevent attacking entities
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            VoidPlayerComponent component =
                    (VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER);

            if (component.inLayer) {
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });

        // Prevent taking damage
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof net.minecraft.server.network.ServerPlayerEntity player) {
                VoidPlayerComponent component =
                        (VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER);

                if (component.inLayer) {
                    return false;
                }
            }

            return true;
        });
    }
}