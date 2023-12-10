package com.coruptical.projectcivilisation.utils;

import com.coruptical.projectcivilisation.commands.AdminFactionCommand;
import com.coruptical.projectcivilisation.commands.ReloadConfigCommand;
import com.coruptical.projectcivilisation.commands.UserFactionsCommand;
import com.coruptical.projectcivilisation.event.ChunkEnterListener;
import com.coruptical.projectcivilisation.models.FastChunk;
import com.coruptical.projectcivilisation.models.StoredChunk;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static Set<FastChunk> getChunksFromStored(short factionID, byte[] chunkByteArray){
        Set<StoredChunk> storedChunks = (Set<StoredChunk>) SerializeHandler.fromBytes(chunkByteArray);

        Set<FastChunk> chunks = new HashSet<>();
        for (StoredChunk storedChunk : storedChunks){
            FastChunk c = new FastChunk(storedChunk.getX(), storedChunk.getZ(), factionID);
            chunks.add(c);
        }

        return chunks;
    }

    public static Set<StoredChunk> getStoredFromChunks(Set<FastChunk> chunks){
        Set<StoredChunk> storedChunks = new HashSet<>();
        for (FastChunk c : chunks){
            storedChunks.add(new StoredChunk(c.getX(), c.getZ()));
        }

        return storedChunks;
    }

    public static String getMembersAsString(List<String> members){
        StringBuilder builder = new StringBuilder();

        for (String member : members){
            builder.append(member).append(",");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));

        return builder.toString();
    }

    public static void createDataFolder(JavaPlugin plugin){
        File folder = new File(plugin.getDataFolder().getAbsolutePath());
        if (!folder.exists()){
            folder.mkdir();
        }
    }
}
