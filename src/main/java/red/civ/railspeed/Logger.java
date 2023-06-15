package red.civ.railspeed;

import org.bukkit.Bukkit;

public class Logger {
    static private final String pluginName = "[RailSpeed]";
    static public void Info(String message){
        Bukkit.getLogger().info(pluginName + " " + message);
    }

    static public void Warn(String message){
        Bukkit.getLogger().warning(pluginName + " " + message);

    }

    static public void Error(String message){
        Bukkit.getLogger().severe(pluginName + " " + message);
    }
}