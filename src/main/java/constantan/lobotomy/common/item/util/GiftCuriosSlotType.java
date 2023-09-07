package constantan.lobotomy.common.item.util;

import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

public enum GiftCuriosSlotType {
    HEAD_1,
    HEAD_2,
    EYE,
    MOUSE_1,
    MOUSE_2,
    CHEEK,
    BROOCH_1,
    BROOCH_2,
    HAND_1,
    HAND_2,
    FACE,
    RIGHT_BACK,
    LEFT_BACK,
    SPECIAL;

    public String getIdentifier() {
        return "gift_" + this.toString().toLowerCase();
    }

    public int getPriority() {
        return -(this.ordinal() + 653);
    }

    public SlotTypeMessage.Builder getMessageBuilder() {
        return new SlotTypeMessage.Builder(this.getIdentifier()).priority(this.getPriority()).icon(
                new ResourceLocation(CuriosApi.MODID, "item/empty_curio_slot"));
    }
}
