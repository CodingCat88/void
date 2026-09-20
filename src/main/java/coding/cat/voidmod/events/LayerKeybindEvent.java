package coding.cat.voidmod.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public final class LayerKeybindEvent {

    private LayerKeybindEvent() {
    }

    public static final Event<CanExitLayer> CAN_EXIT_LAYER = EventFactory.createArrayBacked(CanExitLayer.class,
            listeners -> player -> {
                for (CanExitLayer listener : listeners) {
                    if (!listener.canExit(player)) {
                        return false;
                    }
                }
                return true;
            });

    @FunctionalInterface
    public interface CanExitLayer {
        boolean canExit(ServerPlayerEntity player);
    }
}
