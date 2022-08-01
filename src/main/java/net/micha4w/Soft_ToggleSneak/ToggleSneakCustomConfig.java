package net.micha4w.Soft_ToggleSneak;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ToggleSneakCustomConfig implements ToggleSneakConfig {

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
    public void onPress(MinecraftClient client) {
        ToggleSneakClient.config = ToggleSneakCustomConfig.loadOrCreate();
        ((ToggleSneakCustomConfig) ToggleSneakClient.config).saveConfig(client, !isActivated);
    }

    public void saveConfig(MinecraftClient client, boolean willBeActivated) {
        isActivated = willBeActivated;
        save();

        if ( isActivated ) {
            client.player.sendMessage(Text.translatable("text.soft_toggle_sneak.enable"), true);
        } else {
            ToggleSneakClient.isSneaking = false;
            client.player.sendMessage(Text.translatable("text.soft_toggle_sneak.disable"), true);
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
    public ConfigScreenFactory<?> getScreen() {
        return parent -> null;
    }
}
