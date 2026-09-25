package coding.cat.voidmod.entity;

import coding.cat.voidmod.VoidsReturn;
import coding.cat.voidmod.entity.e.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class VoidEntities {


    public static final EntityType<WatcherEntity> VOID_WATCHER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(VoidsReturn.MOD_ID, "void_watcher"),
            EntityType.Builder.create(WatcherEntity::new, SpawnGroup.MONSTER).dimensions(0.6F, 1.95F).build()
            );


    private VoidEntities() {
    }

    public static void register() {
        FabricDefaultAttributeRegistry.register(VOID_WATCHER, WatcherEntity.createVoidWatcherAttributes());
    }
}
