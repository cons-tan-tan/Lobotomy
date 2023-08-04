package constantan.lobotomy.mixin;

import constantan.lobotomy.common.entity.AbnormalityEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Mob.class)
public abstract class MixinMob {
    @ModifyArg(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", ordinal = 0))
    private DamageSource doHurtTarget(DamageSource pSource) {
        Mob self = (Mob) (Object) this;
        if (self instanceof AbnormalityEntity abnormality) {
            return abnormality.getAbnormalDamageSource(abnormality.getDamageType(), !abnormality.canDoUnblockableAttack());//引数を弄れるようにする
        }
        return pSource;
    }
}
