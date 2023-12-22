package com.coruptical.projectcivilisation.commands;

import com.coruptical.projectcivilisation.ProjectCivilisation;
import com.coruptical.projectcivilisation.utils.CommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminFactionCommand implements CommandExecutor, TabCompleter {
    private final ProjectCivilisation plugin = ProjectCivilisation.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player p = (Player) commandSender;

            long start = System.nanoTime();
            if (!p.hasPermission(plugin.getAdminPermission())){
                p.sendMessage(plugin.getNoPermissionMessage());
                return true;
            }

            CommandHandler.handleAdminFactionCommand(p, strings);
            long elapsed = System.nanoTime() - start;
            p.sendMessage("Command runtime: " + (double) elapsed/1000000 + "ms.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 0) return list;
        if (args.length == 1 && args[0].isEmpty()){
            list.add("make");
            list.add("delete");
            list.add("list");
        }

        if (args.length == 1 && !args[0].isEmpty()){
            if("make".startsWith(args[0])) list.add("make");
            if("delete".startsWith(args[0])) list.add("delete");
            if("list".startsWith(args[0])) list.add("list");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("delete")){
            if (args[1].isEmpty()){
                list.addAll(plugin.getNamesIdsMap().keySet());
            }else {
                for (String name : plugin.getNamesIdsMap().keySet()){
                    if (name.toLowerCase().startsWith(args[1].toLowerCase())) list.add(name);
                }
            }
        }

        return list;
    }
}
