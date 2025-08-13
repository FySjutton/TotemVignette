package avox.totemvignette;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigSystem {
    public static final ConfigClassHandler<ConfigSystem> CONFIG = ConfigClassHandler.createBuilder(ConfigSystem.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("totemvignette.json"))
                    .build())
            .build();

    @SerialEntry public boolean enableMod = true;
    @SerialEntry public float vignettePower = 0.8f;

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.translatable("totemvignette.category"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("totemvignette.config.category.general"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("totemvignette.config.option.enable_mod"))
                                .description(OptionDescription.of(Text.translatable("totemvignette.config.option.enable_mod.desc")))
                                .binding(true, () -> config.enableMod, newVal -> config.enableMod = newVal)
                                .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                .build())
                        .option(Option.<Float>createBuilder()
                                .name(Text.translatable("totemvignette.config.option.vignette_power"))
                                .description(OptionDescription.of(Text.translatable("totemvignette.config.option.vignette_power.desc")))
                                .binding(0.8f, () -> config.vignettePower, newVal -> config.vignettePower = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.1f, 1f)
                                        .step(0.1f)
                                        .formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
                                .build())
                .build())
        )).generateScreen(parent);
    }
}