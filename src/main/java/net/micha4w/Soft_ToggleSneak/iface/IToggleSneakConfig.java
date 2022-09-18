package net.micha4w.Soft_ToggleSneak.iface;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public interface IToggleSneakConfig {

    void onPress(Minecraft client);

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

    Screen getScreen(Minecraft client, Screen parent);
}
