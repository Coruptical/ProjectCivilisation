package com.coruptical.projectcivilisation.event;

import com.coruptical.projectcivilisation.ProjectCivilisation;
import com.coruptical.projectcivilisation.models.Faction;
import com.coruptical.projectcivilisation.models.FastChunk;
import com.coruptical.projectcivilisation.utils.FactionUtils;
import com.coruptical.projectcivilisation.utils.Utils;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ChunkEnterListener implements Listener {
    private final ProjectCivilisation plugin = ProjectCivilisation.getInstance();

    private int timesRan = 0;
    private long totalTime = 0;

    @EventHandler
    public void onChunkMove(PlayerMoveEvent e ){
        long start = System.nanoTime();

        Player p = e.getPlayer();
        Chunk playersChunk = p.getLocation().getChunk();
        short chunkX = (short) playersChunk.getX();
        short chunkZ = (short) playersChunk.getZ();
        String currentChunkID = chunkX + ";" + chunkZ;

        if (!plugin.getPlayerLastChunkMap().containsKey(p)) {
            if (plugin.getChunkIdMap().containsKey(currentChunkID)) {
                FastChunk chunk = plugin.getChunkIdMap().get(currentChunkID);
                plugin.getPlayerLastChunkMap().put(p, chunk);
            } else {
                plugin.getPlayerLastChunkMap().put(p, new FastChunk(chunkX, chunkZ, (short) -1));
            }
            return;
        }

        FastChunk lastChunk = plugin.getPlayerLastChunkMap().get(p);
        short lastChunkFactionID = lastChunk.getFactionID();
        String lastFaction = FactionUtils.getNameFromID(lastChunkFactionID);

        if (!lastChunk.getId().equalsIgnoreCase(currentChunkID)){
            if (plugin.getChunkIdMap().containsKey(currentChunkID)){
                FastChunk currentChunk = plugin.getChunkIdMap().get(currentChunkID);
                if (!(currentChunk.getFactionID() == lastChunkFactionID)){
                    Utils.msg(p, "&6Entering " + FactionUtils.getNameFromID(
                            currentChunk.getFactionID()) + "'s territory.");

                    Utils.msg(p, "&7Description: " +
                            plugin.getIdsFactionsMap().get(currentChunk.getFactionID()).getDescription());

                    plugin.getPlayerLastChunkMap().replace(p, currentChunk);
                }else{
                    if (lastFaction != null){
                        Utils.msg(p, "&6Entering wilderness.");
                    }
                    plugin.getPlayerLastChunkMap().replace(p, new FastChunk(chunkX, chunkZ, (short) -1));
                }
            }
        }

        long elapsed = System.nanoTime() - start;
        totalTime += elapsed;
        timesRan++;
        if (timesRan % 100 == 0){
            plugin.getLogger().info("Time taken to run " + timesRan + " chunkEnter methods: " + (double) totalTime/1000000 + "ms");
            plugin.getLogger().info("Average time: " + (double) (totalTime/timesRan)/1000000 + "ms");
        }
    }

}
