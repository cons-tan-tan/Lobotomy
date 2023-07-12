package constantan.lobotomy.client.renderer.item;

import constantan.lobotomy.client.model.item.PeakWeaponModel;
import constantan.lobotomy.common.item.PeakWeaponItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class PeakWeaponRenderer extends GeoItemRenderer<PeakWeaponItem> {
    public PeakWeaponRenderer() {
        super(new PeakWeaponModel());
    }
}
