package net.micha4w.Soft_ToggleSneak.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.micha4w.Soft_ToggleSneak.ToggleSneakClient;

public class ToggleSneakModMenu implements ModMenuApi {

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if ( ToggleSneakClient.config == null )
            ToggleSneakClient.innitConfig();

        return ToggleSneakClient.config.getScreen();
    }
}
