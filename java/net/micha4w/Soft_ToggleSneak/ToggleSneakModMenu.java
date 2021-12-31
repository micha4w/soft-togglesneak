package net.micha4w.Soft_ToggleSneak;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ToggleSneakModMenu implements ModMenuApi {

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if ( ToggleSneakClient.config == null )
            ToggleSneakClient.innitConfig();

        return ToggleSneakClient.config.getScreen();
    }
}
