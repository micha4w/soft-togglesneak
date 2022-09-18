package net.micha4w.Soft_ToggleSneak.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.micha4w.Soft_ToggleSneak.iface.IToggleSneakConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ToggleSneakCustomConfig implements IToggleSneakConfig {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String path = "config/soft_toggle_sneak.json";

    public boolean isActivated = true;
    long minTicks = 0;
    long maxTicks = 5;

    UnsneakBehaviour unsneak_behaviour = new UnsneakBehaviour();

    static class UnsneakBehaviour {
        boolean unsneakInLava = false;
        boolean unsneakInWater = true;
        boolean unsneakWhenFlying = true;
    }

    ToggleSneakBehaviour togglesneak_behaviour = new ToggleSneakBehaviour();
    static class ToggleSneakBehaviour {
        boolean sneakWhenInLava = true;
        boolean sneakWhenInWater = false;
        boolean sneakWhenFlying = false;
        boolean sneakWhenGettingOfHorse = false;
    }

    public void save () {
        try {
            FileWriter writer = new FileWriter(path);

            gson.toJson(this, writer);
            writer.close();

        } catch ( IOException e ) { e.printStackTrace(); }
    }

    public static ToggleSneakCustomConfig loadOrCreate () {

        ToggleSneakCustomConfig config;

        try {
            FileReader reader = new FileReader(path);

            config = gson.fromJson(reader, ToggleSneakCustomConfig.class);
        } catch (FileNotFoundException e) {
            config = new ToggleSneakCustomConfig();
            config.save();
        }

        return config;
    }


    @Override
    public void onPress(Minecraft client) {
        ch.micha4w.Soft_ToggleSneak.ToggleSneakClient.config = ToggleSneakCustomConfig.loadOrCreate();
        ((ToggleSneakCustomConfig) ch.micha4w.Soft_ToggleSneak.ToggleSneakClient.config).saveConfig(client, !isActivated);
    }

    public void saveConfig(Minecraft client, boolean willBeActivated) {
        isActivated = willBeActivated;
        save();

        if ( client.player != null ) {
            if ( isActivated ) {
                client.player.displayClientMessage(Component.translatable("text.soft_toggle_sneak.enable"), true);
            } else {
                ch.micha4w.Soft_ToggleSneak.ToggleSneakClient.isSneaking = false;
                client.player.displayClientMessage(Component.translatable("text.soft_toggle_sneak.disable"), true);
            }
        }
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
        return null;
    }
}
