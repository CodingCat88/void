package coding.cat.voidmod.effect;

import coding.cat.voidmod.VoidsReturn;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class VoidStatusEffects {

    private static final RegistryKey<StatusEffect> VOID_MARKED_KEY =
            RegistryKey.of(RegistryKeys.STATUS_EFFECT, Identifier.of(VoidsReturn.MOD_ID, "void_marked"));

    public static final RegistryEntry<StatusEffect> VOID_MARKED;

    static {
        Registry.register(
                Registries.STATUS_EFFECT,
                VOID_MARKED_KEY,
                new StatusEffect(StatusEffectCategory.HARMFUL, 0x1a0330) {
                }
        );
        VOID_MARKED = Registries.STATUS_EFFECT.getEntry(VOID_MARKED_KEY).orElseThrow();
    }

    private VoidStatusEffects() {
    }

    // Referencing this class from VoidMod.onInitialize() forces the static
    // block above to run - this method just makes that intent explicit.
    public static void register() {
    }
}
