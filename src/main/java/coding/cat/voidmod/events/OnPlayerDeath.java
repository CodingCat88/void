package coding.cat.voidmod.events;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import coding.cat.voidmod.item.VoidItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class OnPlayerDeath implements ServerPlayerEvents.AllowDeath{
    @Override
    public boolean allowDeath(ServerPlayerEntity player, DamageSource damageSource, float v) {
        if (player.getInventory().contains(new ItemStack(VoidItems.NULL_TOTEM))) {
            player.setHealth(player.getMaxHealth());
            if (!((VoidPlayerComponent)player.getComponent(VoidComponents.PLAYER)).inLayer) {
                ((VoidPlayerComponent)player.getComponent(VoidComponents.PLAYER)).inLayer = true;
            }
            return false;
        }

        return true;
    }
}
