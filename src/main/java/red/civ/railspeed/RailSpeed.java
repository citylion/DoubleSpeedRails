package red.civ.railspeed;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RailSpeed extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(new RailListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
