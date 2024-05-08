package net.elytraautopilot;

import net.elytraautopilot.commands.ClientCommands;
import net.elytraautopilot.config.ModConfig;
import net.elytraautopilot.utils.Hud;
import net.elytraautopilot.utils.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElytraAutoPilot implements ClientModInitializer {
    // Unchanging things
    public static final String MODID = "elytraautopilot";
    public static final Logger LOGGER = LoggerFactory.getLogger("ElytraAutoPilot");
    public static MinecraftClient minecraftClient;
    private static final ModConfig config = new ModConfig();
    
    // Changing things
    private STATE currentState = STATE.IDLE;
    public static int destXpos;
    public static int destZpos;
    public static double distance = 0d;
    public static double pitchMod = 1f;
    public static Vec3d previousPosition;
    public static double currentVelocity;
    public static double currentVelocityHorizontal;
    public static boolean calculateHud;
    
    public static boolean isDescending;
    public static boolean pullUp;
    public static boolean pullDown;
    
    
    public static ModConfig getConfig() {
        return config;
    }
    
    public static String getModId() {
        return MODID;
    }
    
    @Override
    public void onInitializeClient() {
        // Get current instance
        minecraftClient = MinecraftClient.getInstance();
        
        // Register Keybinds
        KeyBindings.init();
        
        // Hud rendering once every tick
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> ElytraAutoPilot.this.onScreenTick());
        
        // 20 times per second; flying processing before any screen tick
        ClientTickEvents.END_CLIENT_TICK.register(e -> this.onClientTick());
        
        // load saved config
        ModConfig.GSON.load();
        
        ClientCommands.register(minecraftClient);
    }
    
    // Idle: doing nothing, allow full player control
    // Takeoff: Taking off, look up, and use rockets
    // Gliding: player control over yaw
    // Flying to: directed flying
    // Smooth Landing:
    // Risky Landing
    private enum STATE {
        IDLE, TAKEOFF, GLIDING, FLYING_TO, SMOOTH_LANDING, RISKY_LANDING
    }
    
    private void onScreenTick()
    {
        // completely stops the flying logic when paused
        if (minecraftClient.isPaused())
        {
            currentState = STATE.IDLE;
            return;
        }
        
        PlayerEntity player = minecraftClient.player;
        if (player == null) return;
        
        
        double fpsSpeedAdapt = 60 / (20 / minecraftClient.getLastFrameDuration());
        
        
        switch (currentState) {
            case IDLE:
                break;
            case TAKEOFF: {
                // Elytra not equipped
                if (player.getInventory().armor.get(2).getItem() != Items.ELYTRA) {
                    player.sendMessage(Text.translatable("text." + MODID + ".takeoffFail.noElytraEquipped").formatted(Formatting.RED), true);
                    return;
                }
                // Elytra too broken
                if (player.getInventory().armor.get(2).getMaxDamage() - player.getInventory().armor.get(2).getDamage() == 1) {
                    player.sendMessage(Text.translatable("text." + MODID + ".takeoffFail.elytraBroken").formatted(Formatting.RED), true);
                    return;
                }
                // No rockets being held
                if (player.getMainHandStack().getItem() != Items.FIREWORK_ROCKET && player.getOffHandStack().getItem() != Items.FIREWORK_ROCKET) {
                    player.sendMessage(Text.translatable("text." + MODID + ".takeoffFail.fireworkRequired").formatted(Formatting.RED), true);
                    return;
                }
                
                World world = player.getWorld();;
                int worldHeight = world.getTopY();
                int n = 2; // checks every block starting 2 above the player
                double playerHeight = player.getY();
                for (double i = playerHeight; i < worldHeight; i++) {
                    BlockPos blockPos = BlockPos.ofFloored(player.getX(), player.getY() + n, player.getZ());
                    if (!world.getBlockState(blockPos).isAir()) {
                        player.sendMessage(Text.translatable("text." + MODID + ".takeoffFail.clearSkyNeeded").formatted(Formatting.RED), true);
                        return;
                    }
                    n++;
                }
                takeoffCooldown = 5;
                minecraftClient.options.jumpKey.setPressed(true);
            }
                
                
                
                
                PlayerEntity player = minecraftClient.player;
                if (!onTakeoff) {
                    if (player != null) {
                        Item itemMain = player.getMainHandStack().getItem();
                        Item itemOff = player.getOffHandStack().getItem();
                        Item itemChest = player.getInventory().armor.get(2).getItem();
                        int elytraDamage = player.getInventory().armor.get(2).getMaxDamage() - player.getInventory().armor.get(2).getDamage();
                        if (itemChest != Items.ELYTRA) {
                            player.sendMessage(Text.translatable("text." + MODID + ".takeoffFail.noElytraEquipped").formatted(Formatting.RED), true);
                            return;
                        }
                        if (elytraDamage == 1) {
                            player.sendMessage(Text.translatable("text." + MODID + ".takeoffFail.elytraBroken").formatted(Formatting.RED), true);
                            return;
                        }
                        if (itemMain != Items.FIREWORK_ROCKET && itemOff != Items.FIREWORK_ROCKET) {
                            player.sendMessage(Text.translatable("text." + MODID + ".takeoffFail.fireworkRequired").formatted(Formatting.RED), true);
                            return;
                        }
                        
                        World world = player.getWorld();
                        Vec3d clientPos = player.getPos();
                        int l = world.getTopY();
                        int n = 2;
                        double c = clientPos.getY();
                        for (double i = c; i < l; i++) {
                            BlockPos blockPos = BlockPos.ofFloored(clientPos.getX(), clientPos.getY() + n, clientPos.getZ());
                            if (!world.getBlockState(blockPos).isAir()) {
                                player.sendMessage(Text.translatable("text." + MODID + ".takeoffFail.clearSkyNeeded").formatted(Formatting.RED), true);
                                return;
                            }
                            n++;
                        }
                        takeoffCooldown = TAKEOFF_COOLDOWN_TICKS;
                        minecraftClient.options.jumpKey.setPressed(true);
                    }
                    return;
                }
                if (player != null) {
                    if (groundheight > ModConfig.minHeight) {
                        onTakeoff = false;
                        minecraftClient.options.useKey.setPressed(false);
                        minecraftClient.options.jumpKey.setPressed(false);
                        autoFlight = true;
                        pitchMod = 3f;
                        if (isChained) {
                            isflytoActive = true;
                            isChained = false;
                            minecraftClient.inGameHud.getChatHud().addMessage(Text.translatable("text." + MODID + ".flyto", argXpos, argZpos).formatted(Formatting.GREEN));
                        }
                        return;
                    }
                    if (!player.isFallFlying())
                        minecraftClient.options.jumpKey.setPressed(!minecraftClient.options.jumpKey.isPressed());
                    Item itemMain = player.getMainHandStack().getItem();
                    Item itemOff = player.getOffHandStack().getItem();
                    boolean hasFirework = (itemMain == Items.FIREWORK_ROCKET || itemOff == Items.FIREWORK_ROCKET);
                    if (!hasFirework) {
                        if (!tryRestockFirework(player)) {
                            minecraftClient.options.useKey.setPressed(false);
                            minecraftClient.options.jumpKey.setPressed(false);
                            onTakeoff = false;
                            player.sendMessage(Text.translatable("text." + MODID + ".takeoffAbort.noFirework").formatted(Formatting.RED), true);
                            shouldRecenter = true;
                        }
                    } else minecraftClient.options.useKey.setPressed(player.getVelocity().getY() < 0.75f && player.getPitch() == -90f);
                }
                break;
            case FLYING_TO:
                Vec3d playerPosition = player.getPos();
                double distanceDiffX = (double) destXpos - playerPosition.x;
                double distanceDiffZ = (double) destZpos - playerPosition.z;
                float targetYaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(distanceDiffZ, distanceDiffX) * 57.2957763671875D) - 90.0F);
                float yaw = MathHelper.wrapDegrees(player.getYaw());
                if (Math.abs(yaw - targetYaw) < ModConfig.turningSpeed * 2 * fpsSpeedAdapt) player.setYaw(targetYaw);
                else {
                    if (yaw < targetYaw) player.setYaw((float) (yaw + ModConfig.turningSpeed * fpsSpeedAdapt));
                    if (yaw > targetYaw) player.setYaw((float) (yaw - ModConfig.turningSpeed * fpsSpeedAdapt));
                }
                distance = Math.sqrt(distanceDiffX * distanceDiffX + distanceDiffZ * distanceDiffZ);
                if (distance < 20) {
                    if (!ModConfig.canAutoLand)
                    {
                        player.sendMessage(Text.of("Arrived at Destination"), true);
                        SoundEvent soundEvent = SoundEvent.of(new Identifier(ModConfig.playSoundOnLanding));
                        player.playSound(soundEvent, 1.3f, 1f);
                        currentState = STATE.IDLE;
                        break;
                    }
                    player.sendMessage(Text.translatable("text.elytraautopilot.landing").formatted(Formatting.BLUE), true);
                    SoundEvent soundEvent = SoundEvent.of(new Identifier(ModConfig.playSoundOnLanding));
                    player.playSound(soundEvent, 1.3f, 1f);
                    if(ModConfig.riskyLanding && ModConfig.canAutoLand)
                    {
                        currentState = STATE.RISKY_LANDING;
                    } else if (ModConfig.canAutoLand){
                        currentState = STATE.SMOOTH_LANDING;
                    }
                    break;
                }
            case GLIDING:
                // Flight pitch behavior
                float pitch = player.getPitch();
                if (pullUp) {
                    player.setPitch((float) (pitch - ModConfig.pullUpSpeed * fpsSpeedAdapt));
                    pitch = player.getPitch();
                    if (pitch <= ModConfig.pullUpAngle) {
                        player.setPitch((float) ModConfig.pullUpAngle);
                    }
                    // Powered flight behavior
                    minecraftClient.options.useKey.setPressed(ModConfig.poweredFlight && currentVelocity < 1.25f);
                }
                if (pullDown) {
                    player.setPitch((float) (pitch + ModConfig.pullDownSpeed * pitchMod * fpsSpeedAdapt));
                    pitch = player.getPitch();
                    if (pitch >= ModConfig.pullDownAngle) {
                        player.setPitch((float) ModConfig.pullDownAngle);
                    }
                    // Powered flight behavior
                    minecraftClient.options.useKey.setPressed(ModConfig.poweredFlight && currentVelocity < 1.25f);
                }
                break;
            case SMOOTH_LANDING:
                break;
            case RISKY_LANDING:
                break;
        }
    }
    
    private void onClientTick()
    {
        if (!(minecraftClient.isPaused() && minecraftClient.isInSingleplayer())) Hud.tick();
        
        if (ClientCommands.bufferSave) {
            ModConfig.GSON.save();
            ClientCommands.bufferSave = false;
        }
        
        PlayerEntity player = minecraftClient.player;
        
        if(player == null){
            currentState = STATE.IDLE;
            return;
        }
        
        if(player.isFallFlying())
        {
            calculateHud = true;
        } else {
            calculateHud = false;
        }
        
        if(currentState.equals(STATE.FLYING_TO) || currentState.equals(STATE.GLIDING))
        {
            if (!tryRestockElytra(player) && ModConfig.emergencyLand) {
                player.sendMessage(Text.of("Elytra durability low, landing now!"), true);
                if(ModConfig.riskyLanding)
                {
                    currentState = STATE.RISKY_LANDING;
                } else {
                    currentState = STATE.SMOOTH_LANDING;
                }
                return;
            }
            
            if (player.isTouchingWater() || player.isInLava())
            {
                currentState = STATE.IDLE;
                return;
            }
            float velHigh = 0f;
            float velLow = 0f;
            
            double altitude = player.getY();
            if (isDescending) {
                pullUp = false;
                pullDown = true;
                if (altitude > ModConfig.maxHeight) { //TODO fix inconsistent height behavior
                    velHigh = 0.3f;
                } else if (altitude > ModConfig.maxHeight - 10) {
                    velLow = 0.28475f;
                }
                if (currentVelocity >= ModConfig.pullDownMaxVelocity + Math.max(velHigh, velLow)) {
                    isDescending = false;
                    pullDown = false;
                    pullUp = true;
                    pitchMod = 1f;
                }
            } else {
                pullUp = true;
                pullDown = false;
                if (currentVelocity <= ModConfig.pullUpMinVelocity || altitude > ModConfig.maxHeight - 10) {
                    isDescending = true;
                    pullDown = true;
                    pullUp = false;
                }
            }
        }
    }
    
    private static boolean tryRestockElytra(PlayerEntity player) {
        int elytraDurability = player.getInventory().armor.get(2).getMaxDamage() - player.getInventory().armor.get(2).getDamage();
        if (ModConfig.elytraHotswap) {
            if (elytraDurability <= 5) { // Leave some leeway, so we don't stop flying
                // Optimization: find the first elytra with sufficient durability
                ItemStack newElytra = null;
                int minDurability = 10;
                for (ItemStack itemStack : player.getInventory().main) {
                    if (itemStack.getItem() == Items.ELYTRA) {
                        int itemDurability = itemStack.getMaxDamage() - itemStack.getDamage();
                        if (itemDurability >= minDurability) {
                            newElytra = itemStack;
                            break;
                        }
                    }
                }
                if (newElytra != null) {
                    int chestSlot = 6;
                    assert minecraftClient.interactionManager != null;
                    minecraftClient.interactionManager.clickSlot(
                            player.playerScreenHandler.syncId,
                            chestSlot,
                            player.getInventory().main.indexOf(newElytra),
                            SlotActionType.SWAP,
                            player
                    );
                    player.playSound((SoundEvent) SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA, 1.0F, 1.0F);
                    player.sendMessage(Text.translatable("text." + MODID + ".swappedElytra").formatted(Formatting.GREEN), true);
                } else {
                    return false;
                }
            }
        } else return elytraDurability > 30;
        return true;
    }
    
    private static boolean tryRestockFirework(PlayerEntity player) {
        if (ModConfig.fireworkHotswap) {
            ItemStack newFirework = null;
            
            for (ItemStack itemStack : player.getInventory().main) {
                if (itemStack.getItem() == Items.FIREWORK_ROCKET) {
                    newFirework = itemStack;
                    break;
                }
            }
            if (newFirework != null) {
                int handSlot;
                if (player.getOffHandStack().isEmpty()) {
                    handSlot = 45; //Offhand slot refill
                } else {
                    handSlot = 36 + player.getInventory().selectedSlot; //Mainhand slot refill
                }
                
                assert minecraftClient.interactionManager != null;
                minecraftClient.interactionManager.clickSlot(
                        player.playerScreenHandler.syncId,
                        handSlot,
                        player.getInventory().main.indexOf(newFirework),
                        SlotActionType.SWAP,
                        player
                );
                return true;
            }
        }
        return false;
    }
    
    private void computeVelocity() {
        Vec3d newPosition;
        PlayerEntity player = minecraftClient.player;
        if (player != null && !(minecraftClient.isPaused() && minecraftClient.isInSingleplayer())) {
            newPosition = player.getPos();
            if (previousPosition == null)
                previousPosition = newPosition;
            
            Vec3d difference = new Vec3d(newPosition.x - previousPosition.x, newPosition.y - previousPosition.y, newPosition.z - previousPosition.z);
            Vec3d difference_horizontal = new Vec3d(newPosition.x - previousPosition.x, 0, newPosition.z - previousPosition.z);
            previousPosition = newPosition;
            
            currentVelocity = difference.length();
            currentVelocityHorizontal = difference_horizontal.length();
        }
    }
}
