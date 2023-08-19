package constantan.lobotomy.mixin;

import constantan.lobotomy.client.layer.EgoSuitLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandRenderer.class)
public class MixinArmorStandRenderer {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context p_173915_, CallbackInfo ci) {
        ArmorStandRenderer self = (ArmorStandRenderer) (Object) this;
        self.addLayer(new EgoSuitLayer<>(self, new PlayerModel<>(p_173915_.bakeLayer(ModelLayers.PLAYER), false)));
    }
}
