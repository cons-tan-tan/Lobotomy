package constantan.lobotomy.client.gui;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.IIngameOverlay;

public class SanityHudOverlay {
    private static final ResourceLocation FILLED_SANITY = new ResourceLocation("minecraft",
            "textures/item/water_bucket.png");
    private static final ResourceLocation EMPTY_SANITY = new ResourceLocation("minecraft",
            "textures/item/bucket.png");


    public static final IIngameOverlay HUD_SANITY = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, EMPTY_SANITY);
        for (int i = 0; i < 10; i++) {
            GuiComponent.blit(poseStack, x - 94 + (i * 9), y - 54, 0, 0, 12, 12, 12, 12 );
        }

        RenderSystem.setShaderTexture(0, FILLED_SANITY);
        for (int i = 0; i < 10; i++) {
//            if (ClientSanityData.getPlayerSanity() > i) {
//                GuiComponent.blit(poseStack, x - 94 + (i * 9), y - 54, 0, 0, 12, 12, 12, 12 );
//            } else {
//                break;
//            }
        }
    });
}
