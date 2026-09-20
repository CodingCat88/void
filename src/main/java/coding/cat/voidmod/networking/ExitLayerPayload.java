package coding.cat.voidmod.networking;

import coding.cat.voidmod.VoidsReturn;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ExitLayerPayload() implements CustomPayload {

    public static final Id<ExitLayerPayload> ID = new Id<>(Identifier.of(VoidsReturn.MOD_ID, "exit_layer"));
    public static final PacketCodec<PacketByteBuf, ExitLayerPayload> CODEC = PacketCodec.unit(new ExitLayerPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
