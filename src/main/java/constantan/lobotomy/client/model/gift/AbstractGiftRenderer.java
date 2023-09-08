package constantan.lobotomy.client.model.gift;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public abstract class AbstractGiftRenderer<T extends Entity> extends EntityModel<T> {

    public abstract ModelPart[] getParts();
}
