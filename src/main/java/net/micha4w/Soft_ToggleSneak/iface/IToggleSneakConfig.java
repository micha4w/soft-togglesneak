package net.micha4w.Soft_ToggleSneak.iface;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.minecraft.client.MinecraftClient;

public interface IToggleSneakConfig {

    void onPress(MinecraftClient client);

    boolean getActivated();
    long getMinTicks();
    long getMaxTicks();

    boolean getUnsneakInLava();
    boolean getUnsneakInWater();
    boolean getUnsneakWhenFlying();

    boolean getSneakWhenInLava();
    boolean getSneakWhenInWater();
    boolean getSneakWhenFlying();
    boolean getSneakWhenGettingOfHorse();

    ConfigScreenFactory<?> getScreen();
}
