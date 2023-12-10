package com.coruptical.projectcivilisation.commands;

import com.coruptical.projectcivilisation.ProjectCivilisation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminFactionCommand implements CommandExecutor, TabCompleter {
    private final ProjectCivilisation plugin = ProjectCivilisation.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player p = (Player) commandSender;
            if (!p.hasPermission(plugin.getAdminPermission())){
                p.sendMessage(plugin.getNoPermissionMessage());
                return true;
            }


            
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
