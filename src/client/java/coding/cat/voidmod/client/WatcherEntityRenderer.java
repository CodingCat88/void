package coding.cat.voidmod.client;

import coding.cat.voidmod.entity.e.WatcherEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class WatcherEntityRenderer extends MobEntityRenderer<WatcherEntity, BipedEntityModel<WatcherEntity>> {

    private static final Identifier TEXTURE = Identifier.of("void", "textures/entity/thousand_eyes.png");

    public WatcherEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER_SLIM)), 0.5F);
    }

    @Override
    public Identifier getTexture(WatcherEntity entity) {
        return TEXTURE;
    }

    @Override
    public RenderLayer getRenderLayer(WatcherEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return RenderLayer.getEntityTranslucent(getTexture(entity));
    }
}
