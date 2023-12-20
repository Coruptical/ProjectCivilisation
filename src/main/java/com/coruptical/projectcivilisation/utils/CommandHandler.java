package com.coruptical.projectcivilisation.utils;

import com.coruptical.projectcivilisation.ProjectCivilisation;
import com.coruptical.projectcivilisation.models.Faction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CommandHandler{
    private static final String adminCommandUsage = "/afactions <make/delete/list> <factionName?>";
    private static final String adminEditCommandUsage = "/fedit <set-owner/set-power/set-description/set-name/add-member/remove-member/claim/radiusclaim/autoclaim> <factionName> <value>";
    private static final String userFactionsCommandUsage = "/factions <claim/radius-claim/leave> <?radius>";
    private static final String userFactionOwnerCommandUsage = "/factionsmanage <set-description/add-member/kick-member/promote/demote/claim/radius-claim>";

    public static void handleFactionsEditCommand(Player player, String[] args){
        final ProjectCivilisation plugin = ProjectCivilisation.getInstance();
        final String incorrectUsageMsg = plugin.getPrefix() + "&cIncorrect usage. &7" + adminEditCommandUsage;
        // <set-owner/set-power/set-description/set-name/add-member/remove-member/claim/radiusclaim/autoclaim> <factionName> <value>
        if (args.length == 0){
            Utils.msg(player, incorrectUsageMsg);
            return;
        }
        if (args.length == 1){
            Utils.msg(player, incorrectUsageMsg);
            return;
        }

        if (args.length == 2){
            Utils.msg(player, incorrectUsageMsg);
            return;
        }

        String arg1 = args[0];
        String factionName = args[1];
        String value = args[2];

        // check if our faction exists
        if (!FactionUtils.containsFaction(factionName)){
            Utils.msg(player, plugin.getPrefix() + "&7This faction does not exist.");
            return;
        }

        if (arg1.equalsIgnoreCase("set-owner")){
            if (!FactionUtils.playerExists(value)){
                Utils.msg(player, plugin.getPrefix() + "&7This player does not exist.");
                return;
            }

            FactionUtils.setOwner(factionName, value);
            Utils.msg(player, plugin.getPrefix() + "&aSuccessfully set owner to &f" + value);
        } else if(arg1.equalsIgnoreCase("set-name")){
            if (value.isEmpty()){
                Utils.msg(player, plugin.getPrefix() + "&7The name of the faction must be at least of length 1.");
                return;
            }

            FactionUtils.setName(factionName, value);
            Utils.msg(player, plugin.getPrefix() + "&aSuccessfully set name to " + value);
        } else if (arg1.equalsIgnoreCase("set-description")){
            if (value.isEmpty()){
                Utils.msg(player, plugin.getPrefix() + "&7The description of the faction must be at least of length 1.");
                return;
            }

            FactionUtils.setDescription(factionName, value);
            Utils.msg(player, plugin.getPrefix() + "&aSuccessfully set description to: &f" + value);
        } else if (arg1.equalsIgnoreCase("set-power")) {
            try {
                int parsedPower = Integer.parseInt(value);
                FactionUtils.setPower(factionName, parsedPower);
                Utils.msg(player, plugin.getPrefix() + "&aSuccessfully set power to " + value);
            } catch (NumberFormatException e){
                Utils.msg(player, plugin.getPrefix() + "&7The power must be given as a number.");
            }
        } else if(arg1.equalsIgnoreCase("add-member")){
            if (!FactionUtils.playerExists(value)){
                Utils.msg(player, plugin.getPrefix() + "&7This player does not exist.");
                return;
            }

            FactionUtils.addMember(factionName, value);
            Utils.msg(player, plugin.getPrefix() + "&aSuccessfully added &f" + value + "&a as a member");
        } else if(arg1.equalsIgnoreCase("remove-member")){
            if (!FactionUtils.playerExists(value)){
                Utils.msg(player, plugin.getPrefix() + "&7This player does not exist.");
                return;
            }

            FactionUtils.removeMember(factionName, value);
            Utils.msg(player, plugin.getPrefix() + "&aSuccessfully removed &f" + value + "&a as a member.");
        } else if(arg1.equalsIgnoreCase("claim")){
            FactionUtils.claim(player, factionName);
            Utils.msg(player, plugin.getPrefix() + "&aSuccessfully claimed chunk at your location.");
        } else if (arg1.equalsIgnoreCase("radiusclaim")){
            try {
                int radius = Integer.parseInt(value);
                if (radius > plugin.getRadiusClaimLimit()){
                    Utils.msg(player, plugin.getPrefix() + "&7This radius is larger than the radius-claim limit.");
                    return;
                }

                int claimed = FactionUtils.radiusClaim(plugin.doAsyncRadiusClaiming(), factionName, player, radius);
                Utils.msg(player, plugin.getPrefix() + "&aSuccessfully claimed &f" + claimed + "&a chunks.");
            }catch (NumberFormatException e){
                Utils.msg(player, plugin.getPrefix() + "&7The radius must be given as an integer.");
            }
        } else if (arg1.equalsIgnoreCase("autoclaim")){
            Utils.msg(player, plugin.getPrefix() + "&cThis feature is a &5WIP&c and is not implemented yet.");
        }
    }


    public static void handleAdminFactionCommand(Player player, String[] args){
        final ProjectCivilisation plugin = ProjectCivilisation.getInstance();
        final String incorrectUsageMsg = plugin.getPrefix() + "&cIncorrect usage. &7" + adminCommandUsage;

        if (args.length == 0){
            Utils.msg(player, incorrectUsageMsg);
            return;
        }

        if (args.length == 1){
            if (!args[0].equalsIgnoreCase("list")){
                Utils.msg(player, plugin.getPrefix() +  "&cIncorrect usage. &7" + adminCommandUsage);
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

                if (memberBuilder.length() > 0){
                    memberBuilder.deleteCharAt(memberBuilder.lastIndexOf(","));
                }

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
            Utils.msg(player, plugin.getPrefix() + "&cYou must give a faction name.");
        }

        if (first.equalsIgnoreCase("make")){
            if (FactionUtils.containsFaction(factionName)){
                Utils.msg(player, plugin.getPrefix() + "&7This faction already exists.");
                return;
            }
            FactionUtils.createFaction(factionName);
        }else if (first.equalsIgnoreCase("delete")){
            if (!FactionUtils.containsFaction(factionName)){
                Utils.msg(player, plugin.getPrefix() + "&7This faction does not exist.");
                return;
            }

            Faction fac = plugin.getIdsFactionsMap().get(plugin.getNamesIdsMap().get(factionName));
            short id = fac.getFactionID();
            FactionUtils.deleteFaction(id, fac.getName());
        }else{
            Utils.msg(player, incorrectUsageMsg);
        }
    }

    public static void handleFactionCommand(Player player, String[] args){
        final ProjectCivilisation plugin = ProjectCivilisation.getInstance();
        final String incorrectUsageMsg = plugin.getPrefix() + "&cIncorrect usage. &7" + userFactionsCommandUsage;

        String puuid = player.getUniqueId().toString();

        if (args.length == 0){
            Utils.msg(player, incorrectUsageMsg);
            return;
        }

        Faction playersFaction = FactionUtils.getPlayersFaction(player);

        if (playersFaction == null) {
            Utils.msg(player, plugin.getPrefix() + "&7You are not in a faction.");
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("leave")) {
                if (playersFaction.getOwnerUUID().equals(puuid)) {
                    Utils.msg(player, plugin.getPrefix() + "&7You cannot leave leave a faction you are owner of.");
                    return;
                }

                playersFaction.getMemberUUIDs().remove(puuid);
                Utils.msg(player, plugin.getPrefix() + "&aYou have successfully left &f" + playersFaction.getName());
            } else if (args[0].equalsIgnoreCase("claim")) {
                if (!player.hasPermission(plugin.getFactionAdminPermission())) {
                    player.sendMessage(plugin.getNoPermissionMessage());
                    return;
                }
                FactionUtils.claim(player, playersFaction.getName());
                Utils.msg(player, plugin.getPrefix() + "&aSuccessfully claimed chunk at your location.");
            } else {
                Utils.msg(player, incorrectUsageMsg);
            }
        }else if (args.length == 2){
            if (!args[0].equalsIgnoreCase("radiusclaim")){
                Utils.msg(player, incorrectUsageMsg);
                return;
            }

            try {
                int radius = Integer.parseInt(args[1]);

                if (radius < 1){
                    Utils.msg(player, plugin.getPrefix() + "&7The radius must be greater than 0.");
                    return;
                }

                if (radius > plugin.getRadiusClaimLimit()){
                    Utils.msg(player, plugin.getPrefix() + "&7The radius given is larger than the limit &f(" + plugin.getRadiusClaimLimit() + "&f)");
                    return;
                }

                int claimed = FactionUtils.radiusClaim(plugin.doAsyncRadiusClaiming(), playersFaction.getName(), player, radius);
                Utils.msg(player, plugin.getPrefix() + "&aSuccessfully claimed &f" + claimed + "&a chunks.");
            }catch (NumberFormatException e){
                Utils.msg(player, plugin.getPrefix() + "&7You must give the radius as an number.");
            }
        }
    }

    public static void handleManageFactionCommand(Player p, String[] args){
        final ProjectCivilisation plugin = ProjectCivilisation.getInstance();
        final String incorrectUsageMsg = plugin.getPrefix() + "&cIncorrect usage. &7" + userFactionOwnerCommandUsage;

        if (args.length == 0){
            Utils.msg(p, incorrectUsageMsg);
            return;
        }
        // <set-description/add-member/kick-member/promote/demote/claim/radius-claim>


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
