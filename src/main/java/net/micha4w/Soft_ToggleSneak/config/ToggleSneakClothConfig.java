package net.micha4w.Soft_ToggleSneak.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.micha4w.Soft_ToggleSneak.iface.IToggleSneakConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

@Config(name = "soft_toggle_sneak")
public class ToggleSneakClothConfig implements ConfigData, IToggleSneakConfig {
    public boolean isActivated = true;
    long minTicks = 0;
    long maxTicks = 5;

    @ConfigEntry.Gui.CollapsibleObject
    UnsneakBehaviour unsneak_behaviour = new UnsneakBehaviour();
    static class UnsneakBehaviour {
        boolean unsneakInLava = false;
        boolean unsneakInWater = true;
        boolean unsneakWhenFlying = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    ToggleSneakBehaviour togglesneak_behaviour = new ToggleSneakBehaviour();
    static class ToggleSneakBehaviour {
        boolean sneakWhenInLava = true;
        boolean sneakWhenInWater = false;
        boolean sneakWhenFlying = false;
        boolean sneakWhenGettingOfHorse = false;
    }

    @Override
    public void onPress(Minecraft client) {
        client.setScreen(getScreen(client, client.screen));
    }


    @Override
    public boolean getActivated() {
        return isActivated;
    }

    @Override
    public long getMinTicks() {
        return minTicks;
    }

    @Override
    public long getMaxTicks() {
        return maxTicks;
    }

    @Override
    public boolean getUnsneakInLava() {
        return unsneak_behaviour.unsneakInLava;
    }

    @Override
    public boolean getUnsneakInWater() {
        return unsneak_behaviour.unsneakInWater;
    }

    @Override
    public boolean getUnsneakWhenFlying() {
        return unsneak_behaviour.unsneakWhenFlying;
    }

    @Override
    public boolean getSneakWhenInLava() {
        return togglesneak_behaviour.sneakWhenInLava;
    }

    @Override
    public boolean getSneakWhenInWater() {
        return togglesneak_behaviour.sneakWhenInWater;
    }

    @Override
    public boolean getSneakWhenFlying() {
        return togglesneak_behaviour.sneakWhenFlying;
    }

    @Override
    public boolean getSneakWhenGettingOfHorse() {
        return togglesneak_behaviour.sneakWhenGettingOfHorse;
    }

    @Override
    public Screen getScreen(Minecraft client, Screen parent) {
        return AutoConfig.getConfigScreen(ToggleSneakClothConfig.class, parent).get();
    }
}
