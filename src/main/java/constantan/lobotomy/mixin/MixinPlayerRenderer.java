package constantan.lobotomy.mixin;

import constantan.lobotomy.client.layer.EgoSuitLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void PlayerRenderer(EntityRendererProvider.Context pContext, boolean pUseSlimModel, CallbackInfo ci) {
        PlayerRenderer self = (PlayerRenderer) (Object) this;
        self.addLayer(new EgoSuitLayer<>(self, new PlayerModel<>(pContext.bakeLayer(pUseSlimModel ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), pUseSlimModel)));
    }
}
