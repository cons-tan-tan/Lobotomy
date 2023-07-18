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
        for (int i = 0; i < 20; i++) {
            GuiComponent.blit(poseStack, x - 92 + (i * 8), y - 54, 0, 0, 10, 10, 10, 10 );
        }

        RenderSystem.setShaderTexture(0, FILLED_SANITY);
        for (int i = 0; i < 20; i++) {
            if (ClientSanityData.getPlayerSanity() > i) {
                GuiComponent.blit(poseStack, x - 92 + (i * 8), y - 54, 0, 0, 10, 10, 10, 10 );
            } else {
                break;
            }
        }
    });
}
