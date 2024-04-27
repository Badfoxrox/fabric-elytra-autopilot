package net.elytraautopilot.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;

import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    
    
    public Screen createSettingsGui(Screen parent)
    {
        parent = YetAnotherConfigLib.createBuilder()
                .title(Text.of("Used for narration. Could be used to render a title in the future."))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Elytra Autopilot"))
                        .tooltip(Text.of("Elytra autopilot config screen"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("In-Flight GUI"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Show Gui"))
                                        .binding(true, () -> showgui, newVal -> showgui = newVal )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("GUI Scale"))
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(0,100)
                                                .step(1)
                                                .formatValue(value -> (Text.literal(value + " %"))))
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



/* @ConfigEntries(includeAll = true)
public class ModConfig extends Config {
    public ModConfig(String modId) {
        super(ConfigOptions.mod(modId));
    }

    @Transitive
    @ConfigEntries(includeAll = true)
    public static class gui implements ConfigGroup {
        public static boolean showgui = true;
        @ConfigEntry.Slider
        @ConfigEntry.BoundedInteger(min = 0, max = 300)
        public static int guiScale = 100;
        public static int guiX = 5;
        public static int guiY = 5;
    }

    @Transitive
    @ConfigEntries(includeAll = true)
    public static class flightprofile implements ConfigGroup {
        @ConfigEntry.Slider
        @ConfigEntry.BoundedInteger(min = 0, max = 500)
        public static int maxHeight = 360;
        @ConfigEntry.Slider
        @ConfigEntry.BoundedInteger(min = 0, max = 500)
        public static int minHeight = 180;
        public static boolean autoLanding = true;
        public static String playSoundOnLanding = "minecraft:block.note_block.pling";
        public static double autoLandSpeed = 3;
        public static double turningSpeed = 3;
        public static double takeOffPull = 10;
        public static boolean riskyLanding = false;
        public static boolean poweredFlight = false;
        public static boolean elytraHotswap = true;
        public static boolean fireworkHotswap = true;
        public static boolean emergencyLand = true;
    }

    @Transitive
    @ConfigEntries(includeAll = true)
    public static class advanced implements ConfigGroup {
        @ConfigEntry.Slider
        @ConfigEntry.BoundedInteger(min = 1, max = 20)
        public static int groundCheckTicks = 1;
        public static double pullUpAngle = -46.633514;
        public static double pullDownAngle = 37.19872;
        public static double pullUpMinVelocity = 1.9102669;
        public static double pullDownMaxVelocity = 2.3250866;
        public static double pullUpSpeed = 2.1605124;
        public static double pullDownSpeed = 0.20545267;
        public static List<String> flyLocations = new ArrayList<>();
    }
} */