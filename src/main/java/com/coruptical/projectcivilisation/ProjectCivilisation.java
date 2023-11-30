package com.coruptical.projectcivilisation;

import com.coruptical.projectcivilisation.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

public final class ProjectCivilisation extends JavaPlugin {
    private static ProjectCivilisation instance;

    private HashSet<Short> factionIds = new HashSet<>();

    public HashSet<Short> getFactionIds() {
        return factionIds;
    }

    public static ProjectCivilisation getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        Utils.registerAll(this);
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
