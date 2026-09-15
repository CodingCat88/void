package coding.cat.voidmod.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class VoidPlayerComponent implements AutoSyncedComponent {

    public boolean inLayer = false;
    private final PlayerEntity player;

    public VoidPlayerComponent(PlayerEntity player) {
        this.player = player;
        this.inLayer = false;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.inLayer = nbtCompound.getBoolean("in_layer");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putBoolean("in_layer", this.inLayer);
    }
}
