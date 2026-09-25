package coding.cat.voidmod.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class WatcherAttackLock {

    public static final int LOCK_TICKS = 5 * 20;

    private static final Map<UUID, Integer> locks = new ConcurrentHashMap<>();

    private WatcherAttackLock() {
    }

    public static void register() {
        LayerKeybindEvent.CAN_EXIT_LAYER.register(player -> !locks.containsKey(player.getUuid()));
        ServerTickEvents.END_SERVER_TICK.register(WatcherAttackLock::tick);
    }

    public static void lock(UUID playerId) {
        locks.put(playerId, LOCK_TICKS);
    }

    private static void tick(MinecraftServer server) {
        locks.replaceAll((uuid, ticksRemaining) -> ticksRemaining - 1);
        locks.values().removeIf(ticksRemaining -> ticksRemaining <= 0);
    }
}
