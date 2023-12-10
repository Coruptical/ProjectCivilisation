package com.coruptical.projectcivilisation;

import com.coruptical.projectcivilisation.models.Faction;
import com.coruptical.projectcivilisation.models.FastChunk;
import com.coruptical.projectcivilisation.utils.DatabaseHandler;
import com.coruptical.projectcivilisation.utils.FactionUtils;
import com.coruptical.projectcivilisation.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class ProjectCivilisation extends JavaPlugin {
    private static ProjectCivilisation instance;

    private final HashMap<Short, Faction> idsFactionsMap = new HashMap<>();
    private final HashMap<String, Short> namesIdsMap = new HashMap<>();
    private final HashMap<String, FastChunk> chunkIdMap = new HashMap<>();
    private final HashMap<Player, FastChunk> playerLastChunkMap = new HashMap<>();

    public HashMap<Player, FastChunk> getPlayerLastChunkMap() {
        return playerLastChunkMap;
    }

    public HashMap<String, FastChunk> getChunkIdMap() {
        return chunkIdMap;
    }

    public HashMap<Short, Faction> getIdsFactionsMap() {
        return idsFactionsMap;
    }

    public HashMap<String, Short> getNamesIdsMap() {
        return namesIdsMap;
    }

    public static ProjectCivilisation getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        // Generate empty data folder
        Utils.createDataFolder(this);

        // Register utils
        Utils.registerAll(this);

        // Instantiate faction utils
        FactionUtils.getInstance().init(this);

        // Instantiate database handler
        DatabaseHandler.getInstance().init(this);
        DatabaseHandler.getInstance().loadFactions();
    }

    @Override
    public void onDisable() {

    }

    public String getAdminPermission(){
        return getConfig().getString("AdminPermission");
    }

    public String getNoPermissionMessage(){
        return ChatColor.translateAlternateColorCodes('&',
                getConfig().getString("NoPermissionMessage"));
    }

    public String getPrefix(){
        return ChatColor.translateAlternateColorCodes('&',
                getConfig().getString("MessagePrefix"));
    }
}
