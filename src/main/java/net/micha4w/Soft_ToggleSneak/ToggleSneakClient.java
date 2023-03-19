package net.micha4w.Soft_ToggleSneak;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.micha4w.Soft_ToggleSneak.config.ToggleSneakClothConfig;
import net.micha4w.Soft_ToggleSneak.config.ToggleSneakCustomConfig;
import net.micha4w.Soft_ToggleSneak.iface.IKeybinding;
import net.micha4w.Soft_ToggleSneak.iface.IToggleSneakConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class ToggleSneakClient implements ClientModInitializer {

    private static KeyBinding toggleKeyBind;
    public static IToggleSneakConfig config;

    private static KeyBinding noMoveKeyBind;
    private static boolean noMoving;
    private static Vec3d lastPosition;

    public static void innitConfig() {
        try {
            AutoConfig.register(ToggleSneakClothConfig.class, GsonConfigSerializer::new);
            config = AutoConfig.getConfigHolder(ToggleSneakClothConfig.class).getConfig();

        } catch (NoClassDefFoundError e) {
            config = ToggleSneakCustomConfig.loadOrCreate();
        }
    }

    public static boolean isNoMoveActivated() {
        return noMoving;
    }

    @Override
    public void onInitializeClient() {
        if ( config == null ) {
            innitConfig();
        }

        toggleKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.enable_toggle_sneak",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KeyBinding.MOVEMENT_CATEGORY
        ));

        noMoveKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.no_moving",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                KeyBinding.MOVEMENT_CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(ToggleSneakClient::endTick);
    }


    private static long clickTick = 0;
    private static boolean wasPressed = false;
    private static boolean willUnsneak = false;
    public static boolean isSneaking = false;

    private static boolean flyWhenClick = false;
    private static boolean inWaterWhenClick = false;
    private static boolean inLavaWhenClick = false;

    public static boolean isActivated() {
        return config.getActivated();
    }

    private static void endTick (MinecraftClient client) {

        if (noMoveKeyBind.wasPressed())
        {
            noMoving = !noMoving;
            if (noMoving) {
                lastPosition = client.player.getPos();
            } else {
                client.player.setPosition(lastPosition);
            }
            client.player.sendMessage(Text.of("NoMoving: " + noMoving));
        }

        if ( toggleKeyBind.wasPressed() ) {
            config.onPress(client);
        }

        boolean isActivated = isActivated();

        if ( isActivated ) {

            if (client.player != null && client.world != null) {

                boolean isPressed = ((IKeybinding) client.options.sneakKey).isPressed(true);

                if ( flyWhenClick && !client.player.getAbilities().flying ) flyWhenClick = false;
                if ( inLavaWhenClick && !client.player.isInLava() ) inLavaWhenClick = false;
                if ( inWaterWhenClick && !client.player.isInsideWaterOrBubbleColumn() ) inWaterWhenClick = false;


                if ( !isPressed && (
                    ( config.getUnsneakInWater() && client.player.isInsideWaterOrBubbleColumn() && !inWaterWhenClick ) ||
                    ( config.getUnsneakInLava() && client.player.isInLava() && !inLavaWhenClick) ||
                    ( config.getUnsneakWhenFlying() && client.player.getAbilities().flying && !flyWhenClick ) )
                ) {
                    isSneaking = false;
                }

                if ( isPressed && !wasPressed ) {
                    if ( isSneaking ) {
                        willUnsneak = true;
                    } else {
                        isSneaking = true;

                        flyWhenClick = client.player.getAbilities().flying;
                        inWaterWhenClick = client.player.isInsideWaterOrBubbleColumn();
                        inLavaWhenClick = client.player.isInLava();

                        if (
                            ( !config.getSneakWhenInWater() && inWaterWhenClick ) ||
                            ( !config.getSneakWhenInLava() && inLavaWhenClick ) ||
                            ( !config.getSneakWhenGettingOfHorse() && client.player.hasVehicle() ) ||
                            ( !config.getSneakWhenFlying() && flyWhenClick )
                        ) {
                            willUnsneak = true;
                        } else {
                            clickTick = client.world.getTime();
                        }
                    }
                } else if ( wasPressed && !isPressed ) {
                    if (willUnsneak) {
                        isSneaking = false;
                        willUnsneak = false;
                    } else {
                        long deltaClick = client.world.getTime() - clickTick;

                        if ( deltaClick < config.getMinTicks() || deltaClick > config.getMaxTicks()) {
                            isSneaking = false;
                        }
                    }
                }

                wasPressed = isPressed;
            }
        }
    }
}
