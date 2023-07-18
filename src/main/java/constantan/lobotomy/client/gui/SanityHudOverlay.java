package constantan.lobotomy.client.gui;


import com.mojang.blaze3d.systems.RenderSystem;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.IIngameOverlay;

public class SanityHudOverlay {
    private static final ResourceLocation SANITY_BAR = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/sanity_bar.png");


    public static final IIngameOverlay HUD_SANITY = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SANITY_BAR);
        GuiComponent.blit(poseStack, x - 91, y - 54, 0, 0, 182, 5, 182, 10);

        int bar_width = (int) (182 * ((float) ClientSanityData.getPlayerSanity() / (float) ClientSanityData.getPlayerMaxSanity()));
        GuiComponent.blit(poseStack, x - 91, y - 54, 0, 5, bar_width, 5, 182, 10);
    });
}
