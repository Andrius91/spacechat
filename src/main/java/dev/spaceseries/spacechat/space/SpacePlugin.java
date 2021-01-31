package dev.spaceseries.spacechat.space;

import dev.spaceseries.api.abstraction.plugin.BukkitPlugin;
import dev.spaceseries.api.abstraction.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class SpacePlugin {

    /**
     * SpaceAPI plugin
     */
    private Plugin plugin;

    /**
     * Construct space plugin
     *
     * @param plugin plugin
     */
    public SpacePlugin(JavaPlugin plugin) {
        this.plugin = new BukkitPlugin(plugin);
    }

    /**
     * Returns spaceAPI plugin
     *
     * @return plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }
}
