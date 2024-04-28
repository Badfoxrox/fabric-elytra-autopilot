package net.elytraautopilot.config;

import dev.isxander.yacl3.api.*;
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

public class ModConfig
{
    public static ConfigClassHandler<ModConfig> GSON = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(new Identifier("fabric-elytra-autopilot", "elytraautopilot"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("fabric-elytra-autopilot.json"))
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
    public static boolean autoLanding = true;
    
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
    
    
    public static Screen getConfigScreen(Screen parent)
    {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("Elytra AutoPilot Config Screen"))
                
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("In-Flight GUI"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Show Gui"))
                                .binding(true, () -> showgui, value -> showgui = value)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.of("GUI Scale"))
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 300)
                                        .step(1)
                                        .formatValue(value -> Text.literal(value + " %")))
                                .binding(100, () -> guiScale, value -> guiScale = value)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.of("GUI X Offset"))
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                                        .formatValue(value -> (Text.literal(value + " px"))))
                                .binding(5, () -> guiX, value -> guiX = value)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.of("GUI Y Offset"))
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                                        .formatValue(value -> Text.literal(value + " px")))
                                .binding(5, () -> guiY, value -> guiY = value)
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Flight Profile"))
                        .option(Option.<Integer>createBuilder()
                                .name(Text.of("Max Height when flying"))
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 500)
                                        .step(1)
                                        .formatValue(value -> Text.literal(value + " blocks")))
                                .binding(360, () -> maxHeight, value -> maxHeight = value)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.of("Min Height from ground"))
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 500)
                                        .step(1)
                                        .formatValue(value -> Text.literal(value + " blocks")))
                                .binding(180, () -> minHeight, value -> minHeight = value)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Land automatically"))
                                .controller(TickBoxControllerBuilder::create)
                                .binding(true, () -> autoLanding, value -> autoLanding = value)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.of("Play sound on landing"))
                                .controller(StringControllerBuilder::create)
                                .binding("minecraft:block.note_block.pling", () -> playSoundOnLanding, value -> playSoundOnLanding = value)
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Text.of("Landing turning speed"))
                                .controller(DoubleFieldControllerBuilder::create)
                                .binding(3d, () -> autoLandSpeed, value -> autoLandSpeed = value)
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Text.of("Mid-flight turning speed"))
                                .controller(DoubleFieldControllerBuilder::create)
                                .binding(3d, () -> autoLandSpeed, value -> autoLandSpeed = value)
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Text.of("Take-off looking up speed"))
                                .controller(DoubleFieldControllerBuilder::create)
                                .binding(10d, () -> takeOffPull, value -> takeOffPull = value)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Risky Landing. Take a nosedive!"))
                                .controller(BooleanControllerBuilder::create)
                                .binding(false, () -> riskyLanding, value -> riskyLanding = value)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Powered flight. Use fireworks while flying"))
                                .controller(BooleanControllerBuilder::create)
                                .binding(false, () -> poweredFlight, value -> poweredFlight = value)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Automatically swap elytra when low on durability"))
                                .controller(BooleanControllerBuilder::create)
                                .binding(true, () -> elytraHotswap, value -> elytraHotswap = value)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Refills fireworks automatically"))
                                .controller(BooleanControllerBuilder::create)
                                .binding(true, () -> fireworkHotswap, value -> fireworkHotswap = value)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Emergency Landing"))
                                .controller(BooleanControllerBuilder::create)
                                .binding(true, () -> emergencyLand, value -> emergencyLand = value)
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Advanced"))
                        .option(Option.<Integer>createBuilder()
                                .name(Text.of("Ticks before checking for ground"))
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(1, 20)
                                        .step(1))
                                .binding(1, () -> groundCheckTicks, value -> groundCheckTicks = value)
                                .build())
                        .group(ListOption.<String>createBuilder()
                                .name(Text.of("Fly Locations {Name};{X};{Z}"))
                                .binding(new ArrayList<>(), () -> flyLocations, value -> flyLocations = value)
                                .controller(StringControllerBuilder::create)
                                .initial("")
                                .build())
                        .build())
                .save(() -> GSON.save())
            .build().generateScreen(parent);
    }
}