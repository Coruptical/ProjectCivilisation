package com.coruptical.projectcivilisation.commands;

import com.coruptical.projectcivilisation.ProjectCivilisation;
import com.coruptical.projectcivilisation.utils.DatabaseHandler;
import com.coruptical.projectcivilisation.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ReloadConfigCommand implements CommandExecutor {
    private final ProjectCivilisation plugin = ProjectCivilisation.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if ( commandSender instanceof Player || commandSender instanceof ConsoleCommandSender ){
            if (!commandSender.hasPermission(plugin.getAdminPermission())){
                commandSender.sendMessage(plugin.getNoPermissionMessage());
                return true;
            }

            plugin.reloadConfig();

            // Restart updating of factions into database
            DatabaseHandler.getInstance().stopSavingUpdates();
            DatabaseHandler.getInstance().startSavingUpdates();

            if (commandSender instanceof Player){
                Player p = (Player) commandSender;
                Utils.msg(p, plugin.getPrefix() + "&aSuccessfully reloaded the config!");
            }
        }
        return true;
    }
}
