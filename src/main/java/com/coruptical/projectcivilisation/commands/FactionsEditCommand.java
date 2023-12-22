package com.coruptical.projectcivilisation.commands;

import com.coruptical.projectcivilisation.ProjectCivilisation;
import com.coruptical.projectcivilisation.utils.CommandHandler;
import com.coruptical.projectcivilisation.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FactionsEditCommand implements CommandExecutor, TabCompleter {
    private final ProjectCivilisation plugin = ProjectCivilisation.getInstance();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("You must be a player to execute this command.");
            return true;
        }

        Player player = (Player) commandSender;
        if (!player.hasPermission(ProjectCivilisation.getInstance().getAdminPermission())){
            player.sendMessage(ProjectCivilisation.getInstance().getNoPermissionMessage());
            return true;
        }

        CommandHandler.handleFactionsEditCommand(player, strings);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 0) return list;
        if (args.length == 1 && args[0].length() == 0){
            list.add("set-owner");
            list.add("set-power");
            list.add("set-description");
            list.add("set-name");
            list.add("add-member");
            list.add("remove-member");
            list.add("claim");
            list.add("radiusclaim");
            list.add("autoclaim");
        }

        if (args.length == 1 && args[0].length() > 0){
            if("set-owner".startsWith(args[0].toLowerCase())) list.add("set-owner");
            if("set-power".startsWith(args[0].toLowerCase())) list.add("set-power");
            if("set-description".startsWith(args[0].toLowerCase())) list.add("set-description");
            if("set-name".startsWith(args[0].toLowerCase())) list.add("set-name");
            if("add-member".startsWith(args[0].toLowerCase())) list.add("add-member");
            if("remove-member".startsWith(args[0].toLowerCase())) list.add("remove-member");
            if("claim".startsWith(args[0].toLowerCase())) list.add("claim");
            if("radiusclaim".startsWith(args[0].toLowerCase())) list.add("radiusclaim");
            if("autoclaim".startsWith(args[0].toLowerCase())) list.add("autoclaim");
        }

        if (args.length == 2 && args[1].length() == 0){
            list.addAll(plugin.getNamesIdsMap().keySet());
        }

        if (args.length == 2 && args[1].length() > 0){
            for (String name : plugin.getNamesIdsMap().keySet()){
                if (name.toLowerCase().startsWith(args[1].toLowerCase())) list.add(name);
            }
        }

        if (args.length == 3 && args[2].length() == 0){
            list.add("<value>");
        }
        return list;
    }
}
