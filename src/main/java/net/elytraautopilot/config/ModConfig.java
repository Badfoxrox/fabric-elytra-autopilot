package net.elytraautopilot.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ListOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    public static ConfigClassHandler<ModConfig> GSON = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(new Identifier("fabric-elytra-autopilot", "elytraautopilot"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("fabric-elytra-autopilot.json5"))
                    .setJson5(true)
                    .build())
            .build();
    
    @SerialEntry
    public static Boolean showgui = true;
    
    @SerialEntry
    public static int guiScale = 100;
    
    @SerialEntry
    public static int guiX = 5;
    
    @SerialEntry
    public static int guiY = 5;
    
    @SerialEntry
    public static int maxHeight = 360;
    
    @SerialEntry
    public static int minHeight = 180;
    
    @SerialEntry
    public static boolean canAutoLand = true;
    
    @SerialEntry
    public static String playSoundOnLanding = "minecraft:block.note_block.pling";
    
    @SerialEntry
    public static double autoLandSpeed = 3;
    
    @SerialEntry
    public static double turningSpeed = 3;
    
    @SerialEntry
    public static double takeOffPull = 10;
    
    @SerialEntry
    public static boolean riskyLanding = false;
    
    @SerialEntry
    public static boolean poweredFlight = false;
    
    @SerialEntry
    public static boolean elytraHotswap = true;
    
    @SerialEntry
    public static boolean fireworkHotswap = true;
    
    @SerialEntry
    public static boolean emergencyLand = true;
    
    @SerialEntry
    public static int groundCheckTicks = 1;
    
    @SerialEntry
    public static double pullUpAngle = -46.633514;
    
    @SerialEntry
    public static double pullDownAngle = 37.19872;
    
    @SerialEntry
    public static double pullUpMinVelocity = 1.9102669;
    
    @SerialEntry
    public static double pullDownMaxVelocity = 2.3250866;
    
    @SerialEntry
    public static double pullUpSpeed = 2.1605124;
    
    @SerialEntry
    public static double pullDownSpeed = 0.20545267;
    
    @SerialEntry
    public static List<String> flyLocations = new ArrayList<>();
    
    
    public static Screen getConfigScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("config.elytraautopilot.title"))
                
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.elytraautopilot.gui"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.gui.showgui"))
                                .binding(true, () -> showgui, value -> showgui = value)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.gui.guiScale"))
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 500)
                                        .step(1)
                                        .formatValue(value -> Text.literal(value + " %")))
                                .binding(100, () -> guiScale, value -> guiScale = value)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.gui.guiX"))
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                                        .formatValue(value -> (Text.literal(value + " px"))))
                                .binding(5, () -> guiX, value -> guiX = value)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.gui.guiY"))
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                                        .formatValue(value -> Text.literal(value + " px")))
                                .binding(5, () -> guiY, value -> guiY = value)
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.elytraautopilot.flightprofile"))
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.flightprofile.maxHeight"))
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 1000)
                                        .step(1)
                                        .formatValue(value -> Text.literal(value + " blocks")))
                                .binding(360, () -> maxHeight, value -> maxHeight = value)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.flightprofile.minHeight"))
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 500)
                                        .step(1)
                                        .formatValue(value -> Text.literal(value + " blocks")))
                                .binding(180, () -> minHeight, value -> minHeight = value)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.flightprofile.autoLanding"))
                                .controller(TickBoxControllerBuilder::create)
                                .binding(true, () -> canAutoLand, value -> canAutoLand = value)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.flightprofile.playSoundOnLanding"))
                                .controller(StringControllerBuilder::create)
                                .binding("minecraft:block.note_block.pling", () -> playSoundOnLanding, value -> playSoundOnLanding = value)
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.flightprofile.autoLandSpeed"))
                                .controller(DoubleFieldControllerBuilder::create)
                                .binding(3d, () -> autoLandSpeed, value -> autoLandSpeed = value)
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.flightprofile.turningSpeed"))
                                .controller(DoubleFieldControllerBuilder::create)
                                .binding(3d, () -> autoLandSpeed, value -> autoLandSpeed = value)
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.flightprofile.takeOffPull"))
                                .controller(DoubleFieldControllerBuilder::create)
                                .binding(10d, () -> takeOffPull, value -> takeOffPull = value)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.flightprofile.riskyLanding"))
                                .controller(BooleanControllerBuilder::create)
                                .binding(false, () -> riskyLanding, value -> riskyLanding = value)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.flightprofile.poweredFlight"))
                                .controller(BooleanControllerBuilder::create)
                                .binding(false, () -> poweredFlight, value -> poweredFlight = value)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.flightprofile.elytraHotswap"))
                                .controller(BooleanControllerBuilder::create)
                                .binding(true, () -> elytraHotswap, value -> elytraHotswap = value)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.flightprofile.fireworkHotswap"))
                                .controller(BooleanControllerBuilder::create)
                                .binding(true, () -> fireworkHotswap, value -> fireworkHotswap = value)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.flightprofile.emergencyLand"))
                                .controller(BooleanControllerBuilder::create)
                                .binding(true, () -> emergencyLand, value -> emergencyLand = value)
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.elytraautopilot.advanced"))
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.advanced.groundCheckTicks"))
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(1, 20)
                                        .step(1))
                                .binding(1, () -> groundCheckTicks, value -> groundCheckTicks = value)
                                .build())
                        .group(ListOption.<String>createBuilder()
                                .name(Text.translatable("config.elytraautopilot.advanced.flyLocations"))
                                .binding(new ArrayList<>(), () -> flyLocations, value -> flyLocations = value)
                                .controller(StringControllerBuilder::create)
                                .initial("")
                                .build())
                        .build())
                .save(() -> GSON.save())
                .build().generateScreen(parent);
    }
}