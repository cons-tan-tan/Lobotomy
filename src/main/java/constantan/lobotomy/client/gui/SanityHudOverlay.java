package constantan.lobotomy.client.gui;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import constantan.lobotomy.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.IIngameOverlay;

public class SanityHudOverlay {
    private static final ResourceLocation SANITY_BAR = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/sanity_bar.png");

    private static final int textureWidth = 10;
    private static final int textureHeight = 182;

    private static int bar_width_from;
    private static int bar_width_to;

    /**
     * {@link RenderStateShard#TRANSLUCENT_TRANSPARENCY}のコピペ
     */
    private static final RenderStateShard.TransparencyStateShard TRANSLUCENT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public static final IIngameOverlay HUD_SANITY = ((gui, poseStack, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.player.getAbilities().invulnerable && !minecraft.options.hideGui) {
            int x = width - 10;
            int y = (height / 2) - 91;

            //半透明表示を有効化
            TRANSLUCENT_TRANSPARENCY.setupRenderState();

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            //TRANSLUCENT_TRANSPARENCYを使わないとpAlphaの透過率が有効にならない
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, SANITY_BAR);
            GuiComponent.blit(poseStack, x, y, 0, 0, 5, 182, textureWidth, textureHeight);

            int bar_height = (int) (textureHeight * ((float) ClientSanityData.getPlayerSanity() / (float) ClientSanityData.getPlayerMaxSanity()));
            GuiComponent.blit(poseStack, x, y + (textureHeight - bar_height), 5, textureHeight - bar_height, 5, bar_height, textureWidth, textureHeight);

            //半透明表示を無効化
            TRANSLUCENT_TRANSPARENCY.clearRenderState();
        }
    });
}
