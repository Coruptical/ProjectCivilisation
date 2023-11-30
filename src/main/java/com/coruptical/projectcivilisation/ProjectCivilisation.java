package com.coruptical.projectcivilisation;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class ProjectCivilisation extends JavaPlugin {
    private static ProjectCivilisation instance;

    public static ProjectCivilisation getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();


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
