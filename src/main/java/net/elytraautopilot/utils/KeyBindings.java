package net.elytraautopilot.utils;

import net.elytraautopilot.old.ElytraAutoPilot;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static KeyBinding configBinding;
    public static KeyBinding landBinding;
    public static KeyBinding takeoffBinding;
    
    public static void init() {
        String key;
        String modid = ElytraAutoPilot.getModId();
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            key = "key." + modid + ".toggle";
        } else {
            key = "key." + modid + ".toggle_no_cloth";
        }
        
        landBinding = new KeyBinding(
                "key." + modid + ".land",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                "config." + modid + ".title"
        );
        
        takeoffBinding = new KeyBinding(
                "key." + modid + ".takeoff",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "config." + modid + ".title"
        );
        
        configBinding = new KeyBinding(
                key,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "config." + modid + ".title"
        );
        
        KeyBindingHelper.registerKeyBinding(configBinding);
        KeyBindingHelper.registerKeyBinding(landBinding);
        KeyBindingHelper.registerKeyBinding(takeoffBinding);
    }
}
