package com.coruptical.projectcivilisation.utils;

import com.coruptical.projectcivilisation.commands.ReloadConfigCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Utils {
    public static void msg(Player p, String msg){
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void registerAll(JavaPlugin plugin){
        // Register listeners

        // Register commands
        plugin.getCommand("projectcivilisation_reload").setExecutor( new ReloadConfigCommand() );
    }
}
