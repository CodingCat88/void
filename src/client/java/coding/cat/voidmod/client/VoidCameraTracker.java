package coding.cat.voidmod.client;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public final class VoidCameraTracker {

    private static Matrix4f lastInverseViewProjMatrix = new Matrix4f();
    private static Vec3d lastCameraPos = Vec3d.ZERO;

    private VoidCameraTracker() {
    }

    public static void register() {
        WorldRenderEvents.END.register(context -> {
            Matrix4f viewProj = new Matrix4f(context.projectionMatrix())
                    .mul(context.matrixStack().peek().getPositionMatrix());
            viewProj.invert();

            lastInverseViewProjMatrix = viewProj;
            lastCameraPos = context.camera().getPos();
        });
    }

    public static Matrix4f getInverseViewProjMatrix() {
        return lastInverseViewProjMatrix;
    }

    public static Vec3d getCameraPos() {
        return lastCameraPos;
    }
}
