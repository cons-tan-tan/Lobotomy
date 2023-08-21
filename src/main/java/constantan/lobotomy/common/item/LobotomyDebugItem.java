package constantan.lobotomy.common.item;

import constantan.lobotomy.LobotomyMod;
import constantan.lobotomy.common.capability.sanity.PlayerSanityProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class LobotomyDebugItem extends Item {
    private int mode;

    public LobotomyDebugItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide) {
            LobotomyMod.logger.info(UUID.randomUUID().toString());
            if (pPlayer.isSteppingCarefully()) {

            } else {
                switch (mode) {
                    case 0:
                        pPlayer.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                            sanity.addSanityWithSync(-1, (ServerPlayer) pPlayer);
                        });
                }
            }
        }
        return InteractionResultHolder.fail(stack);
    }
}
