package avox.totemvignette;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class TotemVignette implements ModInitializer {
	public static final KeyBinding toggleMod = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			Text.translatable("totemvignette.toggle_mod").getString(),
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_V,
			KeyBinding.Category.create(Identifier.of(Text.translatable("totemvignette.category").getString()))
	));

	@Override
	public void onInitialize() {
        ConfigSystem.CONFIG.load();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (toggleMod.wasPressed()) {
				boolean oldValue = ConfigSystem.CONFIG.instance().enableMod;
				ConfigSystem.CONFIG.instance().enableMod = !oldValue;
				ConfigSystem.CONFIG.save();
				client.getToastManager().add(
						new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION,
								Text.translatable("totemvignette.toggled"),
								Text.translatable("totemvignette." + (!oldValue ? "enabled" : "disabled"))
						)
				);
			}
		});
	}
}