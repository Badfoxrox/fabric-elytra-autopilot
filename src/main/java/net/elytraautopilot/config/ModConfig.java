package net.elytraautopilot.config;

import com.google.gson.GsonBuilder;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;

import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModConfig
{
    
    public static final ModConfig INSTANCE = new ModConfig();
    
    public static ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(new Identifier("fabric-elytra-autopilot", "elytraautopilot"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("fabric-elytra-autopilot.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();
    
    
    public static Screen createSettingsGui(Screen parent)
    {
        YetAnotherConfigLib.createBuilder()
                .title(Text.of("Used for narration. Could be used to render a title in the future."))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Elytra Autopilot"))
                        .tooltip(Text.of("Elytra autopilot config screen"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("In-Flight GUI"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Show Gui"))
                                        .binding(true, () -> showgui, value -> showgui = value )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("GUI Scale"))
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(0,300)
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
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Flight Profile"))
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Max Height when flying"))
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(0,500)
                                                .step(1)
                                                .formatValue(value -> Text.literal(value + " blocks")))
                                        .binding(360, () -> maxHeight, value -> maxHeight = value)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Min Height from ground"))
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(0,500)
                                                .step(1)
                                                .formatValue(value -> Text.literal(value + " blocks")))
                                        .binding(180, () -> maxHeight, value -> maxHeight = value)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Land automatically"))
                                        .controller(TickBoxControllerBuilder::create)
                                        .binding(true, () -> autoLanding, value -> autoLanding = value )
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
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Advanced"))
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Ticks before checking for ground"))
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 20))
                                        .binding(1, () -> groundCheckTicks, value -> groundCheckTicks = value)
                                        .build())
                                .option(Option.<Double>createBuilder()
                                        .name(Text.of("Going up angle"))
                                        .controller(DoubleFieldControllerBuilder::create)
                                        .binding(-46.633514, () -> pullUpAngle, value -> pullUpAngle = value)
                                        .build())
                                .option(Option.<Double>createBuilder()
                                        .name(Text.of("Going down angle"))
                                        .controller(DoubleFieldControllerBuilder::create)
                                        .binding(37.19872, () -> pullDownAngle, value -> pullUpAngle = value)
                                        .build())
                                .option(Option.<Double>createBuilder()
                                        .name(Text.of("Min velocity when going up"))
                                        .controller(DoubleFieldControllerBuilder::create)
                                        .binding(1.9102669, () -> pullUpMinVelocity, value -> pullUpMinVelocity = value)
                                        .build())
                                .option(Option.<Double>createBuilder()
                                        .name(Text.of("Max velocity when going down"))
                                        .controller(DoubleFieldControllerBuilder::create)
                                        .binding(2.3250866, () -> pullDownMaxVelocity, value -> pullDownMaxVelocity = value)
                                        .build())
                                .option(Option.<Double>createBuilder()
                                        .name(Text.of("Speed to pull up"))
                                        .controller(DoubleFieldControllerBuilder::create)
                                        .binding(2.1605124, () -> pullUpSpeed, value -> pullUpSpeed = value)
                                        .build())
                                .option(Option.<Double>createBuilder()
                                        .name(Text.of("Speed to pull down"))
                                        .controller(DoubleFieldControllerBuilder::create)
                                        .binding(0.20545267, () -> pullDownSpeed, value -> pullDownSpeed = value)
                                        .build())
                                .option(ListOption.<String>createBuilder()
                                        .name(Text.of("Fly Locations. {Name};{Xpos};{Ypos}"))
                                        .binding(new ArrayList<String>(), () -> flyLocations, value -> flyLocations = value)
                                        .controller(StringControllerBuilder::create)
                                        .initial("")
                                        .build())
                                .build())
                        .build())
                .build()
                .generateScreen(parent);
        
        return parent;
    }
    
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
}