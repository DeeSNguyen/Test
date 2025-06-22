package com.example.TeakPlugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("ThreeTickTeak")
public interface ThreeTickTeakConfig extends Config {
    @ConfigItem(
            keyName = "enabled",
            name = "Enable plugin",
            description = "Enable three tick teak woodcutting"
    )
    default boolean enabled() {
        return false;
    }
}
