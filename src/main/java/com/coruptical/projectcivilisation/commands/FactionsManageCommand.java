package com.coruptical.projectcivilisation.commands;

import com.coruptical.projectcivilisation.utils.CommandHandler;
import com.coruptical.projectcivilisation.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsManageCommand implements CommandExecutor {
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
}
