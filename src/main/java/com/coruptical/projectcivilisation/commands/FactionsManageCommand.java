package com.coruptical.projectcivilisation.commands;

import com.coruptical.projectcivilisation.utils.CommandHandler;
import com.coruptical.projectcivilisation.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class FactionsManageCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("You must be a player to execute this command.");
            return true;
        }

        Player p = (Player) commandSender;
        if (!CommandHandler.isFactionOwner(p)){
            Utils.msg(p, "&cYou must be the faction owner to execute this command.");
            return true;
        }

        CommandHandler.handleManageFactionCommand(p, strings);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
