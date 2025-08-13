package avox.totemvignette.mixin;

import avox.totemvignette.ConfigSystem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Final @Shadow private static Identifier VIGNETTE_TEXTURE;
	@Shadow @Final private MinecraftClient client;

	@Inject(
			method = "renderVignetteOverlay",
			at = @At(value = "INVOKE", args = "HEAD", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIFFIIIII)V"),
			cancellable = true
	)
	private void renderFullVignetteIfNoTotem(DrawContext context, Entity entity, CallbackInfo ci) {
		if (ConfigSystem.CONFIG.instance().enableMod && entity instanceof PlayerEntity player) {
			if (client.interactionManager == null) return;
			if (!client.interactionManager.getCurrentGameMode().isSurvivalLike()) return;
			boolean hasTotem = player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING || player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING;
			if (!hasTotem) {
				float f = ConfigSystem.CONFIG.instance().vignettePower;
				int color = ColorHelper.fromFloats(1.0F, 0.0F, f, f);

				context.drawTexture(RenderLayer::getVignette, VIGNETTE_TEXTURE, 0, 0, 0.0F, 0.0F, context.getScaledWindowWidth(), context.getScaledWindowHeight(), context.getScaledWindowWidth(), context.getScaledWindowHeight(), color);
				ci.cancel();
			}
		}
	}
}