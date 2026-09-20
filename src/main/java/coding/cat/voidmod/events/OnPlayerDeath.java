package coding.cat.voidmod.events;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import coding.cat.voidmod.item.VoidItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnPlayerDeath implements ServerPlayerEvents.AllowDeath {

    private static final int COUNTDOWN_TICKS = 600;
    private static final Map<UUID, Integer> countdowns = new HashMap<>();

    public static void register() {
        LayerKeybindEvent.CAN_EXIT_LAYER.register(player -> !countdowns.containsKey(player.getUuid()));

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            countdowns.replaceAll((uuid, ticks) -> ticks - 1);

            countdowns.entrySet().removeIf(entry -> {
                if (entry.getValue() <= 0) {
                    ServerPlayerEntity player =
                            server.getPlayerManager().getPlayer(entry.getKey());

                    if (player != null) {
                        VoidPlayerComponent component =
                                (VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER);

                        if (component.inLayer) {
                            component.inLayer = false;
                            VoidComponents.PLAYER.sync(player);
                        }
                    }

                    return true;
                }

                return false;
            });
        });
    }

    public static void removeCountdown(UUID uuid) {
        countdowns.remove(uuid);
    }

    @Override
    public boolean allowDeath(ServerPlayerEntity player, DamageSource damageSource, float v) {
        if (player.getInventory().contains(new ItemStack(VoidItems.NULL_TOTEM))) {

            player.setHealth(player.getMaxHealth());

            VoidPlayerComponent component =
                    (VoidPlayerComponent) player.getComponent(VoidComponents.PLAYER);

            if (!component.inLayer) {
                component.inLayer = true;

                VoidComponents.PLAYER.sync(player);

                // Start the 30 second countdown
                countdowns.put(player.getUuid(), COUNTDOWN_TICKS);
            }

            return false;
        }

        return true;
    }
}