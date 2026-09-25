package coding.cat.voidmod.mixin;

import coding.cat.voidmod.events.IgnoresBlindness;
import coding.cat.voidmod.events.VoidLayerRestrictions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class VoidLayerDamageMixin {

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void voidmod$blockDamage(
            DamageSource source,
            float amount,
            CallbackInfoReturnable<Boolean> cir
    ) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player
                && VoidLayerRestrictions.isInLayer(player)
                && !(source.getAttacker() instanceof IgnoresBlindness)) {
            cir.setReturnValue(false);
        }
    }
}
