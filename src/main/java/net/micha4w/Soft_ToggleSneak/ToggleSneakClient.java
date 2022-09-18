package ch.micha4w.Soft_ToggleSneak;

import com.mojang.logging.LogUtils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.micha4w.Soft_ToggleSneak.config.ToggleSneakClothConfig;
import net.micha4w.Soft_ToggleSneak.config.ToggleSneakCustomConfig;
import net.micha4w.Soft_ToggleSneak.iface.IKeyMapping;
import net.micha4w.Soft_ToggleSneak.iface.IToggleSneakConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

@Mod(ToggleSneakClient.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ToggleSneakClient {
    public static final String MODID = "soft_togglesneak";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static KeyMapping sneakKeyMapping;
    private static final KeyMapping toggleKeyMapping = new KeyMapping("key.enable_toggle_sneak", GLFW.GLFW_KEY_RIGHT_SHIFT, "key.categories.movement");

    public static IToggleSneakConfig config;

    public static boolean isActivated() {
        return config.getActivated();
    }

    @SubscribeEvent
    public static void onClientInit(FMLClientSetupEvent event) {
        sneakKeyMapping = Minecraft.getInstance().options.keyShift;
        try {
            AutoConfig.register(ToggleSneakClothConfig.class, GsonConfigSerializer::new);
            config = AutoConfig.getConfigHolder(ToggleSneakClothConfig.class).getConfig();

            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(config::getScreen)
            );
        } catch (NoClassDefFoundError e) {
            config = ToggleSneakCustomConfig.loadOrCreate();
        }
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(toggleKeyMapping);
    }

    private static long clickTick = 0;
    private static boolean wasPressed = false;
    private static boolean willUnsneak = false;
    public static boolean isSneaking = false;

    private static boolean flyWhenClick = false;
    private static boolean inWaterWhenClick = false;
    private static boolean inLavaWhenClick = false;

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class EventHandler {

        @SubscribeEvent
        public static void onTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Minecraft client = Minecraft.getInstance();

                if (toggleKeyMapping.consumeClick()) {
                    config.onPress(client);
                }

                if (config.getActivated() && client.player != null && client.level != null) {

                    boolean isPressed = ((IKeyMapping) sneakKeyMapping).isDown(true);

                    if (flyWhenClick && !client.player.getAbilities().flying) flyWhenClick = false;
                    if (inLavaWhenClick && !client.player.isInLava()) inLavaWhenClick = false;
                    if (inWaterWhenClick && !client.player.isInWaterOrBubble()) inWaterWhenClick = false;


                    if (!isPressed && (
                            (config.getUnsneakInWater() && client.player.isInWaterOrBubble() && !inWaterWhenClick) ||
                                    (config.getUnsneakInLava() && client.player.isInLava() && !inLavaWhenClick) ||
                                    (config.getUnsneakWhenFlying() && client.player.getAbilities().flying && !flyWhenClick)
                    )) {
                        isSneaking = false;
                    }

                    if (isPressed && !wasPressed) {
                        if (isSneaking) {
                            willUnsneak = true;
                        } else {
                            isSneaking = true;

                            flyWhenClick = client.player.getAbilities().flying;
                            inLavaWhenClick = client.player.isInLava();
                            inWaterWhenClick = client.player.isInWaterOrBubble();

                            if (
                                    (!config.getSneakWhenInWater() && inWaterWhenClick) ||
                                            (!config.getSneakWhenInLava() && inLavaWhenClick) ||
                                            (!config.getSneakWhenGettingOfHorse() && client.player.isPassenger()) ||
                                            (!config.getSneakWhenFlying() && flyWhenClick)
                            ) {
                                willUnsneak = true;
                            } else {
                                clickTick = client.level.getGameTime();
                            }
                        }
                    } else if (wasPressed && !isPressed) {
                        if (willUnsneak) {
                            isSneaking = false;
                            willUnsneak = false;
                        } else {
                            long deltaClick = client.level.getGameTime() - clickTick;

                            if (deltaClick < config.getMinTicks() || deltaClick > config.getMaxTicks()) {
                                isSneaking = false;
                            }
                        }
                    }

                    wasPressed = isPressed;
                }
            }
        }
    }
}
