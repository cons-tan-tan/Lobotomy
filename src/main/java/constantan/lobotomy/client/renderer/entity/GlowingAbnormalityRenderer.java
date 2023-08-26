package constantan.lobotomy.client.renderer.entity;

import constantan.lobotomy.common.entity.AbnormalityEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;

public class GlowingAbnormalityRenderer<T extends AbnormalityEntity<T> & IAnimatable> extends AbnormalityRenderer<T> {

    public GlowingAbnormalityRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> model) {
        this(renderManager, model, 0.0F, 1.0F);
    }

    public GlowingAbnormalityRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> model, float shadowRadius) {
        this(renderManager, model, shadowRadius, 1.0F);
    }

    public GlowingAbnormalityRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> model, float shadowRadius, float scale) {
        super(renderManager, model, shadowRadius, scale);

        addLayer(new LayerGlowingAreasGeo<>(this,
                entity -> getGeoModelProvider().getTextureLocation(entity),
                entity -> getGeoModelProvider().getModelLocation(entity),
                RenderType::eyes));
    }
}
