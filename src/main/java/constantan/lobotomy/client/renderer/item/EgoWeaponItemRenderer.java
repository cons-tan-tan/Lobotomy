package constantan.lobotomy.client.renderer.item;

import constantan.lobotomy.client.model.item.EgoWeaponItemModel;
import constantan.lobotomy.lib.LibAbnormality;
import net.minecraft.world.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class EgoWeaponItemRenderer<T extends Item & IAnimatable> extends GeoItemRenderer<T> {

    public EgoWeaponItemRenderer(LibAbnormality.EntityResourceData entityResourceData) {
        super(new EgoWeaponItemModel<>(entityResourceData));
    }
}
