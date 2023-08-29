package constantan.lobotomy.client.model.entity;

import constantan.lobotomy.common.entity.custom.PunishingBird;
import constantan.lobotomy.lib.LibAbnormality;
import net.minecraft.resources.ResourceLocation;

public class PunishingBirdModel extends AbnormalityModel<PunishingBird> {

    public PunishingBirdModel(LibAbnormality.EntityResourceData entityResourceData) {
        super(entityResourceData);
    }

    @Override
    public ResourceLocation getTextureLocation(PunishingBird object) {
        if (object.isAngry()) return this.entityResourceData.getTexture("angry");
        return this.entityResourceData.getTexture("normal");
    }
}
