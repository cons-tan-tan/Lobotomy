package constantan.lobotomy.client.model.item;

import constantan.lobotomy.lib.LibAbnormality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AbnormalityToolItemModel<T extends Item & IAnimatable> extends AnimatedGeoModel<T> {

    protected final LibAbnormality.ToolResourceData toolResourceData;

    public AbnormalityToolItemModel(LibAbnormality.ToolResourceData toolResourceData) {
        super();
        this.toolResourceData = toolResourceData;
    }

    @Override
    public ResourceLocation getModelLocation(T object) {
        return this.toolResourceData.getModel();
    }

    @Override
    public ResourceLocation getTextureLocation(T object) {
        return this.toolResourceData.getTexture();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T animatable) {
        return this.toolResourceData.getAnimation();
    }
}
