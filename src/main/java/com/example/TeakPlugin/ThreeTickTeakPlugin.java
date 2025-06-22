package com.example.TeakPlugin;

import com.example.EthanApiPlugin.Collections.Inventory;
import com.example.EthanApiPlugin.Collections.TileObjects;
import com.example.InteractionApi.TileObjectInteraction;
import com.example.Packets.WidgetPackets;
import com.google.inject.Inject;
import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.Optional;

@PluginDescriptor(
        name = "Three Tick Teak",
        description = "Automates 3t teak cutting using guam tar and pestle and mortar",
        enabledByDefault = false,
        tags = {"woodcutting"}
)
public class ThreeTickTeakPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private ThreeTickTeakConfig config;

    /**
     * Keeps track of the current action in the three-tick cycle.
     * 0 = prepare guam tar
     * 1 = chop teak
     * 2 = idle/wait
     */
    private int tickCycle = 0;

    @Provides
    public ThreeTickTeakConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(ThreeTickTeakConfig.class);
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        if (!config.enabled()) {
            return;
        }

        switch (tickCycle) {
            case 0:
                makeTar();
                break;
            case 1:
                chopTree();
                break;
            default:
                // wait one tick to maintain 3t rhythm
                break;
        }

        tickCycle = (tickCycle + 1) % 3;
    }

    private void chopTree() {
        clientThread.invokeLater(() -> {
            Optional<TileObject> teak = TileObjects.search().nameContains("Teak").nearestToPlayer();
            teak.ifPresent(t -> TileObjectInteraction.interact(t, "Chop down"));
        });
    }

    private void makeTar() {
        clientThread.invokeLater(() -> {
            Optional<Widget> guam = Inventory.search().withName("Guam leaf").first();
            Optional<Widget> tar = Inventory.search().withName("Swamp tar").first();
            if (guam.isPresent() && tar.isPresent() && Inventory.search().withName("Pestle and mortar").first().isPresent()) {
                WidgetPackets.queueWidgetOnWidget(guam.get(), tar.get());
            }
        });
    }
}
