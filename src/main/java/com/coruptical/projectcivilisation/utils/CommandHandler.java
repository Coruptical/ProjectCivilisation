package com.coruptical.projectcivilisation.utils;

import com.coruptical.projectcivilisation.ProjectCivilisation;
import com.coruptical.projectcivilisation.models.Faction;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;

public class CommandHandler{
    private static final String adminCommandUsage = "/afactions <make/delete/list> <factionName?>";
    private static final String adminEditCommandUsage = "/fedit <set-owner/set-power/set-description/set-name/add-member/remove-member/claim/radiusclaim/autoclaim> <factionName> <value>";
    private static final String userFactionsCommandUsage = "/factions <claim/radius-claim/leave> <?radius>";
    private static final String userFactionOwnerCommandUsage = "/factionsmanage <set-description/add-member/kick-member/promote/claim/radius-claim>";

    public static void handleFactionsEditCommand(Player player, String[] args){
        // TODO: Handle factions edit command
    }


    public static void handleAdminFactionCommand(Player player, String[] args){
        // TODO: Handle the rest of admin faction command (make/delete)
        final ProjectCivilisation plugin = ProjectCivilisation.getInstance();
        final String incorrectUsageMsg = "&cIncorrect usage. &7" + adminCommandUsage;

        if (args.length == 0){
            Utils.msg(player, incorrectUsageMsg);
            return;
        }

        if (args.length == 1){
            if (!args[0].equalsIgnoreCase("list")){
                Utils.msg(player, "&cIncorrect usage. &7" + adminCommandUsage);
                return;
            }

            for (short factionID : plugin.getIdsFactionsMap().keySet()){
                Faction fac = plugin.getIdsFactionsMap().get(factionID);

                String ownerUUID = fac.getOwnerUUID();
                StringBuilder memberBuilder = new StringBuilder();


                String ownerName = "";
                for (OfflinePlayer offlinePlayer : plugin.getServer().getOfflinePlayers()){
                    String uuid = offlinePlayer.getUniqueId().toString();
                    if (uuid.equals(ownerUUID)){
                        ownerName = offlinePlayer.getName();
                    }else{
                        if (fac.getMemberUUIDs().contains(offlinePlayer.getUniqueId().toString())){
                            memberBuilder.append(offlinePlayer.getName()).append(",");
                        }
                    }
                }
                memberBuilder.deleteCharAt(memberBuilder.lastIndexOf(","));

                String msg = factionID + ":\n" +
                        "&bName: &f" + fac.getName() + "\n"
                        + "&bDescription: &f" + fac.getDescription() + "\n"
                        + "&bOwner: &f" + ownerName + "\n"
                        + "&bTotal chunks: &f" + fac.getChunks().size() + "\n"
                        + "&bMembers: &f" + memberBuilder + "\n"
                        + "&bPower: &f" + fac.getPower() + "\n";

                Utils.msg(player, msg);
            }
            return;
        }
        // /afactions <make/delete/list> <factionName?>
        String first = args[0];
        String factionName = args[1];

        if (factionName.isEmpty()){
            Utils.msg(player, "&cYou must give a faction name.");
        }

        if (first.equalsIgnoreCase("make")){
            if (plugin.getNamesIdsMap().containsKey(factionName)){
                Utils.msg(player, "&7This faction already exists.");
                return;
            }

            Faction fac = new Faction(factionName,
                    "",
                    new HashSet<>(),
                    "",
                    new ArrayList<>(),
                    0,
                    FactionUtils.generateFactionId(plugin.getIdsFactionsMap()));

            plugin.getIdsFactionsMap().put(fac.getFactionID(), fac);
            plugin.getNamesIdsMap().put(fac.getName(), fac.getFactionID());
            DatabaseHandler.getInstance().insertFaction(fac);
        }else if (first.equalsIgnoreCase("delete")){
            if (!plugin.getNamesIdsMap().containsKey(factionName)){
                Utils.msg(player, "&7This faction does not exist.");
                return;
            }

            Faction fac = plugin.getIdsFactionsMap().get(plugin.getNamesIdsMap().get(factionName));
            short id = fac.getFactionID();
            // TODO: Remove faction from local and database.
            plugin.getIdsFactionsMap().remove(id);
            plugin.getNamesIdsMap().remove(factionName);
            FactionUtils.removeFactionChunks(id);

            DatabaseHandler.getInstance().removeFaction(fac);
        }else{
            Utils.msg(player, incorrectUsageMsg);
        }
    }

    public static void handleFactionCommand(Player player, String[] args){
        // TODO: Handle user faction command
        String suuid = player.getUniqueId().toString();


    }

    public static void handleManageFactionCommand(Player p, String[] args){
        // TODO: Handle faction owner manage command

    }

    public static boolean isFactionOwner(Player p){
        ProjectCivilisation plugin = ProjectCivilisation.getInstance();

        String playerUUID = p.getUniqueId().toString();
        boolean ownerChecked = false;

        for (short id : plugin.getIdsFactionsMap().keySet()){
            Faction fac = plugin.getIdsFactionsMap().get(id);
            if (fac.getOwnerUUID().equals(playerUUID)){
                ownerChecked = true;
                break;
            }
        }

        return ownerChecked;
    }
}
