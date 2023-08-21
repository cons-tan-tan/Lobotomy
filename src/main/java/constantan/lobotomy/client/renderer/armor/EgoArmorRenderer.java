package constantan.lobotomy.client.renderer.armor;

import constantan.lobotomy.client.model.armor.EgoArmorModel;
import constantan.lobotomy.lib.LibEntityResources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class EgoArmorRenderer<T extends ArmorItem & IAnimatable> extends GeoArmorRenderer<T> {

    public static Map<ArmorItem, Supplier<EgoArmorRenderer>> CONSTRUCTORS = new ConcurrentHashMap<>();
    public static Map<ArmorItem, ConcurrentHashMap<UUID, EgoArmorRenderer<?>>> LIVING_ENTITY_RENDERERS = new ConcurrentHashMap<>();

    protected T assignedItem = null;

    public EgoArmorModel<?> egoArmorModel;

    public EgoArmorRenderer(LibEntityResources.EntityResourceData entityResourceData) {
        super(new EgoArmorModel<>(entityResourceData));
        this.egoArmorModel = (EgoArmorModel<?>) this.getGeoModelProvider();
    }

    @Override
    public EgoArmorRenderer<T> applySlot(EquipmentSlot slot) {
        this.getGeoModelProvider().getModel(this.getGeoModelProvider().getModelLocation(this.currentArmorItem));

        setBoneVisibility(this.headBone, false);
        setBoneVisibility(this.bodyBone, true);
        setBoneVisibility(this.rightArmBone, true);
        setBoneVisibility(this.leftArmBone, true);
        setBoneVisibility(this.rightLegBone, true);
        setBoneVisibility(this.leftLegBone, true);
        setBoneVisibility(this.rightBootBone, false);
        setBoneVisibility(this.rightBootBone, false);
        setBoneVisibility(this.leftBootBone, false);

        return this;
    }

    @Nullable
    @Override
    public IAnimatableModel<T> apply(IAnimatable t) {
        if (t instanceof ArmorItem && t == this.assignedItem) {
            return this.getGeoModelProvider();
        }
        return null;
    }

    public static void registerEgoArmorRenderer(Item item,
                                             Supplier<EgoArmorRenderer> rendererConstructor) {
        CONSTRUCTORS.put((ArmorItem) item, rendererConstructor);
        LIVING_ENTITY_RENDERERS.put((ArmorItem) item, new ConcurrentHashMap<>());
    }

    public static EgoArmorRenderer getEgoArmorRenderer(ArmorItem item, final Entity wearer) {
        ConcurrentHashMap<UUID, EgoArmorRenderer<?>> renderers = LIVING_ENTITY_RENDERERS.get(item);
        EgoArmorRenderer armorRenderer;
        UUID uuid = wearer.getUUID();

        if (renderers == null || (armorRenderer = renderers.get(uuid)) == null) {
            armorRenderer = CONSTRUCTORS.get(item).get();

            if (armorRenderer == null)
                throw new IllegalArgumentException("Renderer not registered for item " + item);

            armorRenderer.assignedItem = item;

            if (renderers == null) {
                renderers = new ConcurrentHashMap<>();

                LIVING_ENTITY_RENDERERS.put(item, renderers);
            }

            renderers.put(uuid, armorRenderer);
        }

        return armorRenderer;
    }
}
