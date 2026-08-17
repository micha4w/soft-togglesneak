package net.micha4w.Soft_ToggleSneak;


import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.micha4w.Soft_ToggleSneak.config.ToggleSneakClothConfig;
import net.micha4w.Soft_ToggleSneak.config.ToggleSneakCustomConfig;
import net.micha4w.Soft_ToggleSneak.iface.IKeybinding;
import net.micha4w.Soft_ToggleSneak.iface.IToggleSneakConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class ToggleSneakClient implements ClientModInitializer {

    private static KeyMapping toggleKeyBind;
    public static IToggleSneakConfig config;

    public static void innitConfig() {
        try {
            AutoConfig.register(ToggleSneakClothConfig.class, GsonConfigSerializer::new);
            config = AutoConfig.getConfigHolder(ToggleSneakClothConfig.class).getConfig();

        } catch (NoClassDefFoundError e) {
            config = ToggleSneakCustomConfig.loadOrCreate();
        }
    }

    @Override
    public void onInitializeClient() {
        if ( config == null ) {
            innitConfig();
        }

        toggleKeyBind = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.enable_toggle_sneak",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KeyMapping.Category.MOVEMENT
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

    private static void endTick (Minecraft client) {

        if ( toggleKeyBind.consumeClick() ) {
            config.onPress(client);
        }

        boolean isActivated = isActivated();

        if ( isActivated ) {

            if (client.player != null && client.level != null) {

                boolean isPressed = ((IKeybinding) client.options.keyShift).isPressed(true);

                if ( flyWhenClick && !client.player.getAbilities().flying ) flyWhenClick = false;
                if ( inLavaWhenClick && !client.player.isInLava() ) inLavaWhenClick = false;
                if ( inWaterWhenClick && !client.player.isInWater() ) inWaterWhenClick = false;


                if ( !isPressed && (
                    ( config.getUnsneakInWater() && client.player.isInWater() && !inWaterWhenClick ) ||
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
                        inWaterWhenClick = client.player.isInWater();
                        inLavaWhenClick = client.player.isInLava();

                        if (
                            ( !config.getSneakWhenInWater() && inWaterWhenClick ) ||
                            ( !config.getSneakWhenInLava() && inLavaWhenClick ) ||
                            ( !config.getSneakWhenGettingOfHorse() && client.player.isPassenger() ) ||
                            ( !config.getSneakWhenFlying() && flyWhenClick )
                        ) {
                            willUnsneak = true;
                        } else {
                            clickTick = client.level.getGameTime();
                        }
                    }
                } else if ( wasPressed && !isPressed ) {
                    if (willUnsneak) {
                        isSneaking = false;
                        willUnsneak = false;
                    } else {
                        long deltaClick = client.level.getGameTime() - clickTick;

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
