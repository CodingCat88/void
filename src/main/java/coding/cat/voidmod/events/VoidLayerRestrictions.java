package coding.cat.voidmod.events;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.player.PlayerEntity;

public class VoidLayerRestrictions {

    public static boolean isInLayer(PlayerEntity player) {
        VoidPlayerComponent component =
                (VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER);

        return component.inLayer;
    }

    public static void register() {

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {

            if (entity instanceof net.minecraft.server.network.ServerPlayerEntity player) {

                if (isInLayer(player) && !(source.getAttacker() instanceof IgnoresBlindness)) {
                    return false;
                }
            }

            return true;
        });
    }
}