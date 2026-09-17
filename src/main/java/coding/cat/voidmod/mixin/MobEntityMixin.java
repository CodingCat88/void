package coding.cat.voidmod.mixin;

import coding.cat.voidmod.component.VoidComponents;
import coding.cat.voidmod.component.VoidPlayerComponent;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void void$entitysDontSee(CallbackInfo info) {
        MobEntity mob = (MobEntity)(Object)this;
        if (mob.getTarget() instanceof PlayerEntity hidden) {
            if (((VoidPlayerComponent)hidden.getComponent(VoidComponents.PLAYER)).inLayer) {
                mob.setTarget(null);
            }
        }
    }
}
