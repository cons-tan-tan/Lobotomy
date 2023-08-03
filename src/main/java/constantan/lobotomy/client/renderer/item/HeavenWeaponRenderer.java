package constantan.lobotomy.client.renderer.item;

import constantan.lobotomy.client.model.item.HeavenWeaponModel;
import constantan.lobotomy.common.item.HeavenWeaponItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class HeavenWeaponRenderer extends GeoItemRenderer<HeavenWeaponItem> {
    public HeavenWeaponRenderer() {
        super(new HeavenWeaponModel());
    }
}
