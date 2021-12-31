package net.micha4w.Soft_ToggleSneak;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.minecraft.client.MinecraftClient;

public interface ToggleSneakConfig {

    void onPress(MinecraftClient client);

    boolean getActivated();
    long getMinTicks();
    long getMaxTicks();

    boolean getUnseakInLava();
    boolean getUnseakInWater();
    boolean getUnseakWhenFlying();

    boolean getSneakWhenInLava();
    boolean getSneakWhenInWater();
    boolean getSneakWhenFlying();
    boolean getSneakWhenGettingOfHorse();

    ConfigScreenFactory<?> getScreen();
}
