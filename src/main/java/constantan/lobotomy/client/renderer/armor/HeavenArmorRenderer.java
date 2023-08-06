package constantan.lobotomy.client.renderer.armor;

import constantan.lobotomy.client.model.armor.HeavenArmorModel;
import constantan.lobotomy.common.item.ego.HeavenArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class HeavenArmorRenderer extends GeoArmorRenderer<HeavenArmorItem> {
    public HeavenArmorRenderer() {
        super(new HeavenArmorModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorRightLeg";
        this.leftLegBone = "armorLeftLeg";
        this.rightBootBone = "armorRightBoot";
        this.leftBootBone = "armorLeftBoot";
    }
}
