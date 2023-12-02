package com.coruptical.projectcivilisation.utils;

import com.coruptical.projectcivilisation.commands.AdminFactionCommand;
import com.coruptical.projectcivilisation.commands.ReloadConfigCommand;
import com.coruptical.projectcivilisation.commands.UserFactionsCommand;
import com.coruptical.projectcivilisation.event.ChunkEnterListener;
import com.coruptical.projectcivilisation.models.Faction;
import com.coruptical.projectcivilisation.models.FastChunk;
import com.coruptical.projectcivilisation.models.StoredChunk;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Utils {
    public static void msg(Player p, String msg){
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void registerAll(JavaPlugin plugin){
        // Register listeners
        plugin.getServer().getPluginManager().registerEvents(new ChunkEnterListener(), plugin);

        // Register commands
        plugin.getCommand("projectcivilisation_reload").setExecutor( new ReloadConfigCommand() );

        AdminFactionCommand adminFactionCommand = new AdminFactionCommand();
        plugin.getCommand("civfactionsadmin").setExecutor(adminFactionCommand);
        plugin.getCommand("civfactionsadmin").setTabCompleter(adminFactionCommand);

        UserFactionsCommand factionsCommand = new UserFactionsCommand();
        plugin.getCommand("civfactions").setExecutor(factionsCommand);
        plugin.getCommand("civfactions").setTabCompleter(factionsCommand);
    }
}
