package coding.cat.voidmod.component;

import coding.cat.voidmod.VoidsReturn;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class VoidComponents implements EntityComponentInitializer {
    public static final ComponentKey<VoidPlayerComponent> PLAYER =
            ComponentRegistry.getOrCreate(Identifier.of(VoidsReturn.MOD_ID, "player"), VoidPlayerComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PLAYER, VoidPlayerComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
