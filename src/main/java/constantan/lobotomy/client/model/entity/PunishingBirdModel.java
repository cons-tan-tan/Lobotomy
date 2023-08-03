package constantan.lobotomy.client.model.entity;

import constantan.lobotomy.common.entity.PunishingBirdEntity;
import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.resources.ResourceLocation;

public class PunishingBirdModel extends AbnormalityEntityModel<PunishingBirdEntity> {

    public PunishingBirdModel(LibEntityResources.EntityResourceData entityResourceData) {
        super(entityResourceData);
    }

    @Override
    public ResourceLocation getTextureLocation(PunishingBirdEntity object) {
        if (object.isAngry()) return this.entityResourceData.getTexture("angry");
        return this.entityResourceData.getTexture("normal");
    }
}
